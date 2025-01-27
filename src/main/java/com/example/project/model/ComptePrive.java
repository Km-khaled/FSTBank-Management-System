package com.example.project.model;

import jakarta.persistence.Entity;

@Entity
public class ComptePrive extends CompteParticulier {
    private String titulaire;
    private boolean autoriserDecouvert;

    public String getTitulaire() { return titulaire; }
    public void setTitulaire(String titulaire) { this.titulaire = titulaire; }
    public boolean isAutoriserDecouvert() { return autoriserDecouvert; }
    public void setAutoriserDecouvert(boolean autoriserDecouvert) { this.autoriserDecouvert = autoriserDecouvert; }
}