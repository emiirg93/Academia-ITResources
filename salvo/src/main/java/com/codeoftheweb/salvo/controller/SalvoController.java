package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
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
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping("/games")
    public List<Map<String,Object>> getGames(){
        return gameRepository.findAll().stream().map(game -> game.MakeGameDTO()).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{id}")
    public Map<String,Object> getGameView(@PathVariable Long id){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        GamePlayer gp = gamePlayerRepository.findById(id).get();
        dto.put("id",gp.getGame().getId());
        dto.put("created",gp.getGame().getCreationDate());
        dto.put("gamePlayers",gp.getGame().getSetGamePlayer().stream().map(gamePlayer -> gamePlayer.MakeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("ships",gp.getShips().stream().map(ship -> ship.MakeShipDTO()).collect(Collectors.toList()));

        return dto;
    }
}
