package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    private LocalDateTime joinDate;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvos;

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player, LocalDateTime joinDate) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
    }

    public Long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    public Map<String,Object> MakeGamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",this.getId());
        dto.put("player",this.getPlayer().makePlayerDTO());
        return dto;
    }

    public static GamePlayer GetGamePlayerOpp(Game game, GamePlayer gamePlayerSelf) {
        GamePlayer GPopponent = null;

        for (GamePlayer gp :game.getGamePlayers()
        ) {
            if(gp.getId() != gamePlayerSelf.getId()){
                GPopponent = gp;
            }
        }

        return GPopponent;
    }
}
