package com.example.project.model;

import com.example.project.observer.Observable;
import com.example.project.observer.Observer;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
public abstract class Compte implements Observable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double solde;
    
    @OneToMany(mappedBy = "compteSource", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
    
    @Transient
    private List<Observer> observers = new ArrayList<>();
    
    private Date dateOuverture;
    
    @ManyToOne
    private Client client;
    
    private double tauxInteret;
    private boolean estActif;



    public void setSolde(double solde) { this.solde = solde; }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object arg) {
        for (Observer observer : observers) {
            observer.update(this, arg);
        }
    }

    public void crediter(double montant) {
        this.solde += montant;
        notifyObservers(new TransactionInfo("CREDIT", montant, this.solde));
    }

    @PreUpdate
    public void validateSolde() {
        if (this instanceof CompteProfessionnel) {
            CompteProfessionnel cp = (CompteProfessionnel) this;
            if (solde < -cp.getLimiteDecouvert()) {
                throw new IllegalStateException("Dépassement de la limite de découvert");
            }
        } else if (this instanceof CompteParticulier) {
            if (solde < 0 && !((ComptePrive)this).isAutoriserDecouvert()) {
                throw new IllegalStateException("Découvert non autorisé");
            }
        }
    }

    public void debiter(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        if (this instanceof CompteParticulier && montant > ((CompteParticulier)this).getPlafondRetrait()) {
            throw new IllegalStateException("Dépassement du plafond de retrait");
        }
        this.solde -= montant;
        validateSolde();
        notifyObservers(this);
    }

    public double getSolde() {
        return this.solde;
    }

    public void ajouterTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setCompteSource(this);
    }

    public boolean verifierClient(Client client) {
        return this.client.equals(client);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
    public Date getDateOuverture() { return dateOuverture; }
    public void setDateOuverture(Date dateOuverture) { this.dateOuverture = dateOuverture; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public double getTauxInteret() { return tauxInteret; }
    public void setTauxInteret(double tauxInteret) { this.tauxInteret = tauxInteret; }
    public boolean isEstActif() { return estActif; }
    public void setEstActif(boolean estActif) { this.estActif = estActif; }
}