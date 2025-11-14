package com.tatotalk.controller;

import com.tatotalk.model.Employees;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Import pour la session

import java.io.IOException;

@WebServlet("/connexion")
public class Connexion extends HttpServlet {

    // Méthode GET - Afficher le formulaire
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Spécifiez le chemin vers la "vue"
        String jspPagePath = "/connexion/connexion.jsp";

        // 2. Obtenez le "dispatcher"
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspPagePath);

        // 3. Transférez la requête
        dispatcher.forward(request, response);
    }

    // Méthode POST - Traiter les données du formulaire
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Récupérer les données du formulaire
        String email = request.getParameter("email");
        String motDePasseSaisi = request.getParameter("password");

        // 2. Initialiser la connexion à la base de données (JPA)
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        Employees utilisateur = null;
        boolean motDePasseValide = false;

        try {
            // 3. Chercher l'utilisateur par email
            TypedQuery<Employees> query = em.createQuery(
                    "SELECT u FROM Employees u WHERE u.email = :email", Employees.class);
            query.setParameter("email", email);

            // getSingleResult() lèvera NoResultException si aucun email n'est trouvé
            utilisateur = query.getSingleResult();

            // 4. Vérifier le mot de passe
            // !! ATTENTION SÉCURITÉ !!
            // Ceci n'est valide que si vous stockez les mots de passe en CLAIR (NON RECOMMANDÉ)
            // En production, utilisez un hash (ex: BCrypt)
            // if (BCrypt.checkpw(motDePasseSaisi, utilisateur.getMotDePasse())) {
            //     motDePasseValide = true;
            // }

            if (utilisateur.getPassword().equals(motDePasseSaisi)) {
                motDePasseValide = true;
            }

        } catch (NoResultException e) {
            // L'email n'a pas été trouvé. L'utilisateur ou le MDP est incorrect.
            // On ne fait rien, 'utilisateur' reste null et 'motDePasseValide' reste false.
        } catch (Exception e) {
            // Gérer les autres erreurs (ex: problème de base de données)
            e.printStackTrace(); // Logguer l'erreur
        } finally {
            if (em != null) {
                em.close(); // Toujours fermer l'EntityManager
            }
        }

        // 5. Décider de la redirection
        if (utilisateur != null && motDePasseValide) {
            // CONNEXION RÉUSSIE

            // 5a. Créer une session pour l'utilisateur
            HttpSession session = request.getSession(true); // 'true' crée une session si elle n'existe pas

            // 5b. Stocker les infos de l'utilisateur en session
            session.setAttribute("sessionUserId", utilisateur.getId());
            //session.setAttribute("userEmail", utilisateur.getEmail());
            // Vous pouvez stocker d'autres infos (ex: nom)

            // 5c. Rediriger vers la page d'accueil (pattern Post-Redirect-Get)
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            // ÉCHEC DE LA CONNEXION

            // 5a. Mettre un message d'erreur dans la requête
            request.setAttribute("errorMessage", "Email ou mot de passe incorrect.");

            // 5b. Transférer (forward) vers la page de connexion pour afficher l'erreur
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/connexion.jsp");
            dispatcher.forward(request, response);
        }
    }
}