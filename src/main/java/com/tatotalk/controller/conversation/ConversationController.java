package com.tatotalk.controller.conversation;

import com.tatotalk.model.Employees;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/conv")
public class ConversationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        try{
            int sessionUserId = (Integer) session.getAttribute("sessionUserId");
        }catch (NullPointerException ex){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        int employeeId = Integer.parseInt(request.getParameter("employeeId"));

        Employees employee = em.createQuery("select e from Employees e where id = :userId", Employees.class)
                .setParameter("userId", employeeId)
                .getSingleResult();

        request.setAttribute("employee", employee);

        em.close();

        request.getRequestDispatcher("Conversation/partialConv.jsp").include(request, response);
    }
}
