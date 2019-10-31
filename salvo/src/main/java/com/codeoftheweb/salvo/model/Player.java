package com.codeoftheweb.salvo.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String email;

    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Player() {
    }

    public Player(String email) {
        this.email = email;
    }

    public Player(String email, String password) {
        this.email = email;
        this.password =  password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String,Object> makePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",this.getId());
        dto.put("email",this.getEmail());

        return dto;
    }

    public static Player GetOpponent(Game game, GamePlayer gamePlayerSelf) {
        Player opponent = new Player();

        for (GamePlayer gp :game.getGamePlayers()
        ) {
            if(gp.getId() != gamePlayerSelf.getId()){
                opponent = gp.getPlayer();
            }
        }

        return opponent;
    }

}
