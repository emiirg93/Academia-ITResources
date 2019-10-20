package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship,Long> {
}
