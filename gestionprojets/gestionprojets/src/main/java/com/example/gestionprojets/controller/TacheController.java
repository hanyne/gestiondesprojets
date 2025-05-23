package com.example.gestionprojets.controller;

import com.example.gestionprojets.model.Tache;
import com.example.gestionprojets.repository.TacheRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
public class TacheController {
    private final TacheRepository tacheRepository;

    public TacheController(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }

    @GetMapping
    public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
    }

    @PostMapping
    public Tache createTache(@RequestBody Tache tache) {
        return tacheRepository.save(tache);
    }

    @GetMapping("/{id}")
    public Tache getTacheById(@PathVariable Long id) {
        return tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
    }

    @PutMapping("/{id}")
    public Tache updateTache(@PathVariable Long id, @RequestBody Tache tacheDetails) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        tache.setNom(tacheDetails.getNom());
        tache.setResponsable(tacheDetails.getResponsable());
        tache.setDescription(tacheDetails.getDescription());
        tache.setEtat(tacheDetails.getEtat());
        tache.setPriorite(tacheDetails.getPriorite());
        tache.setDeadline(tacheDetails.getDeadline());
        return tacheRepository.save(tache);
    }

    @DeleteMapping("/{id}")
    public void deleteTache(@PathVariable Long id) {
        tacheRepository.deleteById(id);
    }
}