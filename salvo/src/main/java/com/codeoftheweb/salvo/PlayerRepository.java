package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//Hace que se cree una api apuntando a ese repository.
@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
