package com.example.project;

import java.util.Scanner;
import com.example.project.controller.Controleur;
import com.example.project.factory.BanqueFactory;
import com.example.project.model.*;
import jakarta.persistence.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ConsoleMain {
    private static Scanner scanner = new Scanner(System.in);
    private static BanqueFactory banqueFactory;
    private static EntityManager em;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FSTBankPU");
        em = emf.createEntityManager();
        banqueFactory = new BanqueFactory();
        banqueFactory.setEntityManager(em);

        try {
            System.out.println("1. Login Banquier");
            System.out.println("2. Login Client");
            System.out.print("Choix: ");
            int loginChoice = Integer.parseInt(scanner.nextLine());

            switch (loginChoice) {
                case 1 -> {
                    if (authenticateBanker()) {
                        showMainMenu();
                    }
                }
                case 2 -> {
                    Client client = authenticateClient();
                    if (client != null) {
                        showClientMenu(client);
                    }
                }
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private static boolean authenticateBanker() {
        System.out.println("=== Login Banquier ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        return username.equals("admin") && password.equals("admin");
    }

    private static Client authenticateClient() {
        System.out.println("\n=== Login Client ===");
        System.out.print("ID Client: ");
        Long clientId = Long.parseLong(scanner.nextLine());
        
        Client client = banqueFactory.findClientById(clientId);
        if (client != null) {
            return client;
        } else {
            System.out.println("❌ Client non trouvé!");
            return null;
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Ajouter Client");
            System.out.println("2. Créer Compte");
            System.out.println("3. Voir Transactions");
            System.out.println("4. Voir Clients/Comptes");
            System.out.println("5. Quitter");
            System.out.println("6. Gérer un client (créer comptes, consulter soldes, historique)");

            System.out.print("\nChoix: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addClient();
                case 2 -> createAccount();
                case 3 -> viewTransactions();
                case 4 -> viewClientsAndAccounts();
                case 5 -> { return; }
                case 6 -> manageClient();
                default -> System.out.println("Choix invalide");
            }
        }
    }

    private static void showClientMenu(Client client) {
        while (true) {
            System.out.println("\n=== Menu Client ===");
            System.out.println("1. Consulter soldes");
            System.out.println("2. Faire un virement");
            System.out.println("3. Faire un retrait");
            System.out.println("4. Imprimer relevé de compte");
            System.out.println("5. Quitter");
            
            System.out.print("\nChoix: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> consulterSoldes(client);
                case 2 -> faireVirement(client);
                case 3 -> faireRetrait(client);
                case 4 -> imprimerReleve(client);
                case 5 -> { return; }
                default -> System.out.println("❌ Choix invalide!");
            }
        }
    }

    private static void consulterSoldes(Client client) {
        System.out.println("\n=== Soldes des comptes ===");
        client.getComptes().forEach(compte -> 
            System.out.printf("Compte N°%d: %.2f €\n", compte.getId(), compte.getSolde())
        );
    }

    private static void faireVirement(Client client) {
        System.out.print("\nID du compte source: ");
        Long sourceId = Long.parseLong(scanner.nextLine());
        System.out.print("ID du compte destination: ");
        Long destId = Long.parseLong(scanner.nextLine());
        System.out.print("Montant: ");
        double montant = Double.parseDouble(scanner.nextLine());

        em.getTransaction().begin();
        try {
            Compte source = em.find(Compte.class, sourceId);
            Compte dest = em.find(Compte.class, destId);
            
            if (source != null && dest != null && source.getClient().equals(client)) {
                // Check if balance would go negative
                if (source.getSolde() < montant) {
                    System.out.println("❌ Solde insuffisant! Solde actuel: " + source.getSolde() + " €");
                    em.getTransaction().rollback();
                    return;
                }

                // Process transfer
                source.setSolde(source.getSolde() - montant);
                dest.setSolde(dest.getSolde() + montant);
                
                // Create transaction record
                Transaction transaction = new Transaction();
                transaction.setMontant(montant);
                transaction.setType("VIREMENT");
                transaction.setDate(new Date());
                transaction.setDescription("Virement de compte " + sourceId + " vers compte " + destId);
                transaction.setCompteSource(source);
                transaction.setCompteDestination(dest);
                em.persist(transaction);
                
                em.getTransaction().commit();
                System.out.println("✅ Virement de " + montant + " € effectué avec succès!");
                System.out.println("Nouveau solde: " + source.getSolde() + " €");
            } else {
                System.out.println("❌ Comptes invalides ou non autorisés!");
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void faireRetrait(Client client) {
        System.out.print("\nID du compte: ");
        Long compteId = Long.parseLong(scanner.nextLine());
        System.out.print("Montant: ");
        double montant = Double.parseDouble(scanner.nextLine());

        em.getTransaction().begin();
        try {
            Compte compte = em.find(Compte.class, compteId);
            
            if (compte != null && compte.getClient().equals(client)) {
                // Balance validation
                if (compte.getSolde() < montant) {
                    System.out.println("❌ Solde insuffisant! Solde actuel: " + compte.getSolde() + " €");
                    em.getTransaction().rollback();
                    return;
                }
                
                // Withdrawal limit validation
                if (compte instanceof CompteParticulier) {
                    CompteParticulier cp = (CompteParticulier) compte;
                    if (montant > cp.getPlafondRetrait()) {
                        System.out.println("❌ Dépassement du plafond de retrait: " + cp.getPlafondRetrait() + " €");
                        em.getTransaction().rollback();
                        return;
                    }
                }

                // Process withdrawal
                compte.setSolde(compte.getSolde() - montant);
                
                // Create transaction record
                Transaction transaction = new Transaction();
                transaction.setMontant(montant);
                transaction.setType("RETRAIT");
                transaction.setDate(new Date());
                transaction.setCompteSource(compte);
                transaction.setDescription("Retrait au guichet");
                em.persist(transaction);
                
                em.getTransaction().commit();
                System.out.println("✅ Retrait effectué avec succès!");
                System.out.println("Nouveau solde: " + compte.getSolde() + " €");
            } else {
                System.out.println("❌ Compte invalide!");
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void imprimerReleve(Client client) {
        System.out.print("\nID du compte: ");
        Long compteId = Long.parseLong(scanner.nextLine());
        
        Compte compte = banqueFactory.findCompteById(compteId);
        if (compte != null && compte.getClient().equals(client)) {
            System.out.println("\n=== RELEVÉ DE COMPTE ===");
            System.out.println("Client: " + client.getNom() + " " + client.getPrenom());
            System.out.println("Compte N°: " + compte.getId());
            System.out.printf("Solde actuel: %.2f €\n", compte.getSolde());
            System.out.println("\nTransactions:");
            compte.getTransactions().forEach(t ->
                System.out.printf("%s | %s | %.2f €\n", 
                    t.getDate(), t.getType(), t.getMontant())
            );
        } else {
            System.out.println("❌ Compte invalide!");
        }
    }

    private static void addClient() {
        System.out.println("\n=== Ajouter Client ===");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Type (PARTICULIER/PROFESSIONNEL): ");
        String type = scanner.nextLine();

        try {
            em.getTransaction().begin();
            if (type.equals("PARTICULIER")) {
                banqueFactory.creerClientParticulier(nom, prenom, "ACTIF");
            } else {
                System.out.print("SIRET: ");
                String siret = scanner.nextLine();
                System.out.print("Adresse: ");
                String adresse = scanner.nextLine();
                banqueFactory.creerClientProfessional(nom, siret, adresse);
            }
            em.getTransaction().commit();
            System.out.println("Client créé avec succès!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void viewClientsAndAccounts() {
        System.out.println("\n=== Liste des Clients et Comptes ===");
        banqueFactory.findAllClients().forEach(client -> {
            System.out.println("\nClient: " + client.getNom() + " " + client.getPrenom());
            System.out.println("Type: " + (client instanceof ClientParticulier ? "PARTICULIER" : "PROFESSIONNEL"));
            if (client instanceof ClientProfessionnel) {
                ClientProfessionnel prof = (ClientProfessionnel) client;

            }
            client.getComptes().forEach(compte -> 
                System.out.println("- Compte: " + compte.getId() + ", Solde: " + compte.getSolde())
            );
        });
    }

    private static void createAccount() {
        System.out.println("\n=== Créer Compte ===");
        System.out.print("ID du client: ");
        Long clientId = scanner.nextLong();
        scanner.nextLine(); // Clear buffer
        
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, clientId);
            if (client != null) {
                banqueFactory.creerCompte(client, "0.0");
                em.getTransaction().commit();
                System.out.println("Compte créé avec succès!");
            } else {
                System.out.println("Client non trouvé!");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void viewTransactions() {
        System.out.println("\n=== Liste des Transactions ===");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t ORDER BY t.date DESC", Transaction.class);
        query.getResultList().forEach(transaction -> {
            System.out.println("\n📝 Transaction #" + transaction.getId());
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("💰 Montant: %.2f €\n", transaction.getMontant());
            System.out.printf("📋 Type: %s\n", transaction.getType());
            
            // Safe date formatting
            Date transactionDate = transaction.getDate();
            if (transactionDate != null) {
                System.out.printf("📅 Date: %s\n", dateFormat.format(transactionDate));
            } else {
                System.out.println("📅 Date: Non spécifiée");
            }
            
            // Safe account reference handling
            Compte source = transaction.getCompteSource();
            Compte dest = transaction.getCompteDestination();
            if (source != null && dest != null) {
                System.out.printf("🔄 De: Compte #%d → Vers: Compte #%d\n", 
                    source.getId(), dest.getId());
            }
            
            String desc = transaction.getDescription();
            if (desc != null && !desc.trim().isEmpty()) {
                System.out.printf("📝 Description: %s\n", desc);
            }
        });
    }

    private static void manageClient() {
        System.out.println("\n========================================");
        System.out.println("          GESTION DES CLIENTS");
        System.out.println("========================================");
        System.out.print("Entrez l'ID du client à gérer: ");
        Long clientId = Long.parseLong(scanner.nextLine());
        
        Client client = em.find(Client.class, clientId);
        if (client == null) {
            System.out.println("❌ Erreur: Client avec ID " + clientId + " n'existe pas!");
            return;
        }

        // Afficher les informations du client
        System.out.println("\nInformations du client:");
        System.out.println("------------------------");
        System.out.println("Nom: " + client.getNom());
        System.out.println("Prénom: " + client.getPrenom());
        System.out.println("Type: " + client.getTypeClient());
        
        System.out.println("\nOpérations disponibles:");
        System.out.println("1. ➕ Créer un nouveau compte");
        System.out.println("2. 📊 Consulter solde et historique");
        System.out.print("\nVotre choix: ");
        
        int subChoice = Integer.parseInt(scanner.nextLine());

        switch (subChoice) {
            case 1 -> {
                System.out.println("\n=== Création de compte ===");
                System.out.print("Type de compte (PRIVE/PARTAGE/PRO): ");
                String typeCompte = scanner.nextLine();
                em.getTransaction().begin();
                banqueFactory.creerCompte(client, typeCompte);
                em.getTransaction().commit();
                System.out.println("✅ Compte créé avec succès!");
            }
            case 2 -> {
                System.out.println("\n📋 DÉTAILS DES COMPTES");
                System.out.println("============================");
                client.getComptes().forEach(compte -> {
                    System.out.println("\nCompte N°: " + compte.getId());
                    System.out.println("------------------------");
                    System.out.printf("Solde actuel: %.2f €\n", compte.getSolde());
                    System.out.println("\nHistorique des transactions:");
                    System.out.println("------------------------");
                    compte.getTransactions().forEach(t ->
                        System.out.printf("📎 %s | %s | %.2f €\n", 
                            t.getDate(), t.getType(), t.getMontant())
                    );
                });
            }
            default -> System.out.println("❌ Choix invalide!");
        }
    }
}