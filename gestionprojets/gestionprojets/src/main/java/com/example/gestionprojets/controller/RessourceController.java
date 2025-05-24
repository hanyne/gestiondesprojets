package com.example.gestionprojets.controller;

import com.example.gestionprojets.model.Ressource;
import com.example.gestionprojets.repository.RessourceRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ressources")
public class RessourceController {
    private final RessourceRepository ressourceRepository;

    public RessourceController(RessourceRepository ressourceRepository) {
        this.ressourceRepository = ressourceRepository;
    }

    static class RessourceDTO {
        private String nom;
        private String type;
        private Double cout;
        private Boolean disponibilite;

        // Getters and Setters
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Double getCout() { return cout; }
        public void setCout(Double cout) { this.cout = cout; }
        public Boolean getDisponibilite() { return disponibilite; }
        public void setDisponibilite(Boolean disponibilite) { this.disponibilite = disponibilite; }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Ressource> getAllRessources() {
        return ressourceRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ressource> createRessource(@RequestBody RessourceDTO ressourceDTO) {
        try {
            if (ressourceDTO.getNom() == null || ressourceDTO.getNom().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            if (ressourceDTO.getType() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            if (ressourceDTO.getCout() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            if (ressourceDTO.getDisponibilite() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Ressource ressource = new Ressource();
            ressource.setNom(ressourceDTO.getNom());
            ressource.setType(ressourceDTO.getType());
            ressource.setCout(ressourceDTO.getCout());
            ressource.setDisponibilite(ressourceDTO.getDisponibilite());

            Ressource savedRessource = ressourceRepository.save(ressource);
            return ResponseEntity.ok(savedRessource);
        } catch (Exception e) {
            System.out.println("Error creating Ressource: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Ressource getRessourceById(@PathVariable Long id) {
        return ressourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ressource> updateRessource(@PathVariable Long id, @RequestBody RessourceDTO ressourceDTO) {
        try {
            Ressource ressource = ressourceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));
            ressource.setNom(ressourceDTO.getNom() != null ? ressourceDTO.getNom() : ressource.getNom());
            ressource.setType(ressourceDTO.getType() != null ? ressourceDTO.getType() : ressource.getType());
            ressource.setCout(ressourceDTO.getCout() != null ? ressourceDTO.getCout() : ressource.getCout());
            ressource.setDisponibilite(ressourOceDT.getDisponibilite() != null ? ressourceDTO.getDisponibilite() : ressource.getDisponibilite());
            Ressource updatedRessource = ressourceRepository.save(ressource);
            return ResponseEntity.ok(updatedRessource);
        } catch (Exception e) {
            System.out.println("Error updating Ressource: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRessource(@PathVariable Long id) {
        try {
            ressourceRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}