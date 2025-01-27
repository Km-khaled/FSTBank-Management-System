package com.example.project.model;

public class TransactionInfo {
    private String type;
    private double montant;
    private double soldeApres;

    public TransactionInfo(String type, double montant, double soldeApres) {
        this.type = type;
        this.montant = montant;
        this.soldeApres = soldeApres;
    }

    public String getType() { return type; }
    public double getMontant() { return montant; }
    public double getSoldeApres() { return soldeApres; }
}