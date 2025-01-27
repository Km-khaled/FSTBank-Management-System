package com.example.project.factory;

import com.example.project.model.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class BanqueFactory implements BanqueFactoryLocal, BanqueFactoryRemote {
    protected EntityManager em;

    @PersistenceContext(unitName = "FSTBankPU")
    private EntityManager entityManager;

    public Compte creerCompteParticulier(Client client, String typeCompte) {
        CompteParticulier compte;
        if (typeCompte.equals("PRIVE")) {
            compte = new ComptePrive();
            ((ComptePrive) compte).setAutoriserDecouvert(false);
            compte.setPlafondRetrait(1000.0); // Default withdrawal limit
        } else if (typeCompte.equals("PARTAGE")) {
            compte = new ComptePartage();
            compte.setPlafondRetrait(2000.0); // Higher limit for shared accounts
        } else {
            compte = new CompteParticulier();
            compte.setPlafondRetrait(500.0); // Standard limit
        }
        compte.setClient(client);
        compte.setDateOuverture(new java.util.Date());
        compte.setEstActif(true);
        compte.setSolde(0.0);
        compte.setTypeCompte(typeCompte);
        em.persist(compte);
        return compte;
    }

    public Compte creerCompteProfessional(Client client, String typeCompte) {
        CompteProfessionnel compte = new CompteProfessionnel();
        compte.setClient(client);
        compte.setDateOuverture(new java.util.Date());
        compte.setEstActif(true);
        em.persist(compte);
        return compte;
    }

    public Client creerClientParticulier(String nom, String prenom, String status) {
        ClientParticulier client = new ClientParticulier();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setStatus(status);
        client.setTypeClient("PARTICULIER");
        em.persist(client);
        return client;
    }

    public Client creerClientProfessional(String nom, String prenom, String statut) {
        ClientProfessionnel client = new ClientProfessionnel();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setTypeClient("PROFESSIONNEL");
        em.persist(client);
        return client;
    }

    public void supprimerClient(Long clientId) {
        Client client = em.find(Client.class, clientId);
        if (client != null) {
            em.remove(client);
        }
    }

    public void supprimerCompte(Long compteId) {
        Compte compte = em.find(Compte.class, compteId);
        if (compte != null) {
            em.remove(compte);
        }
    }

    public Client findClientById(Long id) {
        return em.find(Client.class, id);
    }

    public Compte findCompteById(Long id) {
        return em.find(Compte.class, id);
    }

    public List<Client> findAllClients() {
        TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
        return query.getResultList();
    }

    public List<Compte> findAllComptes() {
        TypedQuery<Compte> query = em.createQuery("SELECT c FROM Compte c", Compte.class);
        return query.getResultList();
    }

    public Client updateClient(Client client) {
        return em.merge(client);
    }

    public Compte updateCompte(Compte compte) {
        return em.merge(compte);
    }

    @Override
    public Compte creerCompte(Client client, String typeCompte) {
        if (client instanceof ClientParticulier) {
            return creerCompteParticulier(client, typeCompte);
        } else {
            return creerCompteProfessional(client, typeCompte);
        }
    }

    @Override
    public Client creerClient(String typeClient) {
        if (typeClient.equals("PARTICULIER")) {
            return creerClientParticulier("", "", "");
        } else {
            return creerClientProfessional("", "", "");
        }
    }

    public List<Client> findClientsByNom(String nom) {
        TypedQuery<Client> query = em.createQuery(
            "SELECT c FROM Client c WHERE c.nom LIKE :nom", Client.class);
        query.setParameter("nom", "%" + nom + "%");
        return query.getResultList();
    }

    public List<Compte> findComptesByClient(Client client) {
        TypedQuery<Compte> query = em.createQuery(
            "SELECT c FROM Compte c WHERE c.client = :client", Compte.class);
        query.setParameter("client", client);
        return query.getResultList();
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void persist(Banquier banquier) {
        em.persist(banquier);
    }
}