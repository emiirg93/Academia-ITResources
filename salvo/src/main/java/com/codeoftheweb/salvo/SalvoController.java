package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ShipRepository shipRepository;

    @RequestMapping("/games")
    public List<Map<String, Object>> allId() {
        return gameRepository.findAll().stream().map(game -> game.makeDTO()).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{id}")
    public Map<String, Object> findGamePlayerByID(@PathVariable Long id) {
        Map<String, Object> map = new LinkedHashMap<>();
        GamePlayer g = gamePlayerRepository.findById(id).get();
        map.put("id", g.getGame().getId());
        map.put("created", g.getGame().getCreationDate());
        map.put("gamePlayers", g.getGame().getGamePlayers().stream().map(gamePlayer -> gamePlayer.makeGamePlayerDTO()).collect(Collectors.toList()));
        map.put("ships", g.getShips().stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));

        return map;
    }

}
