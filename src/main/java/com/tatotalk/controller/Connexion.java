package com.tatotalk.controller;

import com.tatotalk.model.Employees;
import com.tatotalk.model.UserSession;
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
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet("/connexion")
public class Connexion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String jspPagePath = "/connexion/connexion.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(jspPagePath);

        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String motDePasseSaisi = request.getParameter("password");

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        Employees utilisateur = null;
        boolean motDePasseValide = false;

        try {
            TypedQuery<Employees> query = em.createQuery(
                    "SELECT u FROM Employees u WHERE u.email = :email", Employees.class);
            query.setParameter("email", email);

            utilisateur = query.getSingleResult();

             if (BCrypt.checkpw(motDePasseSaisi, utilisateur.getPassword())) {
                 motDePasseValide = true;
             }

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        if (utilisateur != null && motDePasseValide) {

            HttpSession session = request.getSession(true);

            session.setAttribute("sessionUserId", utilisateur.getId());
            session.setAttribute("sessionUserRole", utilisateur.getRoles().getName());

            EntityManagerFactory emf2 = (EntityManagerFactory) getServletContext().getAttribute("emf");
            EntityManager em2 = emf2.createEntityManager();

            em2.getTransaction().begin();

            Employees employee = em2.find(Employees.class, utilisateur.getId());

            em2.createQuery("DELETE FROM UserSession u WHERE u.employees.id = :employeeId")
                    .setParameter("employeeId", employee.getId())
                    .executeUpdate();

            UserSession userSession = new UserSession();
            userSession.setSessionId(session.getId());
            userSession.setEmployees(employee);
            userSession.setDateConnexion(LocalDateTime.now());

            em2.persist(userSession);
            em2.getTransaction().commit();
            em2.close();

            response.sendRedirect(request.getContextPath() + "/home");
        } else {

            request.setAttribute("errorMessage", "Email ou mot de passe incorrect.");

            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/connexion.jsp");
            dispatcher.forward(request, response);
        }
    }
}