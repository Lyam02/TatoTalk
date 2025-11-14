package com.tatotalk.controller; // Adaptez le package

import com.tatotalk.model.Employees;
import com.tatotalk.model.PasswordToken;
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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/newMdp")
public class NewMdp extends HttpServlet {

    /**
     * Gère le CLIC sur le lien depuis l'e-mail.
     * Vérifie le token et affiche la page de formulaire.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        PasswordToken tokenEntity = findAndValidateToken(token, em);

        if (tokenEntity != null) {
            // ----- TOKEN VALIDE -----
            // 1. Transférer le token à la JSP (pour le champ caché)
            request.setAttribute("token", token);

            // 2. Afficher la page de saisie du nouveau mot de passe
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/newMdp.jsp");
            dispatcher.forward(request, response);

        } else {
            // ----- TOKEN INVALIDE OU EXPIRÉ -----
            // 1. Mettre un message d'erreur
            request.setAttribute("errorMessage", "Le lien de réinitialisation est invalide ou a expiré. Veuillez refaire une demande.");

            // 2. Renvoyer vers la page "mot de passe oublié"
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/mdpOublie.jsp");
            dispatcher.forward(request, response);
        }

        if (em != null) {
            em.close();
        }
    }

    /**
     * Gère la SOUMISSION du formulaire de nouveau mot de passe.
     * Re-vérifie le token, valide les MDP, et met à jour la BDD.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Récupérer les données du formulaire
        String token = request.getParameter("token");
        String mdp1 = request.getParameter("password");
        String mdp2 = request.getParameter("password-confirm");

        // 2. Vérifier si les mots de passe correspondent
        if (mdp1 == null || mdp1.isEmpty() || !mdp1.equals(mdp2)) {
            request.setAttribute("errorMessage", "Les mots de passe ne correspondent pas.");
            request.setAttribute("token", token); // Important : Renvoyer le token
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/newMdp.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // 3. Re-vérifier la validité du token (l'utilisateur a pu attendre 30+ min)
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        PasswordToken tokenEntity = findAndValidateToken(token, em);

        if (tokenEntity == null) {
            // ----- TOKEN DEVENU INVALIDE PENDANT LA SAISIE -----
            request.setAttribute("errorMessage", "Votre session a expiré. Veuillez refaire une demande de réinitialisation.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/mdpOublie.jsp");
            dispatcher.forward(request, response);
            em.close();
            return;
        }

        // 4. ----- TOUT EST VALIDE : METTRE À JOUR LA BDD -----
        try {
            em.getTransaction().begin();

            // 4a. Récupérer l'employé lié au token
            Employees employe = tokenEntity.getId_Employee();

            // 4b. Mettre à jour son mot de passe
            // !! SÉCURITÉ !! : Vous devez HASHER le mot de passe ici (ex: avec BCrypt)
            // employe.setMotDePasse(hasher.hash(mdp1));
            employe.setPassword(mdp1); // Version non sécurisée pour test

            em.merge(employe); // Appliquer l'UPDATE sur l'employé

            // 4c. Supprimer le token (il est à usage unique)
            em.remove(tokenEntity);

            // 4d. Valider les changements
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Annuler en cas d'erreur
            }
            e.printStackTrace();
            // Gérer l'erreur (ex: renvoyer vers la page avec une erreur BDD)
            request.setAttribute("errorMessage", "Une erreur technique est survenue.");
            request.setAttribute("token", token);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/newMdp.jsp");
            dispatcher.forward(request, response);
            return;

        } finally {
            if (em != null) {
                em.close();
            }
        }

        // 5. ----- SUCCÈS -----
        // Rediriger vers la page de connexion avec un message de succès
        // On utilise la session "flash" pour passer un message après redirection
        HttpSession session = request.getSession();
        session.setAttribute("successMessage", "Mot de passe réinitialisé avec succès. Vous pouvez vous connecter.");
        response.sendRedirect(request.getContextPath() + "/connexion");
    }


    /**
     * Méthode utilitaire pour trouver et valider un token.
     * @param tokenString Le token (UUID) à vérifier
     * @param em L'EntityManager à utiliser
     * @return Le PasswordToken s'il est trouvé ET non expiré, sinon null.
     */
    private PasswordToken findAndValidateToken(String tokenString, EntityManager em) {
        if (tokenString == null || tokenString.isEmpty()) {
            return null;
        }

        PasswordToken tokenEntity = null;
        try {
            // 1. Chercher le token en BDD
            TypedQuery<PasswordToken> query = em.createQuery(
                    "SELECT t FROM PasswordToken t WHERE t.token = :token", PasswordToken.class);
            query.setParameter("token", tokenString);
            tokenEntity = query.getSingleResult();

            // 2. Vérifier s'il est expiré (basé sur notre recommandation)
            if (tokenEntity.getDate_reset_expiration().isBefore(LocalDateTime.now())) {
                System.out.println("Token trouvé mais expiré.");
                return null; // Token expiré
            }

            // 3. Token trouvé et valide
            return tokenEntity;

        } catch (NoResultException e) {
            System.out.println("Token non trouvé en BDD.");
            return null; // Token non trouvé
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Autre erreur
        }
    }
}