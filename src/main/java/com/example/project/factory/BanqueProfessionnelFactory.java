package com.example.project.factory;

import com.example.project.model.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class BanqueProfessionnelFactory extends BanqueFactory {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public Compte creerCompteProfessional(Client client, String typeCompte) {
        CompteProfessionnel compte = (CompteProfessionnel) super.creerCompteProfessional(client, typeCompte);
        compte.setLimiteDecouvert(5000.0);
        compte.setPlafondTransaction(50000.0);
        em.merge(compte);
        return compte;
    }

    public Client creerClientProfessionnel(String entreprise, String siret, String adresseEntreprise) {
        ClientProfessionnel client = new ClientProfessionnel();
        client.setEntreprise(entreprise);
        client.setSiret(siret);
        client.setAdresseEntreprise(adresseEntreprise);
        client.setTypeClient("PROFESSIONNEL");
        em.persist(client);
        return client;
    }

    public CompteProfessionnel creerCompteEntreprise(Client client) {
        CompteProfessionnel compte = new CompteProfessionnel();
        compte.setClient(client);
        compte.setDateOuverture(new java.util.Date());
        compte.setEstActif(true);
        compte.setLimiteDecouvert(10000.0);
        compte.setPlafondTransaction(100000.0);
        em.persist(compte);
        return compte;
    }

    public List<CompteProfessionnel> findComptesEntreprise(String siret) {
        TypedQuery<CompteProfessionnel> query = em.createQuery(
                "SELECT c FROM CompteProfessionnel c " +
                        "JOIN c.client cl " +
                        "WHERE TYPE(cl) = ClientProfessionnel " +
                        "AND TREAT(cl AS ClientProfessionnel).siret = :siret",
                CompteProfessionnel.class);
        query.setParameter("siret", siret);
        return query.getResultList();
    }
}