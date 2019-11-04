package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="locations")
    private List<String> locations;

    private int damage;

    public Ship() {
    }

    public Ship(String type, GamePlayer gamePlayer, List<String> locations) {
        this.type = type;
        this.gamePlayer = gamePlayer;
        this.locations = locations;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
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

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Map<String,Object> MakeShipDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type",this.getType());
        dto.put("locations",this.getLocations());

        return dto;
    }

    public static List<Ship> CreateListShip(Set<Ship> list){
        List<Ship> newList = new ArrayList<>();

        for (Ship o :list) {
            newList.add(o);
        }

        return newList;
    }
}
