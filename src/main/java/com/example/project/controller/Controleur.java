package com.example.project.controller;

import com.example.project.model.Client;
import com.example.project.model.Compte;
import com.example.project.model.Banquier;
import com.example.project.view.Vue;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class Controleur implements ControleurLocal, ControleurRemote {
    @PersistenceContext
    private EntityManager em;
    
    private Client client;
    private Vue vue;
    private Banquier banquier;

    public Controleur() {}

    public Controleur(Client client, Vue vue) {
        this.client = client;
        this.vue = vue;
    }

    @Override
    public void mettreAJourSolde() {
        if (client != null && !client.getComptes().isEmpty()) {
            Compte compte = client.getComptes().get(0);
            vue.afficherSolde(compte.getSolde());
        }
    }

    @Override
    public void effectuerVirement(Compte source, Compte destination, double montant) {
        if (source == null || destination == null) {
            vue.afficherMessage("Comptes invalides");
            return;
        }

        if (montant <= 0) {
            vue.afficherMessage("Montant invalide");
            return;
        }

        if (source.getSolde() < montant) {
            vue.afficherMessage("Solde insuffisant");
            return;
        }

        try {
            source.debiter(montant);
            destination.crediter(montant);
            
            em.merge(source);
            em.merge(destination);
            
            vue.afficherMessage("Virement effectué avec succès");
            mettreAJourSolde();
        } catch (Exception e) {
            vue.afficherMessage("Erreur lors du virement: " + e.getMessage());
        }
    }

    // Getters and Setters
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public Vue getVue() { return vue; }
    public void setVue(Vue vue) { this.vue = vue; }
    public Banquier getBanquier() { return banquier; }
    public void setBanquier(Banquier banquier) { this.banquier = banquier; }
}