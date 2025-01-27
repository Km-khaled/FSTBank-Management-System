package com.example.project.factory;

import com.example.project.model.*;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface BanqueFactoryLocal {


    void persist(Banquier banquier);

    Compte creerCompte(Client client, String typeCompte);
    Client creerClient(String typeClient);
    
    // Add specific methods
    Compte creerCompteParticulier(Client client, String typeCompte);
    Compte creerCompteProfessional(Client client, String typeCompte);
    Client creerClientParticulier(String nom, String prenom, String statut);
    Client creerClientProfessional(String nom, String prenom, String statut);
    Compte findCompteById(Long id);

    Client findClientById(Long id);
    List<Client> findAllClients();
    List<Compte> findAllComptes();



}