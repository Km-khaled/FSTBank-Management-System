package com.example.project.model;

import jakarta.persistence.Entity;

@Entity
public class CompteParticulier extends Compte {
    private String typeCompte;
    private double plafondRetrait;

    public String getTypeCompte() { return typeCompte; }
    public void setTypeCompte(String typeCompte) { this.typeCompte = typeCompte; }
    public double getPlafondRetrait() { return plafondRetrait; }
    public void setPlafondRetrait(double plafondRetrait) { this.plafondRetrait = plafondRetrait; }
}