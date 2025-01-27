package com.example.project.factory;

import com.example.project.model.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class BanqueParticulierFactory extends BanqueFactory {
    
    @PersistenceContext
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public Compte creerCompteParticulier(Client client, String typeCompte) {
        CompteParticulier compte = (CompteParticulier) super.creerCompteParticulier(client, typeCompte);
        compte.setPlafondRetrait(1000.0);
        compte.setTypeCompte(typeCompte);
        em.merge(compte);
        return compte;
    }

    @Override
    public Client creerClientParticulier(String nom, String prenom, String statut) {
        ClientParticulier client = (ClientParticulier) super.creerClientParticulier(nom, prenom, statut);
        client.setTypeClient("PARTICULIER");
        client.setStatut(statut);
        em.merge(client);
        return client;
    }

    public ComptePrive creerComptePrive(Client client) {
        ComptePrive compte = new ComptePrive();
        compte.setClient(client);
        compte.setDateOuverture(new java.util.Date());
        compte.setEstActif(true);
        compte.setTypeCompte("PRIVE");
        compte.setPlafondRetrait(1000.0);
        compte.setAutoriserDecouvert(false);
        em.persist(compte);
        return compte;
    }

    public ComptePartage creerComptePartage(Client client) {
        ComptePartage compte = new ComptePartage();
        compte.setClient(client);
        compte.setDateOuverture(new java.util.Date());
        compte.setEstActif(true);
        compte.setTypeCompte("PARTAGE");
        compte.setPlafondRetrait(2000.0);
        em.persist(compte);
        return compte;
    }
}