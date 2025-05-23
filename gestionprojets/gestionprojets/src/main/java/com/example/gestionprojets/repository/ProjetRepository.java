package com.example.gestionprojets.repository;

import com.example.gestionprojets.model.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
}