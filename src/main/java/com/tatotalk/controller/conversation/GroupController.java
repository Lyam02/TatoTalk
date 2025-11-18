package com.tatotalk.controller.conversation;

import com.tatotalk.model.Employees;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/group")
public class GroupController extends HttpServlet {

    protected void doGet (HttpServletRequest rq, HttpServletResponse resp){

        HttpSession session = rq.getSession();

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getContext("emf");
        EntityManager em = emf.createEntityManager();

        int sessionUserId = (Integer) session.getAttribute("sessionUserId");
        int employeid = Integer.parseInt(rq.getParameter("employeId"));
        String groupName = rq.getParameter("name");
        String groupDescription = rq.getParameter("description");


        Employees employees = new Employees(){

        };

    }

}
