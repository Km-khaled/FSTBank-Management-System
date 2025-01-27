package com.example.project.model;

import jakarta.persistence.Entity;

@Entity
public class CompteProfessionnel extends Compte {
    private double limiteDecouvert;
    private double plafondTransaction;

    public double getLimiteDecouvert() { return limiteDecouvert; }
    public void setLimiteDecouvert(double limiteDecouvert) { this.limiteDecouvert = limiteDecouvert; }
    public double getPlafondTransaction() { return plafondTransaction; }
    public void setPlafondTransaction(double plafondTransaction) { this.plafondTransaction = plafondTransaction; }
}