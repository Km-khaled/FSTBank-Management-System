package com.example.project.view;

import com.example.project.model.Compte;
import com.example.project.model.TransactionInfo;
import com.example.project.controller.ControleurLocal;
import com.example.project.observer.Observable;
import com.example.project.observer.Observer;

public class VueClient implements Vue, Observer {
    private ControleurLocal controleur;

    public void consulterSolde() {
        controleur.mettreAJourSolde();
    }

    public void faireVirement(Compte destination, double montant) {
        if (controleur.getClient() != null && !controleur.getClient().getComptes().isEmpty()) {
            Compte compteSource = controleur.getClient().getComptes().get(0);
            controleur.effectuerVirement(compteSource, destination, montant);
        } else {
            afficherMessage("Aucun compte source disponible");
        }
    }

    public void faireRetrait(double montant) {
        if (controleur.getClient() != null && !controleur.getClient().getComptes().isEmpty()) {
            Compte compte = controleur.getClient().getComptes().get(0);
            compte.debiter(montant);
            afficherMessage("Retrait effectué: " + montant);
            consulterSolde();
        } else {
            afficherMessage("Aucun compte disponible");
        }
    }

    public void imprimerReleve() {
        if (controleur.getClient() != null && !controleur.getClient().getComptes().isEmpty()) {
            Compte compte = controleur.getClient().getComptes().get(0);
            afficherMessage("=== Relevé de compte ===");
            afficherMessage("Solde actuel: " + compte.getSolde());
            afficherMessage("Transactions récentes: ");
            compte.getTransactions().forEach(t -> 
                afficherMessage(t.getDate() + " : " + t.getType() + " - " + t.getMontant())
            );
        }
    }

    @Override
    public void afficherSolde(double solde) {
        System.out.println("Solde actuel: " + solde + "€");
    }

    @Override
    public void afficherMessage(String message) {
        System.out.println("Client >> " + message);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof TransactionInfo) {
            TransactionInfo info = (TransactionInfo) arg;
            afficherMessage(String.format("Transaction %s: %.2f€ - Nouveau solde: %.2f€", 
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