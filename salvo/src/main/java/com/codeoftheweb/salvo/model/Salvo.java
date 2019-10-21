package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turn;

    @ElementCollection
    @Column(name="locations")
    private List<String> locations;

    public Salvo() {
    }

    public Salvo(GamePlayer gamePlayer, int turn, List<String> locations) {
        this.gamePlayer = gamePlayer;
        this.turn = turn;
        this.locations = locations;
    }

    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getTurn() {
        return turn;
    }

    public List<String> getLocations() {
        return locations;
    }

    public static List<String> ApplicationLocations (String a, String b){
        List<String> locations = new ArrayList<>();
        locations.add(a);
        locations.add(b);

        return locations;
    }

    public static List<String> ApplicationLocations (String a,String b,String c){
        List<String> locations = new ArrayList<>();
        locations.add(a);
        locations.add(b);
        locations.add(c);

        return locations;
    }

    public Map<String,Object> makeSalvoDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn",this.getTurn());
        dto.put("player",this.getGamePlayer().getPlayer().getId());
        dto.put("locations",this.getLocations());

        return dto;
    }
}
