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
import javassist.compiler.Parser;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/conv")
public class ConversationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        int employeeId = Integer.parseInt(request.getParameter("employeeId"));

        Employees employee = em.createQuery("select e from Employees e where id = :userId", Employees.class)
                .setParameter("userId", employeeId)
                .getSingleResult();

        request.setAttribute("employee", employee);

        em.close();

        request.getRequestDispatcher("Conversation/partialConv.jsp").forward(request, response);
    }

    /*@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        HttpSession session = request.getSession();
        int employeeId = (Integer) session.getAttribute("currentConvEmployeeId");

        String messageText = request.getParameter("message");

        Employees employee = em.createQuery("select e from Employees e where e.Id = :employeeId", Employees.class)
                .setParameter("employeeId", employeeId)
                .getSingleResult();

        Messages message = new Messages();
        message.setMessage_content(messageText);
        message.setCreated_at(LocalDateTime.now());
        message.setEdited_at(LocalDateTime.now());
        message.setSendTo(employee);

        em.getTransaction().begin();

        em.persist(message);

        em.getTransaction().commit();

        List<Messages> messages = em.createQuery("select m from Messages m where m.sendTo.id = :userId", Messages.class)
                .setParameter("userId", employeeId)
                .getResultList();

        request.setAttribute("messages", messages);

        em.close();

        request.getRequestDispatcher("Conversation/partialMessage.jsp").forward(request, response);
    }*/

}
