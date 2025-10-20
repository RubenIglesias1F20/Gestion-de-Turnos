package com.tailwait.gestionturnos.repository;

import com.tailwait.gestionturnos.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByDui(String dui);
}
