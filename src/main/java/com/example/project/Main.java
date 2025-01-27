package com.example.project;

import com.example.project.controller.*;
import com.example.project.factory.*;
import com.example.project.model.*;
import com.example.project.view.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ejb.EJB;
import java.io.IOException;

@WebServlet(name = "mainServlet", urlPatterns = {"/main"})
public class Main extends HttpServlet {

    @EJB
    private BanqueFactoryLocal banqueFactory;

    @EJB
    private ControleurLocal controleur;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Initialize banking data
            initializeBankingData(request);
            // Forward to dashboard
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de l'initialisation: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void initializeBankingData(HttpServletRequest request) {
        try {
            // Créer un banquier
            Banquier banquier = new Banquier();
            banquier.setNom("Dupont");
            banquier.setPrenom("Jean");
            banquier.setAgence("Paris Centre");
            banquier.setBanqueFactory(banqueFactory);

            // Créer les vues
            VueClient vueClient = new VueClient();
            VueBanquier vueBanquier = new VueBanquier();

            // Créer un client particulier
            Client clientParticulier = banqueFactory.creerClientParticulier(
                    "Martin", "Sophie", "ACTIF");

            // Créer un compte particulier
            Compte compteParticulier = banqueFactory.creerCompteParticulier(
                    clientParticulier, "PRIVE");

            // Configurer le contrôleur
            controleur.setClient(clientParticulier);
            controleur.setVue(vueClient);
            controleur.setBanquier(banquier);

            // Configurer les vues
            vueClient.setControleur(controleur);
            vueBanquier.setControleur(controleur);

            // Ajouter les observateurs
            compteParticulier.addObserver(vueClient);
            compteParticulier.addObserver(vueBanquier);

            // Effectuer quelques opérations de test
            compteParticulier.crediter(1000.0);
            compteParticulier.debiter(300.0);

            // Créer un client professionnel
            ClientProfessionnel clientPro = (ClientProfessionnel) banqueFactory.creerClientProfessional(
                    "SARL Tech", "12345", "Paris");

            // Créer un compte professionnel
            CompteProfessionnel comptePro = (CompteProfessionnel) banqueFactory.creerCompteProfessional(
                    clientPro, "PRO");

            // Stocker les données dans la session
            request.getSession().setAttribute("clientParticulier", clientParticulier);
            request.getSession().setAttribute("compteParticulier", compteParticulier);
            request.getSession().setAttribute("clientPro", clientPro);
            request.getSession().setAttribute("comptePro", comptePro);
            request.getSession().setAttribute("banquier", banquier);

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "crediter":
                    double montantCredit = Double.parseDouble(request.getParameter("montant"));
                    String compteIdCredit = request.getParameter("compteId");
                    Compte compteCredit = banqueFactory.findCompteById(Long.parseLong(compteIdCredit));
                    compteCredit.crediter(montantCredit);
                    break;

                case "debiter":
                    double montantDebit = Double.parseDouble(request.getParameter("montant"));
                    String compteIdDebit = request.getParameter("compteId");
                    Compte compteDebit = banqueFactory.findCompteById(Long.parseLong(compteIdDebit));
                    compteDebit.debiter(montantDebit);
                    break;

                case "virement":
                    double montantVirement = Double.parseDouble(request.getParameter("montant"));
                    String compteSourceId = request.getParameter("compteSource");
                    String compteDestId = request.getParameter("compteDest");
                    Compte source = banqueFactory.findCompteById(Long.parseLong(compteSourceId));
                    Compte destination = banqueFactory.findCompteById(Long.parseLong(compteDestId));
                    controleur.effectuerVirement(source, destination, montantVirement);
                    break;
            }

            response.sendRedirect(request.getContextPath() + "/main");

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de l'opération: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}