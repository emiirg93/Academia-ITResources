package com.codeoftheweb.salvo.controller;


import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @RequestMapping("games/players/{gpid}/salvos")
    public ResponseEntity<Map<String, Object>> SetSalvoes(@PathVariable Long gpid, Authentication authentication, @RequestBody Salvo salvo) {
        ResponseEntity<Map<String, Object>> retorno = null;

        Player player = findPlayer(authentication);
        GamePlayer gamePlayer = findGamePlayer(gpid);
        salvo.setTurn(gamePlayer.getSalvos().size() + 1);
        if (Authorized(player, gamePlayer)) {
            if (gamePlayer.getSalvos().size() <= salvo.getTurn()) {
                salvoRepository.save(new Salvo(gamePlayer, salvo.getTurn(), salvo.getSalvoLocations()));
                retorno = new ResponseEntity<>(makeMap("OK", "Salvo guardado"), HttpStatus.CREATED);
            } else {
                retorno = new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.FORBIDDEN);
            }
        } else {
            retorno = new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
        }

        return retorno;
    }

    @RequestMapping("/game/{id}/players")
    public ResponseEntity<Map<String, Object>> JoinGame(@PathVariable Long id, Authentication authentication) {

        ResponseEntity<Map<String, Object>> retorno = null;

        Game game = gameRepository.getOne(id);
        Player player = findPlayer(authentication);

        if (game != null) {
            if (player != null) {
                if (game.getGamePlayers().size() < 2) {
                    retorno = new ResponseEntity<>(makeMap("gpid", JoinGame(player, game)), HttpStatus.CREATED);
                } else {
                    retorno = new ResponseEntity<>(makeMap("error", "El juego esta lleno."), HttpStatus.FORBIDDEN);
                }
            } else {
                retorno = new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
            }
        } else {
            retorno = new ResponseEntity<>(makeMap("error", "No existe el juego"), HttpStatus.FORBIDDEN);
        }

        return retorno;
    }

    @RequestMapping("/games")
    public Map<String, Object> GetGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        if (isGuest(authentication)) {
            dto.put("player", "Guest");
        } else {
            dto.put("player", playerRepository.findByEmail(authentication.getName()).makePlayerDTO());
        }

        dto.put("games", gameRepository.findAll().stream().map(game -> game.MakeGameDTO()).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> CreateGame(Authentication authentication) {
        Player player = findPlayer(authentication);
        if (player != null) {
            return new ResponseEntity<>(makeMap("gpid", createGame(player)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/game_view/{gp}")
    public ResponseEntity<Map<String, Object>> GetGameView(@PathVariable Long gp, Authentication authentication) {
        Player player = findPlayer(authentication);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gp).orElse(null);
        if (gamePlayer != null) {
            if (gamePlayer.getPlayer().equals(player)) {
                return new ResponseEntity<>(makeMap(gp), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping("/view/{gp}")
    public Map<String, Object> Getview(@PathVariable Long gp, Authentication authentication) {
        Map<String, Object> hits = new LinkedHashMap<String, Object>();
        hits.put("hits", MakeHitsDTO(gp));
        return hits;
    }

    @RequestMapping("/games/players/{gp}/ships")
    public ResponseEntity<Map<String, Object>> GetShipView(@PathVariable Long gp, Authentication authentication, @RequestBody List<Ship> listShips) {

        ResponseEntity<Map<String, Object>> retorno = null;
        Player player = findPlayer(authentication);
        GamePlayer gamePlayer = findGamePlayer(gp);

        if (Authorized(player, gamePlayer)) {

            if (gamePlayer.getShips().size() == 0) {
                listShips.forEach(ship -> ship.setGamePlayer(gamePlayer));
                listShips.forEach(ship -> shipRepository.save(ship));
                gamePlayer.getGame().setGameState("WAITINGFOROPP");
                gameRepository.save(gamePlayer.getGame());
                retorno = new ResponseEntity<>(makeMap("OK", "Barcos colocados correctamente"), HttpStatus.CREATED);
            } else {
                retorno = new ResponseEntity<>(makeMap("error", "Ya coloco sus barcos"), HttpStatus.FORBIDDEN);
            }

        } else {
            retorno = new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
        }

        return retorno;
    }


    /**
     * FUNCIONES
     **/

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<String, Object> makeMap(Long gp) {
        Map<String, Object> hits = new LinkedHashMap<String, Object>();
        hits.put("self", new ArrayList<>());
        hits.put("opponent", new ArrayList<>());
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        GamePlayer gpr = gamePlayerRepository.findById(gp).get();
        dto.put("id", gpr.getGame().getId());
        dto.put("created", gpr.getGame().getCreationDate());
        dto.put("gameState", ChangeState(gp));
        dto.put("gamePlayers", gpr.getGame().getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.MakeGamePlayerDTO())
                .sorted((o1, o2) -> (int) ((long) o1.get("id") - (long) o2.get("id")))
                .collect(Collectors.toList()));
        dto.put("ships", gpr.getShips()
                .stream()
                .map(ship -> ship.MakeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes", gpr.getGame().getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.getSalvos())
                .flatMap(salvos -> salvos.stream())
                .map(salvo -> salvo.makeSalvoDTO())
                .sorted(Comparator.comparingInt(o -> (int) o.get("turn")))
                .collect(Collectors.toList()));
        dto.put("hits", MakeHitsDTO(gp));
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Player findPlayer(Authentication authentication) {
        Player player = playerRepository.findByEmail(authentication.getName());
        return player;
    }

    private GamePlayer findGamePlayer(Long id) {
        GamePlayer gamePlayer = gamePlayerRepository.getOne(id);
        return gamePlayer;
    }

    private Long createGame(Player player) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now.format(formatter);
        Game game = gameRepository.save(new Game(now));

        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player, game.getCreationDate()));

        return gamePlayer.getId();
    }

    public Long JoinGame(Player player, Game game) {
        GamePlayer gp = gamePlayerRepository.save(new GamePlayer(game, player, game.getCreationDate()));
        return gp.getId();
    }

    public Boolean Authorized(Player player, GamePlayer gamePlayer) {
        boolean authorized = false;

        if (player != null && gamePlayer != null && gamePlayer.getPlayer().equals(player)) {
            authorized = true;
        }

        return authorized;
    }

    public Map<String, Object> MakeHitsDTO(Long gpid) {
        GamePlayer gameplayer = gamePlayerRepository.findById(gpid).get();

        Game game = gameplayer.getGame();

        GamePlayer gamePlayerOpponent = GamePlayer.GetGamePlayerOpp(game, gameplayer);

        List<Ship> shipsSelf = new ArrayList<>();
        List<Ship> shipsOpponent = new ArrayList<>();

        for (Ship s : gameplayer.getShips()) {
            shipsSelf.add(s);
        }
        ;

        if (gamePlayerOpponent != null) {
            for (Ship s : gamePlayerOpponent.getShips()) {
                shipsOpponent.add(s);
            }
            ;
        }

        Map<String, Object> hits = new LinkedHashMap<String, Object>();

        if (gamePlayerOpponent != null) {
            hits.put("self", gamePlayerOpponent.getSalvos()
                    .stream()
                    .map(salvo -> salvo.MakeSalvoHitsDTO(gamePlayerOpponent, shipsSelf))
                    .sorted(Comparator.comparingInt(o -> (int) o.get("turn")))
                    .collect(Collectors.toList()));
            hits.put("opponent", gameplayer.getSalvos()
                    .stream()
                    .map(salvo -> salvo.MakeSalvoHitsDTO(gameplayer, shipsOpponent))
                    .sorted(Comparator.comparingInt(o -> (int) o.get("turn")))
                    .collect(Collectors.toList()));
        } else {
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());

        }

        return hits;
    }

    public String ChangeState(Long gpid) {
        String state = "PLACESHIPS";

        GamePlayer gp1 = findGamePlayer(gpid);
        Game game = gp1.getGame();
        GamePlayer gp2 = GamePlayer.GetGamePlayerOpp(game, gp1);

        if (gp1.getShips() != null && gp1.getShips().size() > 0) {
            state = "WAITINGFOROPP";
            if (gp2 != null && gp2.getShips() != null && gp2.getShips().size() > 0) {
                state = "PLAY";
                if (gp1.getSalvos() != null && gp1.getSalvos().size() > 0) {
                    state = "WAIT";
                    if (gp2.getSalvos() != null && gp2.getSalvos().size() > 0) {
                        state = "WAIT";
                        if (gp1.getSalvos().size() <= gp2.getSalvos().size()) {
                            state = "PLAY";
                            if (gp1.getSalvos().size() == gp2.getSalvos().size()) {

                                if (SunkAll(gp1) && SunkAll(gp2)) {
                                    state = "TIE";
                                    if(CreateScore(game)){
                                        scoreRepository.save(new Score(game, gp1.getPlayer(), 0.5, LocalDateTime.now()));
                                        scoreRepository.save(new Score(game, gp2.getPlayer(), 0.5, LocalDateTime.now()));
                                    }
                                } else if (SunkAll(gp1)) {
                                    state = "LOSE";
                                    if(CreateScore(game)) {
                                        scoreRepository.save(new Score(game, gp1.getPlayer(), 0.0, LocalDateTime.now()));
                                        scoreRepository.save(new Score(game, gp2.getPlayer(), 1.0, LocalDateTime.now()));
                                    }
                                } else if (SunkAll(gp2)){
                                    state = "WON";
                                    if(CreateScore(game)) {
                                        scoreRepository.save(new Score(game, gp1.getPlayer(), 1.0, LocalDateTime.now()));
                                        scoreRepository.save(new Score(game, gp2.getPlayer(), 0.0, LocalDateTime.now()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return state;
    }

    public boolean SunkAll(GamePlayer gp) {
        boolean tof = false;

        GamePlayer opponent = GamePlayer.GetGamePlayerOpp(gp.getGame(), gp);
        List<Salvo> salvosOpponent = Salvo.CreateListSalvo(opponent.getSalvos());
        List<Ship> ships = Ship.CreateListShip(gp.getShips());

        Map<String, Object> map = Salvo.accumulateShipsHits(ships, salvosOpponent);

        int accumulator = (int) map.get("carrier") + (int) map.get("battleship") + (int) map.get("submarine") + (int) map.get("destroyer") + (int) map.get("patrolboat");

        int totalLocationShips = 0;

        for (Ship ship : ships) {
            totalLocationShips += ship.getLocations().size();
        }

        if (accumulator == totalLocationShips) {
            tof = true;
        }

        return tof;
    }

    public boolean CreateScore(Game game) {
        boolean tof = false;

        if (game.getScores().size() == 0) {
            tof = true;
        }
        return tof;
    }

}
