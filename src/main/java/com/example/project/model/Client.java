package com.example.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;








@Entity
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private Date dateDeNaissance;
    private String adresse;
    private String email;
    private String telephone;
    private String typeClient;
    private String status;
    
    @Column(nullable = true)
    private Double solde = 0.0; // Change to Double object


    private String username;


public Double getSolde() { return solde; }
public void setSolde(Double solde) { this.solde = solde; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Compte> comptes = new ArrayList<>();

    public void ajouterCompte(Compte compte) {
        comptes.add(compte);
        compte.setClient(this);
    }

    public void supprimerCompte(Compte compte) {
        comptes.remove(compte);
        compte.setClient(null);
    }

    public List<Compte> getComptes() {
        return comptes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public Date getDateDeNaissance() { return dateDeNaissance; }
    public void setDateDeNaissance(Date dateDeNaissance) { this.dateDeNaissance = dateDeNaissance; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(String typeClient) {
        this.typeClient = typeClient;
    }
}