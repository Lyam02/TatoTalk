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
import org.hibernate.Length;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/mess")
public class MessageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        try{
            int sessionUserId = (Integer) session.getAttribute("sessionUserId");
        }catch (NullPointerException ex){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        int employeeId = Integer.parseInt(req.getParameter("employeeId"));

        Employees employee = em.createQuery("select e from Employees e where e.id = :employeeId", Employees.class)
                .setParameter("employeeId", employeeId).getSingleResult();

        String message = req.getParameter("message");

        if(message != null){
            Messages messages = new Messages();
            messages.setMessage_content(message);
            messages.setSendTo(employee);
            messages.setEdited_at(LocalDateTime.now());
            messages.setCreated_at(LocalDateTime.now());

            em.getTransaction().begin();
            em.persist(messages);
            em.getTransaction().commit();
        }

        List<Messages> messages = em.createQuery("select m from Messages m where m.sendTo.id = :employeeId", Messages.class)
                .setParameter("employeeId", employeeId).getResultList();


        req.setAttribute("messages", messages);
        req.setAttribute("employee", employee);

        em.close();

        req.getRequestDispatcher("Conversation/partialMess.jsp").forward(req, resp);

    }

}
