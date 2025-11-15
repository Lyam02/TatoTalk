package com.tatotalk.controller; // Adaptez le package

import com.tatotalk.model.Employees;
import com.tatotalk.model.HistoriqueMdp;
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
import java.util.List;

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
            request.setAttribute("token", token);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/newMdp.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // 3. Re-vérifier la validité du token
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        PasswordToken tokenEntity = findAndValidateToken(token, em);

        if (tokenEntity == null) {
            // ----- TOKEN DEVENU INVALIDE PENDANT LA SAISIE -----
            request.setAttribute("errorMessage", "Votre session a expiré. Veuillez refaire une demande de réinitialisation.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/mdpOublie.jsp");
            dispatcher.forward(request, response);
            if (em != null) em.close();
            return;
        }

        // 4. ----- TOUT EST VALIDE : METTRE À JOUR LA BDD -----
        try {
            em.getTransaction().begin();

            // 4a. Récupérer l'employé lié au token
            Employees employe = tokenEntity.getId_Employee();

            // -----------------------------------------------------------------
            // NOUVEAU : VÉRIFICATION DE L'HISTORIQUE (SANS HASHAGE)
            // -----------------------------------------------------------------

            // 1. Récupérer les 4 derniers mots de passe de l'historique
            TypedQuery<String> historyQuery = em.createQuery(
                    "SELECT h.old_password FROM HistoriqueMdp h " +
                            "WHERE h.Id_Employee = :employee " +
                            "ORDER BY h.date_historique DESC", String.class);
            historyQuery.setParameter("employee", employe);
            historyQuery.setMaxResults(5); // 5 derniers de l'historique
            List<String> lastPasswords = historyQuery.getResultList();

            // 2. Ajouter le mot de passe ACTUEL (c'est le 6ème)
            if (employe.getPassword() != null && !employe.getPassword().isEmpty()) {
                lastPasswords.add(employe.getPassword());
            }

            // 3. Vérifier si le NOUVEAU mdp (mdp1) est dans la liste
            if (lastPasswords.contains(mdp1)) {
                // Le mot de passe a déjà été utilisé
                em.getTransaction().rollback(); // Annuler la transaction

                request.setAttribute("errorMessage", "Vous ne pouvez pas réutiliser l'un de vos 5 derniers mots de passe.");
                request.setAttribute("token", token);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/newMdp.jsp");
                dispatcher.forward(request, response);

                if (em != null) em.close(); // Fermer l'EM
                return; // Arrêter l'exécution
            }

            // -----------------------------------------------------------------
            // FIN DE LA VÉRIFICATION
            // -----------------------------------------------------------------


            // 4b. Sauvegarder l'ANCIEN mot de passe dans l'historique
            if (employe.getPassword() != null && !employe.getPassword().isEmpty()) {
                HistoriqueMdp oldMdpEntry = new HistoriqueMdp();
                oldMdpEntry.setId_Employee(employe);
                oldMdpEntry.setOld_password(employe.getPassword()); // Stocker l'ancien mdp
                oldMdpEntry.setDate_historique(LocalDateTime.now());
                em.persist(oldMdpEntry); // Sauvegarder le nouvel objet historique
            }

            // 4c. Mettre à jour son mot de passe
            employe.setPassword(mdp1); // Version non sécurisée

            em.merge(employe); // Appliquer l'UPDATE sur l'employé

            // 4d. Supprimer le token (il est à usage unique)
            em.remove(tokenEntity);

            // 4e. Valider les changements
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