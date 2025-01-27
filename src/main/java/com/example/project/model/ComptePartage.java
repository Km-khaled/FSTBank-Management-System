package com.example.project.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class ComptePartage extends CompteParticulier {
    @ElementCollection
    private List<String> cotitulaires = new ArrayList<>();

    public void ajouterCotitulaire(String nom) {
        cotitulaires.add(nom);
    }

    public void retirerCotitulaire(String nom) {
        cotitulaires.remove(nom);
    }

    public int getNombreCotitulaires() {
        return cotitulaires.size();
    }

    public List<String> getCotitulaires() { return cotitulaires; }
    public void setCotitulaires(List<String> cotitulaires) { this.cotitulaires = cotitulaires; }
}