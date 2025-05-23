package com.example.gestionprojets.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String type;
    private double cout;
    private String disponibilite;

    // Constructeurs
    public Ressource() {}
    public Ressource(String nom, String type, double cout, String disponibilite) {
        this.nom = nom;
        this.type = type;
        this.cout = cout;
        this.disponibilite = disponibilite;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getCout() { return cout; }
    public void setCout(double cout) { this.cout = cout; }
    public String getDisponibilite() { return disponibilite; }
    public void setDisponibilite(String disponibilite) { this.disponibilite = disponibilite; }
}