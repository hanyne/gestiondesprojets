package com.example.gestionprojets.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String type;
    private Double cout;
    private Boolean disponibilite;

    @ManyToMany(mappedBy = "ressources", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "ressource-projet")
    private List<Projet> projets = new ArrayList<>();

    @ManyToMany(mappedBy = "ressources", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "ressource-tache")
    private List<Tache> taches = new ArrayList<>();

    // Constructors
    public Ressource() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getCout() { return cout; }
    public void setCout(Double cout) { this.cout = cout; }
    public Boolean getDisponibilite() { return disponibilite; }
    public void setDisponibilite(Boolean disponibilite) { this.disponibilite = disponibilite; }
    public List<Projet> getProjets() { return projets; }
    public void setProjets(List<Projet> projets) { this.projets = projets; }
    public List<Tache> getTaches() { return taches; }
    public void setTaches(List<Tache> taches) { this.taches = taches; }
}