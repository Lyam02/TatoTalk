package com.tatotalk.controller.conversation;

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
import java.util.List;

@WebServlet("/delete")
public class DeleteMessController extends HttpServlet {

    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        int sessionUserId = (Integer) session.getAttribute("sessionUserId");

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        int messageId = Integer.parseInt(req.getParameter("messageId"));
        int employeeId = Integer.parseInt(req.getParameter("employeeId"));
        Employees employeeSendTo = em.find(Employees.class, employeeId);
        Employees employeeSendBy = em.find(Employees.class, sessionUserId);

        em.createQuery("delete from Messages m where m.id = :messageid")
                .setParameter("messageid", messageId).executeUpdate();
        em.getTransaction().commit();

        List<Messages> messagesList = em.createQuery("select m from Messages m where (m.sendTo.id = :employeeId and m.sendBy.id = :sessionUserId) " +
                        "or (m.sendTo.id = :sessionUserId and m.sendBy.id = :employeeId) order by m.edited_at", Messages.class)
                .setParameter("employeeId", employeeId).setParameter("sessionUserId", sessionUserId).getResultList();

        req.setAttribute("messages", messagesList);
        req.setAttribute("employeeSendTo", employeeSendTo);
        req.setAttribute("employeeSendBy", employeeSendBy);

        req.getRequestDispatcher("Conversation/partialMess.jsp").forward(req, resp);
    }

}
