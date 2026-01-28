package com.tatotalk.controller;

import com.tatotalk.model.Employees;
import com.tatotalk.model.Roles; // Assurez-vous que cette classe existe
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
import org.mindrot.jbcrypt.BCrypt; // N'oubliez pas l'import Bcrypt !

import java.io.IOException;
import java.util.List;

// URL plus spécifique pour l'ajout
@WebServlet("/admin/addUser")
public class AdminAddUser extends HttpServlet {

    /**
     * AFFICHE le formulaire d'ajout d'utilisateur.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Contrôle de sécurité
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé");
            return;
        }

        // 2. Charger les rôles pour le menu déroulant
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Roles> query = em.createQuery("SELECT r FROM Roles r", Roles.class);
            List<Roles> rolesList = query.getResultList();
            request.setAttribute("rolesList", rolesList);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Impossible de charger la liste des rôles.");
        } finally {
            if (em != null) em.close();
        }

        // 3. Afficher la page JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/adminAddUser.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * TRAITE la soumission du formulaire.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Contrôle de sécurité
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé");
            return;
        }

        // 2. Récupérer les données du formulaire
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String samaccountname = request.getParameter("samaccountname");
        String password = request.getParameter("password");
        String roleIdStr = request.getParameter("role_id");
        // Champs optionnels
        String displayname = request.getParameter("displayname");
        String department = request.getParameter("department");
        String service = request.getParameter("service");

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        try {
            // 3. Validation simple (vous pouvez ajouter plus de contrôles)
            if (email == null || email.isEmpty() || password == null || password.isEmpty() || roleIdStr == null) {
                throw new ServletException("Champs requis (email, mot de passe, rôle) manquants.");
            }

            // 4. Vérifier si l'email ou le samaccountname existe déjà
            if (userExists(em, email, samaccountname)) {
                request.setAttribute("errorMessage", "Cet email ou ce 'samaccountname' est déjà utilisé.");
                // Re-exécuter la logique du GET pour ré-afficher le formulaire avec les rôles
                doGet(request, response);
                return;
            }

            // 5. Tout est OK : créer l'employé
            em.getTransaction().begin();

            // 5a. Hacher le mot de passe
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            // 5b. Récupérer l'entité Rôle
            Roles selectedRole = em.find(Roles.class, Integer.parseInt(roleIdStr));
            if (selectedRole == null) {
                throw new ServletException("Rôle sélectionné invalide.");
            }

            // 5c. Créer le nouvel employé
            Employees newEmployee = new Employees(
                    nom, prenom, email, samaccountname,
                    displayname, department, selectedRole, service,
                    hashedPassword // On stocke le mot de passe HACHÉ
            );

            // 5d. Persister en BDD
            em.persist(newEmployee);
            em.getTransaction().commit();

            // 6. Rediriger avec un message de succès
            // (Idéalement vers une page listant les utilisateurs)
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Employé " + prenom + " " + nom + " ajouté avec succès.");
            response.sendRedirect(request.getContextPath() + "/home"); // Mettez l'URL de votre dashboard admin

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Annuler en cas d'erreur
            }
            e.printStackTrace();
            // En cas d'erreur, ré-afficher le formulaire avec le message
            request.setAttribute("errorMessage", "Erreur lors de l'ajout : " + e.getMessage());
            doGet(request, response); // Re-exécute le doGet pour re-peupler les rôles

        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Méthode de sécurité pour vérifier si l'utilisateur en session est un ADMIN.
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("sessionUserRole") == null) {
            return false; // Personne n'est connecté ou n'a de rôle
        }
        try {
            // On récupère le String (le nom du rôle)
            String userRole = (String) session.getAttribute("sessionUserRole");

            // On compare directement le String
            return "Admin".equalsIgnoreCase(userRole);

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Erreur (ex: casting)
        }
    }

    /**
     * Vérifie si un utilisateur existe déjà avec cet email ou samaccountname.
     */
    private boolean userExists(EntityManager em, String email, String samaccountname) {
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM Employees e WHERE e.email = :email OR e.samaccountname = :sam", Long.class);
            query.setParameter("email", email);
            query.setParameter("sam", samaccountname);
            return query.getSingleResult() > 0;
        } catch (NoResultException e) {
            return false;
        }
    }
}