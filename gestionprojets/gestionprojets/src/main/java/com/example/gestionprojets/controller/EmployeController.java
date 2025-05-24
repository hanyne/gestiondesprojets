package com.example.gestionprojets.controller;

import com.example.gestionprojets.model.Employe;
import com.example.gestionprojets.repository.EmployeRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
public class EmployeController {
    private final EmployeRepository employeRepository;

    public EmployeController(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    static class EmployeDTO {
        private String nom;
        private String role;

        // Getters and Setters
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employe> createEmploye(@RequestBody EmployeDTO employeDTO) {
        try {
            if (employeDTO.getNom() == null || employeDTO.getNom().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            if (employeDTO.getRole() == null || employeDTO.getRole().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            Employe employe = new Employe();
            employe.setNom(employeDTO.getNom());
            employe.setRole(employeDTO.getRole());

            Employe savedEmploye = employeRepository.save(employe);
            return ResponseEntity.ok(savedEmploye);
        } catch (Exception e) {
            System.out.println("Error creating Employe: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employe getEmployeById(@PathVariable Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody EmployeDTO employeDTO) {
        try {
            Employe employe = employeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
            employe.setNom(employeDTO.getNom() != null ? employeDTO.getNom() : employe.getNom());
            employe.setRole(employeDTO.getRole() != null ? employeDTO.getRole() : employe.getRole());
            Employe updatedEmploye = employeRepository.save(employe);
            return ResponseEntity.ok(updatedEmploye);
        } catch (Exception e) {
            System.out.println("Error updating Employe: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        try {
            Employe employe = employeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
            employeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Error deleting Employe: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}