package com.example.gestionprojets.controller;

import com.example.gestionprojets.model.Projet;
import com.example.gestionprojets.model.Ressource;
import com.example.gestionprojets.repository.ProjetRepository;
import com.example.gestionprojets.repository.RessourceRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {
    private final ProjetRepository projetRepository;
    private final RessourceRepository ressourceRepository;

    public ProjetController(ProjetRepository projetRepository, RessourceRepository ressourceRepository) {
        this.projetRepository = projetRepository;
        this.ressourceRepository = ressourceRepository;
    }

    static class ProjetDTO {
        private String nom;
        private String dateDebut;
        private String dateFin;
        private Double budget;
        private String statut;
        private List<Long> ressourceIds = new ArrayList<>();

        // Getters and Setters
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getDateDebut() { return dateDebut; }
        public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
        public String getDateFin() { return dateFin; }
        public void setDateFin(String dateFin) { this.dateFin = dateFin; }
        public Double getBudget() { return budget; }
        public void setBudget(Double budget) { this.budget = budget; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public List<Long> getRessourceIds() { return ressourceIds; }
        public void setRessourceIds(List<Long> ressourceIds) { this.ressourceIds = ressourceIds; }
    }

    @GetMapping
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Projet> createProjet(@RequestBody ProjetDTO projetDTO) {
        try {
            // Validate required fields
            if (projetDTO.getNom() == null || projetDTO.getNom().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            if (projetDTO.getDateDebut() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            if (projetDTO.getBudget() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Projet projet = new Projet();
            projet.setNom(projetDTO.getNom());
            projet.setDateDebut(LocalDate.parse(projetDTO.getDateDebut()));
            projet.setDateFin(projetDTO.getDateFin() != null ? LocalDate.parse(projetDTO.getDateFin()) : null);
            projet.setBudget(projetDTO.getBudget());
            projet.setStatut(projetDTO.getStatut() != null ? projetDTO.getStatut() : "En cours");

            // Handle Ressources
            List<Ressource> ressources = new ArrayList<>();
            if (projetDTO.getRessourceIds() != null && !projetDTO.getRessourceIds().isEmpty()) {
                for (Long ressourceId : projetDTO.getRessourceIds()) {
                    Ressource ressource = ressourceRepository.findById(ressourceId)
                            .orElseThrow(() -> new RuntimeException("Ressource non trouvée avec l'ID: " + ressourceId));
                    if (!ressource.getDisponibilite()) {
                        throw new RuntimeException("Ressource non disponible: " + ressource.getNom());
                    }
                    ressources.add(ressource);
                    ressource.getProjets().add(projet);
                }
            }
            projet.setRessources(ressources);

            Projet savedProjet = projetRepository.save(projet);
            return ResponseEntity.ok(savedProjet);
        } catch (Exception e) {
            System.out.println("Error creating Projet: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public Projet getProjetById(@PathVariable Long id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Projet> updateProjet(@PathVariable Long id, @RequestBody ProjetDTO projetDTO) {
        try {
            Projet projet = projetRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
            projet.setNom(projetDTO.getNom() != null ? projetDTO.getNom() : projet.getNom());
            projet.setDateDebut(projetDTO.getDateDebut() != null ? LocalDate.parse(projetDTO.getDateDebut()) : projet.getDateDebut());
            projet.setDateFin(projetDTO.getDateFin() != null ? LocalDate.parse(projetDTO.getDateFin()) : projet.getDateFin());
            projet.setBudget(projetDTO.getBudget() != null ? projetDTO.getBudget() : projet.getBudget());
            projet.setStatut(projetDTO.getStatut() != null ? projetDTO.getStatut() : projet.getStatut());

            // Update Ressources
            if (projetDTO.getRessourceIds() != null) {
                List<Ressource> newRessources = new ArrayList<>();
                for (Long ressourceId : projetDTO.getRessourceIds()) {
                    Ressource ressource = ressourceRepository.findById(ressourceId)
                            .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));
                    newRessources.add(ressource);
                    ressource.getProjets().add(projet);
                }
                projet.getRessources().clear();
                projet.setRessources(newRessources);
            }

            Projet updatedProjet = projetRepository.save(projet);
            return ResponseEntity.ok(updatedProjet);
        } catch (Exception e) {
            System.out.println("Error updating Projet: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        try {
            Projet projet = projetRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
            for (Ressource ressource : projet.getRessources()) {
                ressource.getProjets().remove(projet);
            }
            projetRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}