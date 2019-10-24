package com.codeoftheweb.salvo.controller;


import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/game/{id}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long id,Authentication authentication){

        ResponseEntity<Map<String, Object>> retorno = null;

        Game game = gameRepository.getOne(id);
        Player player = playerRepository.findByEmail(authentication.getName());

        if(game != null){
            if(player != null){
                if(game.getGamePlayers().size() < 2){
                    retorno = new ResponseEntity<>(makeMap("gpid",JoinGame(player,game)),HttpStatus.CREATED);
//                    retorno = new ResponseEntity<>(makeMap("gpid",gamePlayer.getGame().getGamePlayers().size()),HttpStatus.CREATED);
                }else{
                    retorno = new ResponseEntity<>(makeMap("error", "El juego esta lleno."), HttpStatus.FORBIDDEN);
                }
            }
            else{
                retorno = new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
            }
        }else{
            retorno = new ResponseEntity<>(makeMap("error", "No existe el juego"), HttpStatus.FORBIDDEN);
        }

        return retorno;
    }

    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        if(isGuest(authentication)){
            dto.put("player","Guest");
        }else{
            dto.put("player",playerRepository.findByEmail(authentication.getName()).makePlayerDTO());
        }

        dto.put("games",gameRepository.findAll().stream().map(game -> game.MakeGameDTO()).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping(path = "/games" , method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication){
        Player player = playerRepository.findByEmail(authentication.getName());
        if(player != null){
            return new ResponseEntity<>(makeMap("gpid", createGame(player)), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/game_view/{gp}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable Long gp, Authentication authentication){
        Player player = playerRepository.findByEmail(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.getOne(gp);
        if(gamePlayer.getPlayer().equals(player)){
            return  new ResponseEntity<>(makeMap(gp),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(makeMap("error", "No esta autorizado."), HttpStatus.UNAUTHORIZED);
        }
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<String, Object> makeMap(Long gp) {
        Map<String, Object> hits = new LinkedHashMap<String, Object>();
        hits.put("self",new ArrayList<>());
        hits.put("opponent",new ArrayList<>());
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        GamePlayer gpr = gamePlayerRepository.findById(gp).get();
        dto.put("id",gpr.getGame().getId());
        dto.put("created",gpr.getGame().getCreationDate());
        dto.put("gameState","PLACESHIPS");
        dto.put("gamePlayers",gpr.getGame().getGamePlayers().stream().map(gamePlayer -> gamePlayer.MakeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("ships",gpr.getShips().stream().map(ship -> ship.MakeShipDTO()).collect(Collectors.toList()));
        dto.put("salvoes",gpr.getGame().getGamePlayers().stream().map(gamePlayer -> gamePlayer.getSalvos()).flatMap(salvos -> salvos.stream()).map(salvo -> salvo.makeSalvoDTO()).collect(Collectors.toList()));
        dto.put("hits",hits);
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Long createGame(Player player){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now.format(formatter);
        Game game = gameRepository.save(new Game(now));

        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game,player,game.getCreationDate()));

        return gamePlayer.getId();
    }

    public Long JoinGame(Player player, Game game){
        GamePlayer gp = gamePlayerRepository.save(new GamePlayer(game,player,game.getCreationDate()));
        return gp.getId();
    }
}
