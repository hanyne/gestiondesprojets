package com.example.gestionprojets.repository;

import com.example.gestionprojets.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeRepository extends JpaRepository<Employe, Long> {
}