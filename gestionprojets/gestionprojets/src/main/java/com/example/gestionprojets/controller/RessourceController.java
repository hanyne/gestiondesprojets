package com.example.gestionprojets.controller;

import com.example.gestionprojets.model.Ressource;
import com.example.gestionprojets.repository.RessourceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ressources")
public class RessourceController {
    private final RessourceRepository ressourceRepository;

    public RessourceController(RessourceRepository ressourceRepository) {
        this.ressourceRepository = ressourceRepository;
    }

    @GetMapping
    public List<Ressource> getAllRessources() {
        return ressourceRepository.findAll();
    }

    @PostMapping
    public Ressource createRessource(@RequestBody Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    @GetMapping("/{id}")
    public Ressource getRessourceById(@PathVariable Long id) {
        return ressourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));
    }

    @PutMapping("/{id}")
    public Ressource updateRessource(@PathVariable Long id, @RequestBody Ressource ressourceDetails) {
        Ressource ressource = ressourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));
        ressource.setNom(ressourceDetails.getNom());
        ressource.setType(ressourceDetails.getType());
        ressource.setCout(ressourceDetails.getCout());
        ressource.setDisponibilite(ressourceDetails.getDisponibilite());
        return ressourceRepository.save(ressource);
    }

    @DeleteMapping("/{id}")
    public void deleteRessource(@PathVariable Long id) {
        ressourceRepository.deleteById(id);
    }
}