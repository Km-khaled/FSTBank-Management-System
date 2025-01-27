package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.view.Vue;
import jakarta.ejb.Local;

@Local
public interface ControleurLocal {
    void mettreAJourSolde();
    void effectuerVirement(Compte source, Compte destination, double montant);
    void setClient(Client client);
    void setVue(Vue vue);
    void setBanquier(Banquier banquier);
    
    Client getClient();
    Banquier getBanquier();
}