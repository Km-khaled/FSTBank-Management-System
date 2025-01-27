package com.example.project.view;

import com.example.project.model.Client;
import com.example.project.model.Compte;
import com.example.project.model.TransactionInfo;
import com.example.project.controller.ControleurLocal;
import com.example.project.observer.Observable;
import com.example.project.observer.Observer;

public class VueBanquier implements Vue, Observer {
    private ControleurLocal controleur;

    public void creerClient(String nom, String prenom, String type) {
        try {
            controleur.getBanquier().creerClient(nom, prenom, type);
            afficherMessage("Client créé avec succès: " + nom + " " + prenom);
        } catch (Exception e) {
            afficherMessage("Erreur lors de la création du client: " + e.getMessage());
        }
    }

    public void creerCompte(Client client, String type) {
        try {
            Compte compte = controleur.getBanquier().creerCompte(client, type);
            afficherMessage("Compte créé avec succès pour: " + client.getNom());
            afficherSolde(compte.getSolde());
        } catch (Exception e) {
            afficherMessage("Erreur lors de la création du compte: " + e.getMessage());
        }
    }

    public void consulterSolde(Client client) {
        if (!client.getComptes().isEmpty()) {
            Compte compte = client.getComptes().get(0);
            afficherMessage("Solde du compte de " + client.getNom() + ":");
            afficherSolde(compte.getSolde());
        } else {
            afficherMessage("Le client n'a pas de compte");
        }
    }

    public void consulterHistorique(Compte compte) {
        afficherMessage("=== Historique des transactions ===");
        afficherMessage("Compte de: " + compte.getClient().getNom());
        compte.getTransactions().forEach(t -> 
            afficherMessage(t.getDate() + " : " + t.getType() + " - " + t.getMontant() + "€")
        );
    }

    public void faireVirement(Compte destination, double montant) {
        Client client = controleur.getClient();
        if (client != null && !client.getComptes().isEmpty()) {
            Compte compteSource = client.getComptes().get(0);
            controleur.effectuerVirement(compteSource, destination, montant);
        } else {
            afficherMessage("Aucun compte source disponible");
        }
    }

    @Override
    public void afficherSolde(double solde) {
        System.out.println("Solde: " + solde + "€");
    }

    @Override
    public void afficherMessage(String message) {
        System.out.println("Banquier >> " + message);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof TransactionInfo) {
            TransactionInfo info = (TransactionInfo) arg;
            afficherMessage(String.format("ALERTE - Transaction %s: %.2f€ - Nouveau solde: %.2f€", 
                info.getType(), info.getMontant(), info.getSoldeApres()));
        }
    }

    public void setControleur(ControleurLocal controleur) {
        this.controleur = controleur;
    }

    public ControleurLocal getControleur() {
        return controleur;
    }
}