package com.example.demo.inventory.domain.repository;

import com.example.demo.inventory.domain.model.PlantReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantReservationRepo extends JpaRepository<PlantReservation, Long> {

}
