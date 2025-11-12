package com.tatotalk.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StartUp implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tatotalk");
        sce.getServletContext().setAttribute("emf", emf);
    }

}
