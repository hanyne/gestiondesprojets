package com.example.gestionprojets.repository;

import com.example.gestionprojets.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacheRepository extends JpaRepository<Tache, Long> {
}