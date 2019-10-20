package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

//me crea la tabla relacional
@Entity
public class Player {

    @Id
    //sirve para la autogeneracion del id de forma secuencial ej : 1-2-3-4
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String email;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    public Player() {
    }

    public Player(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<GamePlayer> getGamePlayerSet() {
        return gamePlayers;
    }

    public void setGamePlayerSet(Set<GamePlayer> gamePlayerSet) {
        this.gamePlayers = gamePlayerSet;
    }

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", this.getId());
        map.put("email", this.getEmail());
        return map;
    }
}
