package com.tatotalk.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/mdp-oublie")
public class MdpOublieController extends HttpServlet {

    // Méthode GET - Afficher le formulaire ou récupérer des données
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Spécifiez le chemin vers la "vue" (le fichier JSP mdpOublie)
        String jspPagePath = "/connexion/mdpOublie.jsp";

        // 2. Obtenez le "dispatcher" pour cette page
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspPagePath);

        // 3. Transférez la requête (request) et la réponse (response) au JSP
        // Le serveur fait le transfert en interne, l'URL ne change pas
        dispatcher.forward(request, response);
    }

    // Méthode POST - Traiter les données du formulaire
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        Email emailLogic = new Email(emf);
        // Récupérer les paramètres du formulaire
        String email = request.getParameter("email");

        // --- ÉTAPE 2 : Vérifier si l'e-mail existe (code select sql BDD) ---
         boolean userExiste = emailLogic.emailExist(email);

        // --- ÉTAPE 3 : Si oui, générer le token et envoyer l'e-mail ---
        if (userExiste) {
            try {
                // ... (votre logique pour générer le token) ...
                String token = UUID.randomUUID().toString();

                // ... (votre logique pour sauvegarder le token en BDD) ...

                // ... (votre logique pour construire le lien) ...
                String resetLink = request.getScheme() + "://" +    // (1)
                        request.getServerName() + ":" +             // (2)
                        request.getServerPort() +                   // (3)
                        request.getContextPath() +                  // (4)
                        "/nouveau-mdp?token=" + token;              // (5)

                // ... (votre logique pour envoyer l'e-mail)
                // fonction envoie eMail

                System.out.println("Lien de réinitialisation : " + resetLink);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // --- ÉTAPE 4 : Rediriger vers la page de succès ---
            // On utilise "sendRedirect" ici, car c'est une action terminée.
            // L'URL dans le navigateur du client va changer.
            response.sendRedirect(request.getContextPath() + "/connexion/connexion.jsp");

            System.out.println(email);
        }

    }

}

class Email{
    private EntityManagerFactory emf;
    public Email(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public boolean emailExist(String email) {
        boolean emailExist = false;

        try (EntityManager em = emf.createEntityManager()) {
            String sql = "SELECT COUNT(*) FROM Employees WHERE email = :email";
            Query query = em.createQuery(sql);
            query.setParameter("email", email);
            long count = (long) query.getSingleResult();
            emailExist = count > 0;
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return emailExist;
    }
}