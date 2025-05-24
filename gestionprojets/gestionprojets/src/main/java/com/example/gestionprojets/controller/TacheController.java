package com.example.gestionprojets.controller;

import com.example.gestionprojets.model.Tache;
import com.example.gestionprojets.model.Projet;
import com.example.gestionprojets.model.Employe;
import com.example.gestionprojets.model.Ressource;
import com.example.gestionprojets.repository.TacheRepository;
import com.example.gestionprojets.repository.ProjetRepository;
import com.example.gestionprojets.repository.EmployeRepository;
import com.example.gestionprojets.repository.RessourceRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/taches")
public class TacheController {
    private final TacheRepository tacheRepository;
    private final ProjetRepository projetRepository;
    private final EmployeRepository employeRepository;
    private final RessourceRepository ressourceRepository;

    public TacheController(TacheRepository tacheRepository, ProjetRepository projetRepository,
                           EmployeRepository employeRepository, RessourceRepository ressourceRepository) {
        this.tacheRepository = tacheRepository;
        this.projetRepository = projetRepository;
        this.employeRepository = employeRepository;
        this.ressourceRepository = ressourceRepository;
    }

    static class TacheDTO {
        private String nom;
        private String description;
        private String etat;
        private String priorite;
        private String deadline;
        private Long projetId;
        private Long responsableId;
        private List<Long> ressourceIds = new ArrayList<>();

        // Getters and Setters
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getEtat() { return etat; }
        public void setEtat(String etat) { this.etat = etat; }
        public String getPriorite() { return priorite; }
        public void setPriorite(String priorite) { this.priorite = priorite; }
        public String getDeadline() { return deadline; }
        public void setDeadline(String deadline) { this.deadline = deadline; }
        public Long getProjetId() { return projetId; }
        public void setProjetId(Long projetId) { this.projetId = projetId; }
        public Long getResponsableId() { return responsableId; }
        public void setResponsableId(Long responsableId) { this.responsableId = responsableId; }
        public List<Long> getRessourceIds() { return ressourceIds; }
        public void setRessourceIds(List<Long> ressourceIds) { this.ressourceIds = ressourceIds; }
    }

    static class TacheResponseDTO {
        private Long id;
        private String nom;
        private String description;
        private String etat;
        private String priorite;
        private String deadline;
        private ProjetDTO projet;
        private EmployeDTO responsable;
        private List<RessourceDTO> ressources;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getEtat() { return etat; }
        public void setEtat(String etat) { this.etat = etat; }
        public String getPriorite() { return priorite; }
        public void setPriorite(String priorite) { this.priorite = priorite; }
        public String getDeadline() { return deadline; }
        public void setDeadline(String deadline) { this.deadline = deadline; }
        public ProjetDTO getProjet() { return projet; }
        public void setProjet(ProjetDTO projet) { this.projet = projet; }
        public EmployeDTO getResponsable() { return responsable; }
        public void setResponsable(EmployeDTO responsable) { this.responsable = responsable; }
        public List<RessourceDTO> getRessources() { return ressources; }
        public void setRessources(List<RessourceDTO> ressources) { this.ressources = ressources; }
    }

    static class ProjetDTO {
        private Long id;
        private String nom;
        private Double budget;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public Double getBudget() { return budget; }
        public void setBudget(Double budget) { this.budget = budget; }
    }

