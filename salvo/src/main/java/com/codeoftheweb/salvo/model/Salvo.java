package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turn;

    @ElementCollection
    @Column(name = "locations")
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

    public static List<String> ApplicationLocations(String a, String b) {
        List<String> locations = new ArrayList<>();
        locations.add(a);
        locations.add(b);

        return locations;
    }

    public static List<String> ApplicationLocations(String a, String b, String c) {
        List<String> locations = new ArrayList<>();
        locations.add(a);
        locations.add(b);
        locations.add(c);

        return locations;
    }

    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", this.getTurn());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations", this.getLocations());

        return dto;
    }

    public Map<String, Object> MakeSalvoHitsDTO(GamePlayer GPOpponent, List<Ship> shipsSelf) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        Salvo salvoOpponent = GetSalvoOpponent(GPOpponent, this.getTurn());
        List<String> salvoLocations = new ArrayList<>();
        List<Salvo> salvos = new ArrayList<>();

        for (Salvo salvo : GPOpponent.getSalvos()) {
            salvos.add(salvo);
        }

        for (String location : salvoOpponent.getLocations()) {
            salvoLocations.add(location);
        }

        dto.put("turn", this.getTurn());
        dto.put("hitLocations", HitsLocations(GPOpponent, shipsSelf, this.getTurn()));
        dto.put("damages", MakeDamages(salvoLocations, shipsSelf, salvos, this.getTurn()));
        dto.put("missed", MakeMissed(GPOpponent, shipsSelf, this.getTurn(), salvoOpponent));
        return dto;


    }

    public List<String> HitsLocations(GamePlayer gamePlayerOpp, List<Ship> shipsSelf, int turn) {
        List<String> hitsLocations = new ArrayList<>();

        Salvo salvoOpponent = GetSalvoOpponent(gamePlayerOpp, turn);

        for (Ship ship : shipsSelf) {

            for (String locationShip : ship.getLocations()) {

                for (String salvoLocations : salvoOpponent.getLocations()) {
                    if (locationShip.equals(salvoLocations)) {
                        hitsLocations.add(salvoLocations);
                    }
                }
            }
        }


        return hitsLocations;
    }

    public Salvo GetSalvoOpponent(GamePlayer gamePlayerOpp, int turn) {
        Salvo salvoOpponent = gamePlayerOpp.getSalvos().stream().filter(salvo -> salvo.getTurn() == turn).findAny().orElse(null);
        return salvoOpponent;
    }

    public Map<String, Object> MakeDamages(List<String> salvoLocation, List<Ship> shipsSelf, List<Salvo> salvos, int turn) {
        int carrierHits = 0, battleshipHits = 0, submarineHits = 0, destroyerHits = 0, patrolboatHits = 0;

        for (Ship ship : shipsSelf) {
            for (String locationShip : ship.getLocations()) {
                for (String locationSalvo : salvoLocation) {
                    if (locationShip.equals(locationSalvo)) {
                        switch (ship.getType().toLowerCase()) {
                            case "carrier":
                                carrierHits += 1;
                                break;
                            case "battle":
                                battleshipHits += 1;
                                break;
                            case "submarine":
                                submarineHits += 1;
                                break;
                            case "destroyer":
                                destroyerHits += 1;
                                break;
                            case "patrol boat":
                                patrolboatHits += 1;
                                break;
                        }
                    }
                }
            }

        }

        salvos.sort((o1, o2) -> o1.getTurn() - o2.getTurn());
        Map<String, Object> mapAccumulate = accumulateShipsHits(shipsSelf, salvos, turn);

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("carrierHits", carrierHits);
        dto.put("battleshipHits", battleshipHits);
        dto.put("submarineHits", submarineHits);
        dto.put("destroyerHits", destroyerHits);
        dto.put("patrolboatHits", patrolboatHits);

        dto.put("carrier", mapAccumulate.get("carrier"));
        dto.put("battleship", mapAccumulate.get("battleship"));
        dto.put("submarine", mapAccumulate.get("submarine"));
        dto.put("destroyer", mapAccumulate.get("destroyer"));
        dto.put("patrol boat", mapAccumulate.get("patrol boat"));

        return dto;
    }

    public int MakeMissed(GamePlayer gamePlayerOpp, List<Ship> shipsSelf, int turn, Salvo salvo) {
        int missed = 0;
        List<String> hits = HitsLocations(gamePlayerOpp, shipsSelf, turn);

        if (hits.size() < salvo.getLocations().size()) {
            missed = salvo.getLocations().size() - hits.size();
        }

        return missed;
    }

    public Map<String, Object> accumulateShipsHits(List<Ship> ships, List<Salvo> salvos, int turn) {
        int carrier = 0, battleship = 0, submarine = 0, destroyer = 0, patrolboat = 0;

        for (Salvo salvo : salvos) {
            if (salvo.getTurn() <= turn) {
                for (Ship ship : ships) {
                    for (String salvoLocation : salvo.getLocations()) {
                        for (String shipLocation : ship.getLocations()) {
                            if (shipLocation.equals(salvoLocation)) {
                                switch (ship.getType().toLowerCase()) {
                                    case "carrier":
                                        carrier += 1;
                                        break;
                                    case "battleship":
                                        battleship += 1;
                                        break;
                                    case "submarine":
                                        submarine += 1;
                                        break;
                                    case "destroyer":
                                        destroyer += 1;
                                        break;
                                    case "patrol boat":
                                        patrolboat += 1;
                                        break;
                                }
                            }
                        }
                    }
                }
            } else
                break;
        }


        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        dto.put("carrier", carrier);
        dto.put("battleship", battleship);
        dto.put("submarine", submarine);
        dto.put("destroyer", destroyer);
        dto.put("patrol boat", patrolboat);

        return dto;
    }
}
