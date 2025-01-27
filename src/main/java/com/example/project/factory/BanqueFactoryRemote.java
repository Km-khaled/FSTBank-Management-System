package com.example.project.factory;

import com.example.project.model.Client;
import com.example.project.model.Compte;
import jakarta.ejb.Remote;

@Remote
public interface BanqueFactoryRemote {
    Compte creerCompte(Client client, String typeCompte);
    Client creerClient(String typeClient);
}