    static class EmployeDTO {
        private Long id;
        private String nom;
        private String role;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    static class RessourceDTO {
        private Long id;
        private String nom;
        private String type;
        private Double cout;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Double getCout() { return cout; }
        public void setCout(Double cout) { this.cout = cout; }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TacheResponseDTO> getAllTaches() {
        List<Tache> taches = tacheRepository.findAll();
        return taches.stream().map(tache -> {
            TacheResponseDTO dto = new TacheResponseDTO();
            dto.setId(tache.getId());
            dto.setNom(tache.getNom());
            dto.setDescription(tache.getDescription());
            dto.setEtat(tache.getEtat());
            dto.setPriorite(tache.getPriorite());
            dto.setDeadline(tache.getDeadline() != null ? tache.getDeadline().toString() : null);

            if (tache.getProjet() != null) {
                ProjetDTO projetDTO = new ProjetDTO();
                projetDTO.setId(tache.getProjet().getId());
                projetDTO.setNom(tache.getProjet().getNom());
                projetDTO.setBudget(tache.getProjet().getBudget());
                dto.setProjet(projetDTO);
            }

            if (tache.getResponsable() != null) {
                EmployeDTO employeDTO = new EmployeDTO();
                employeDTO.setId(tache.getResponsable().getId());
                employeDTO.setNom(tache.getResponsable().getNom());
                employeDTO.setRole(tache.getResponsable().getRole());
                dto.setResponsable(employeDTO);
            }

            if (tache.getRessources() != null) {
                List<RessourceDTO> ressourceDTOs = tache.getRessources().stream().map(ressource -> {
                    RessourceDTO ressourceDTO = new RessourceDTO();
                    ressourceDTO.setId(ressource.getId());
                    ressourceDTO.setNom(ressource.getNom());
                    ressourceDTO.setType(ressource.getType());
                    ressourceDTO.setCout(ressource.getCout());
                    return ressourceDTO;
                }).collect(Collectors.toList());
                dto.setRessources(ressourceDTOs);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TacheResponseDTO> createTache(@RequestBody TacheDTO tacheDTO) {
        try {
            if (tacheDTO.getNom() == null || tacheDTO.getNom().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            if (tacheDTO.getProjetId() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            if (tacheDTO.getResponsableId() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            if (tacheDTO.getDeadline() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Tache tache = new Tache();
            tache.setNom(tacheDTO.getNom());
            tache.setDescription(tacheDTO.getDescription() != null ? tacheDTO.getDescription() : "");
            tache.setEtat(tacheDTO.getEtat() != null ? tacheDTO.getEtat() : "En cours");
            tache.setPriorite(tacheDTO.getPriorite() != null ? tacheDTO.getPriorite() : "Moyenne");
            tache.setDeadline(LocalDate.parse(tacheDTO.getDeadline()));

            Projet projet = projetRepository.findById(tacheDTO.getProjetId())
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + tacheDTO.getProjetId()));
            tache.setProjet(projet);
            projet.getTaches().add(tache);

            Employe responsable = employeRepository.findById(tacheDTO.getResponsableId())
                    .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'ID: " + tacheDTO.getResponsableId()));
            tache.setResponsable(responsable);
            responsable.getTaches().add(tache);

            List<Ressource> ressources = new ArrayList<>();
            if (tacheDTO.getRessourceIds() != null && !tacheDTO.getRessourceIds().isEmpty()) {
                for (Long ressourceId : tacheDTO.getRessourceIds()) {
                    Ressource ressource = ressourceRepository.findById(ressourceId)
                            .orElseThrow(() -> new RuntimeException("Ressource non trouvée avec l'ID: " + ressourceId));
                    if (!ressource.getDisponibilite()) {
                        throw new RuntimeException("Ressource non disponible: " + ressource.getNom());
                    }
                    ressources.add(ressource);
                    ressource.getTaches().add(tache);
                }
            }
            tache.setRessources(ressources);

            Tache savedTache = tacheRepository.save(tache);

            // Convert to Response DTO
            TacheResponseDTO responseDTO = new TacheResponseDTO();
            responseDTO.setId(savedTache.getId());
            responseDTO.setNom(savedTache.getNom());
            responseDTO.setDescription(savedTache.getDescription());
            responseDTO.setEtat(savedTache.getEtat());
            responseDTO.setPriorite(savedTache.getPriorite());
            responseDTO.setDeadline(savedTache.getDeadline() != null ? savedTache.getDeadline().toString() : null);

            if (savedTache.getProjet() != null) {
                ProjetDTO projetDTO = new ProjetDTO();
                projetDTO.setId(savedTache.getProjet().getId());
                projetDTO.setNom(savedTache.getProjet().getNom());
                projetDTO.setBudget(savedTache.getProjet().getBudget());
                responseDTO.setProjet(projetDTO);
            }

            if (savedTache.getResponsable() != null) {
                EmployeDTO employeDTO = new EmployeDTO();
                employeDTO.setId(savedTache.getResponsable().getId());
                employeDTO.setNom(savedTache.getResponsable().getNom());
                employeDTO.setRole(savedTache.getResponsable().getRole());
                responseDTO.setResponsable(employeDTO);
            }

            if (savedTache.getRessources() != null) {
                List<RessourceDTO> ressourceDTOs = savedTache.getRessources().stream().map(ressource -> {
                    RessourceDTO ressourceDTO = new RessourceDTO();
                    ressourceDTO.setId(ressource.getId());
                    ressourceDTO.setNom(ressource.getNom());
                    ressourceDTO.setType(ressource.getType());
                    ressourceDTO.setCout(ressource.getCout());
                    return ressourceDTO;
                }).collect(Collectors.toList());
                responseDTO.setRessources(ressourceDTOs);
            }

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.out.println("Error creating Tache: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TacheResponseDTO getTacheById(@PathVariable Long id) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tache non trouvée"));

        TacheResponseDTO dto = new TacheResponseDTO();
        dto.setId(tache.getId());
        dto.setNom(tache.getNom());
        dto.setDescription(tache.getDescription());
        dto.setEtat(tache.getEtat());
        dto.setPriorite(tache.getPriorite());
        dto.setDeadline(tache.getDeadline() != null ? tache.getDeadline().toString() : null);

        if (tache.getProjet() != null) {
            ProjetDTO projetDTO = new ProjetDTO();
            projetDTO.setId(tache.getProjet().getId());
            projetDTO.setNom(tache.getProjet().getNom());
            projetDTO.setBudget(tache.getProjet().getBudget());
            dto.setProjet(projetDTO);
        }

        if (tache.getResponsable() != null) {
            EmployeDTO employeDTO = new EmployeDTO();
            employeDTO.setId(tache.getResponsable().getId());
            employeDTO.setNom(tache.getResponsable().getNom());
            employeDTO.setRole(tache.getResponsable().getRole());
            dto.setResponsable(employeDTO);
        }

        if (tache.getRessources() != null) {
            List<RessourceDTO> ressourceDTOs = tache.getRessources().stream().map(ressource -> {
                RessourceDTO ressourceDTO = new RessourceDTO();
                ressourceDTO.setId(ressource.getId());
                ressourceDTO.setNom(ressource.getNom());
                ressourceDTO.setType(ressource.getType());
                ressourceDTO.setCout(ressource.getCout());
                return ressourceDTO;
            }).collect(Collectors.toList());
            dto.setRessources(ressourceDTOs);
        }

        return dto;
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TacheResponseDTO> updateTache(@PathVariable Long id, @RequestBody TacheDTO tacheDTO) {
        try {
            Tache tache = tacheRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tache non trouvée"));
            tache.setEtat(tacheDTO.getEtat() != null ? tacheDTO.getEtat() : tache.getEtat());
            tache.setPriorite(tacheDTO.getPriorite() != null ? tacheDTO.getPriorite() : tache.getPriorite());
            tache.setDeadline(tacheDTO.getDeadline() != null ? LocalDate.parse(tacheDTO.getDeadline()) : tache.getDeadline());
            if (tacheDTO.getProjetId() != null) {
                Projet projet = projetRepository.findById(tacheDTO.getProjetId())
                        .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
                tache.setProjet(projet);
                projet.getTaches().add(tache);
            }

            Tache updatedTache = tacheRepository.save(tache);

            TacheResponseDTO responseDTO = new TacheResponseDTO();
            responseDTO.setId(updatedTache.getId());
            responseDTO.setNom(updatedTache.getNom());
            responseDTO.setDescription(updatedTache.getDescription());
            responseDTO.setEtat(updatedTache.getEtat());
            responseDTO.setPriorite(updatedTache.getPriorite());
            responseDTO.setDeadline(updatedTache.getDeadline() != null ? updatedTache.getDeadline().toString() : null);

            if (updatedTache.getProjet() != null) {
                ProjetDTO projetDTO = new ProjetDTO();
                projetDTO.setId(updatedTache.getProjet().getId());
                projetDTO.setNom(updatedTache.getProjet().getNom());
                projetDTO.setBudget(updatedTache.getProjet().getBudget());
                responseDTO.setProjet(projetDTO);
            }

            if (updatedTache.getResponsable() != null) {
                EmployeDTO employeDTO = new EmployeDTO();
                employeDTO.setId(updatedTache.getResponsable().getId());
                employeDTO.setNom(updatedTache.getResponsable().getNom());
                employeDTO.setRole(updatedTache.getResponsable().getRole());
                responseDTO.setResponsable(employeDTO);
            }

            if (updatedTache.getRessources() != null) {
                List<RessourceDTO> ressourceDTOs = updatedTache.getRessources().stream().map(ressource -> {
                    RessourceDTO ressourceDTO = new RessourceDTO();
                    ressourceDTO.setId(ressource.getId());
                    ressourceDTO.setNom(ressource.getNom());
                    ressourceDTO.setType(ressource.getType());
                    ressourceDTO.setCout(ressource.getCout());
                    return ressourceDTO;
                }).collect(Collectors.toList());
                responseDTO.setRessources(ressourceDTOs);
            }

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTache(@PathVariable Long id) {
        try {
            Tache tache = tacheRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tache non trouvée"));
            if (tache.getProjet() != null) {
                tache.getProjet().getTaches().remove(tache);
            }
            if (tache.getResponsable() != null) {
                tache.getResponsable().getTaches().remove(tache);
            }
            for (Ressource ressource : tache.getRessources()) {
                ressource.getTaches().remove(tache);
            }
            tacheRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/{id}/ressources/{ressourceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TacheResponseDTO> allocateRessource(@PathVariable Long id, @PathVariable Long ressourceId) {
        try {
            Tache tache = tacheRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tache non trouvée"));
            Ressource ressource = ressourceRepository.findById(ressourceId)
                    .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));
            if (!ressource.getDisponibilite() || tache.getRessources().contains(ressource)) {
                throw new RuntimeException("Ressource non disponible ou déjà allouée");
            }
            tache.getRessources().add(ressource);
            ressource.getTaches().add(tache);
            Tache updatedTache = tacheRepository.save(tache);

            TacheResponseDTO responseDTO = new TacheResponseDTO();
            responseDTO.setId(updatedTache.getId());
            responseDTO.setNom(updatedTache.getNom());
            responseDTO.setDescription(updatedTache.getDescription());
            responseDTO.setEtat(updatedTache.getEtat());
            responseDTO.setPriorite(updatedTache.getPriorite());
            responseDTO.setDeadline(updatedTache.getDeadline() != null ? updatedTache.getDeadline().toString() : null);

            if (updatedTache.getProjet() != null) {
                ProjetDTO projetDTO = new ProjetDTO();
                projetDTO.setId(updatedTache.getProjet().getId());
                projetDTO.setNom(updatedTache.getProjet().getNom());
                projetDTO.setBudget(updatedTache.getProjet().getBudget());
                responseDTO.setProjet(projetDTO);
            }

            if (updatedTache.getResponsable() != null) {
                EmployeDTO employeDTO = new EmployeDTO();
                employeDTO.setId(updatedTache.getResponsable().getId());
                employeDTO.setNom(updatedTache.getResponsable().getNom());
                employeDTO.setRole(updatedTache.getResponsable().getRole());
                responseDTO.setResponsable(employeDTO);
            }

            if (updatedTache.getRessources() != null) {
                List<RessourceDTO> ressourceDTOs = updatedTache.getRessources().stream().map(ressourceItem -> {
                    RessourceDTO ressourceDTO = new RessourceDTO();
                    ressourceDTO.setId(ressourceItem.getId());
                    ressourceDTO.setNom(ressourceItem.getNom());
                    ressourceDTO.setType(ressourceItem.getType());
                    ressourceDTO.setCout(ressourceItem.getCout());
                    return ressourceDTO;
                }).collect(Collectors.toList());
                responseDTO.setRessources(ressourceDTOs);
            }

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/{id}/cout-total", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getTotalCost(@PathVariable Long id) {
        try {
            Tache tache = tacheRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tache non trouvée"));
            double totalCost = tache.getRessources().stream()
                    .mapToDouble(Ressource::getCout)
                    .sum();
            return ResponseEntity.ok(totalCost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0.0);
        }
    }
}