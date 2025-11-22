package com.tatotalk.config;

import com.tatotalk.model.UserSession;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebFilter("/*")
public class FilterController implements Filter {

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.equals("/") ||
            path.startsWith("/connexion") ||
            path.startsWith("/vendor") ||
            path.startsWith("/Shared") ||
            path.startsWith("/mdp-oublie") ||
            path.startsWith("/newMdp")) {

            chain.doFilter(req, resp);
            return;
        }

        EntityManagerFactory emf = (EntityManagerFactory) req.getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        String sessionId = getSessionId(req);

        if (sessionId != null){

            var sessionUser = em.createQuery("select u from UserSession u where u.id = :sessionId", UserSession.class)
                    .setParameter("sessionId", sessionId).getSingleResult();


            if (sessionUser != null){

                HttpSession session = req.getSession(true);
                session.setAttribute("sessionUserId", sessionUser.employees.id);
                session.setAttribute("sessionUserRole", sessionUser.employees.roles.name);

                String newSession = session.getId();

                if (!Objects.equals(newSession, sessionId)){

                    em.getTransaction().begin();

                    em.createQuery("update UserSession u set u.id = :newSession where u.id = :sessionId")
                            .setParameter("newSession", newSession)
                            .setParameter("sessionId", sessionId).executeUpdate();

                  em.getTransaction().commit();
                  em.close();

                    chain.doFilter(req, resp);
                    return;
                }

                chain.doFilter(req, resp);
                return;
            }

        }

    }

    private String getSessionId (HttpServletRequest req){

        Cookie[] cookies = req.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if ("JSESSIONID".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
