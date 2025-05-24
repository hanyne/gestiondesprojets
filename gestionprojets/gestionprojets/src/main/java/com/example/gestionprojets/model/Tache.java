package com.example.gestionprojets.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String description;
    private String etat;
    private String priorite;
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @JsonBackReference
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    @JsonBackReference
    private Employe responsable;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tache_ressource",
            joinColumns = @JoinColumn(name = "tache_id"),
            inverseJoinColumns = @JoinColumn(name = "ressource_id")
    )
    @JsonManagedReference
    private List<Ressource> ressources = new ArrayList<>();

    // Constructors
    public Tache() {}

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
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }
    public Employe getResponsable() { return responsable; }
    public void setResponsable(Employe responsable) { this.responsable = responsable; }
    public List<Ressource> getRessources() { return ressources; }
    public void setRessources(List<Ressource> ressources) { this.ressources = ressources; }
}