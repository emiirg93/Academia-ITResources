package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addPlayer(
            @RequestParam String email, @RequestParam String password) {

        if ( email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error","Missing data"), HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>(makeMap("error","El nombre esta en uso."), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/player/edit", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> editPlayer(
            @RequestParam String email , Authentication authentication) {

        if ( email.isEmpty()) {
            return new ResponseEntity<>(makeMap("error","Missing data"), HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>(makeMap("error","El nombre esta en uso."), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByEmail(authentication.getName());

        if(player.getEmail().toLowerCase() == email.toLowerCase()){
            return new ResponseEntity<>(makeMap("error","Ingrese un email diferente al que estas usando actualmente."), HttpStatus.FORBIDDEN);
        }

        player.setEmail(email);
        playerRepository.save(player);
        return new ResponseEntity<>(makeMap("OK","Modificacion exitosa"),HttpStatus.OK);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}
