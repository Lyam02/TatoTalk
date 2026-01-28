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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/lastMessage")
public class LastMessageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        Integer sessionUserId;
        try {
            sessionUserId = (Integer) session.getAttribute("sessionUserId");
            if (sessionUserId == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        try {
            List<Messages> allMessages = em.createQuery(
                            "SELECT m FROM Messages m " +
                                    "WHERE (m.sendBy.id = :sessionUserId OR m.sendTo.id = :sessionUserId) " +
                                    "AND m.groupe IS NULL " +
                                    "ORDER BY m.created_at DESC", Messages.class)
                    .setParameter("sessionUserId", sessionUserId)
                    .getResultList();

            Map<Integer, Map<String, String>> lastMessagesMap = new HashMap<>();

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            for (Messages msg : allMessages) {
                int otherEmployeeId;
                if (msg.getSendBy().id == sessionUserId) {
                    otherEmployeeId = msg.getSendTo().id;
                } else {
                    otherEmployeeId = msg.getSendBy().id;
                }

                if (!lastMessagesMap.containsKey(otherEmployeeId)) {
                    Map<String, String> messageData = new HashMap<>();
                    messageData.put("messageContent", msg.getMessage_content());
                    messageData.put("createdAt", msg.getCreated_at().format(timeFormatter));

                    lastMessagesMap.put(otherEmployeeId, messageData);
                }
            }

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");

            boolean first = true;
            for (Map.Entry<Integer, Map<String, String>> entry : lastMessagesMap.entrySet()) {
                if (!first) {
                    jsonBuilder.append(",");
                }
                first = false;

                jsonBuilder.append("\"").append(entry.getKey()).append("\":{");
                jsonBuilder.append("\"messageContent\":\"").append(escapeJson(entry.getValue().get("messageContent"))).append("\",");
                jsonBuilder.append("\"createdAt\":\"").append(entry.getValue().get("createdAt")).append("\"");
                jsonBuilder.append("}");
            }

            jsonBuilder.append("}");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonBuilder.toString());

        } finally {
            em.close();
        }
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}