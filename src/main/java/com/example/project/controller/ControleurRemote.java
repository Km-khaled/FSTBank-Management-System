package com.example.project.controller;

import com.example.project.model.Compte;
import jakarta.ejb.Remote;

@Remote
public interface ControleurRemote {
    void mettreAJourSolde();
    void effectuerVirement(Compte source, Compte destination, double montant);
}