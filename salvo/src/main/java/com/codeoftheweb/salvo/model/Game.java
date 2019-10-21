package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> setGamePlayer;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scoreSet;

    public Game() {
    }

    public Game(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Set<GamePlayer> getSetGamePlayer() {
        return setGamePlayer;
    }

    public Set<Score> getScoreSet() {
        return scoreSet;
    }

    public Map<String,Object> MakeGameDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",this.getId());
        dto.put("created",this.getCreationDate());
        dto.put("gamePlayers",this.getSetGamePlayer()
                .stream()
                .map(gamePlayer -> gamePlayer.MakeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("scores",this.getScoreSet()
                .stream()
                .map(score -> score.makeScoreDTO())
                .collect(Collectors.toList()));
        return dto;
    }



}
