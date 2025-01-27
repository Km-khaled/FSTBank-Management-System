package com.example.project.model;

import jakarta.persistence.*;
import jakarta.ejb.EJB;

import java.io.Serializable;
import java.util.List;

import com.example.project.factory.BanqueFactoryLocal;

@Entity
public class Banquier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String agence;
    private String username;
    private String password;





    @EJB
    @Transient
    private BanqueFactoryLocal banqueFactory;

    public void creerClient(String nom, String prenom, String type) {
        Client client = banqueFactory.creerClient(type);
        client.setNom(nom);
        client.setPrenom(prenom);
    }

    public Compte creerCompte(Client client, String typeCompte) {
        return banqueFactory.creerCompte(client, typeCompte);
    }

    public double consulterSolde(Compte compte) {
        return compte.getSolde();
    }

    public List<Transaction> consulterHistorique(Compte compte) {
        return compte.getTransactions();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getAgence() { return agence; }
    public void setAgence(String agence) { this.agence = agence; }
    public BanqueFactoryLocal getBanqueFactory() { return banqueFactory; }
    public void setBanqueFactory(BanqueFactoryLocal banqueFactory) { this.banqueFactory = banqueFactory; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}