package com.tatotalk.controller;

import com.tatotalk.model.Employees;
import com.tatotalk.model.Messages;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        try{
            int sessionUserId = (Integer) session.getAttribute("sessionUserId");
        }catch (NullPointerException ex){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }


        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        List<Employees> employees = em.createQuery("SELECT e from Employees e", Employees.class).getResultList();

        request.setAttribute("employees", employees);

        em.close();

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}
