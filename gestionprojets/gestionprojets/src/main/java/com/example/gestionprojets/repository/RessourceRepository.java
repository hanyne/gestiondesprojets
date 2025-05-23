package com.example.gestionprojets.repository;

import com.example.gestionprojets.model.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RessourceRepository extends JpaRepository<Ressource, Long> {
}