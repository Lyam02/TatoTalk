package com.tatotalk.controller.conversation;

import com.tatotalk.model.Messages;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/verif")
public class VerifMessController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession();

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        int employeId = Integer.parseInt(req.getParameter("employeeId"));

        int sessionUserId = (Integer) session.getAttribute("sessionUserId");

        int messageId = Integer.parseInt(req.getParameter("messageId"));

         Messages messageToVerif = em.createQuery("select m from Messages m where m.id = :messageId", Messages.class)
                 .setParameter("messageId", messageId)
                 .getSingleResult();

         Messages lastMessage = em.createQuery("select m from Messages m where m.sendBy.id = :employeId and m.sendTo.id = :sessionUserId " +
                         "order by m.edited_at DESC limit 1", Messages.class)
                 .setParameter("sessionUserId", sessionUserId).setParameter("employeId", employeId).getSingleResult();


         if (messageToVerif.edited_at.isBefore(lastMessage.edited_at)){
             resp.setContentType("application/json");
             resp.getWriter().write("{\"isTheNewest\": false}");

         }else if (messageToVerif.edited_at.isEqual(lastMessage.edited_at)){
             resp.setContentType("application/json");
             resp.getWriter().write("{\"isTheNewest\": true}");

         }else{
             resp.setContentType("application/json");
             resp.getWriter().write("{\"isTheNewest\": true}");
         }
    }

}
