package com.example.gestionprojets.controller;

import com.example.gestionprojets.model.Projet;
import com.example.gestionprojets.repository.ProjetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {
    private final ProjetRepository projetRepository;

    public ProjetController(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    @GetMapping
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    @PostMapping
    public Projet createProjet(@RequestBody Projet projet) {
        return projetRepository.save(projet);
    }

    @GetMapping("/{id}")
    public Projet getProjetById(@PathVariable Long id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
    }

    @PutMapping("/{id}")
    public Projet updateProjet(@PathVariable Long id, @RequestBody Projet projetDetails) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        projet.setNom(projetDetails.getNom());
        projet.setDateDebut(projetDetails.getDateDebut());
        projet.setDateFin(projetDetails.getDateFin());
        projet.setBudget(projetDetails.getBudget());
        projet.setStatut(projetDetails.getStatut());
        return projetRepository.save(projet);
    }

    @DeleteMapping("/{id}")
    public void deleteProjet(@PathVariable Long id) {
        projetRepository.deleteById(id);
    }
}