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

    public Game() {
    }

    public Game(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getSetGamePlayer() {
        return setGamePlayer;
    }

    public void setSetGamePlayer(Set<GamePlayer> setGamePlayer) {
        this.setGamePlayer = setGamePlayer;
    }

    public Map<String,Object> MakeGameDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",this.getId());
        dto.put("created",this.getCreationDate());
        dto.put("gamePlayers",this.getSetGamePlayer().stream().map(gamePlayer -> gamePlayer.MakeGamePlayerDTO()).collect(Collectors.toList()));

        return dto;
    }
}
