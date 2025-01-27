package com.example.project.model;

import com.example.project.observer.Observable;
import com.example.project.observer.Observer;
import jakarta.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Transaction implements Observable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double montant;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    private String type;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "compteSource_id")
    private Compte compteSource;
    
    @ManyToOne
    @JoinColumn(name = "compteDestination_id")
    private Compte compteDestination;
    
    @Transient // Add this to prevent persistence of observers
    private List<Observer> observers = new ArrayList<>();

    // Default constructor
    public Transaction() {
        this.date = new Date();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Compte getCompteSource() { return compteSource; }
    public void setCompteSource(Compte compteSource) { this.compteSource = compteSource; }
    public Compte getCompteDestination() { return compteDestination; }
    public void setCompteDestination(Compte compteDestination) { this.compteDestination = compteDestination; }

    // Observer pattern methods
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
        observers.forEach(o -> o.update(this, arg));
    }
}