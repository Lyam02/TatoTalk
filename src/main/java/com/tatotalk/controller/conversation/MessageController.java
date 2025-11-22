package com.tatotalk.controller.conversation;

import com.tatotalk.model.Employees;
import com.tatotalk.model.Fichier;
import com.tatotalk.model.Messages;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/*
 *                      NOTE : CHANGEMENT DES uploadPath ET appPath
 *
 *   Mettre absolument les mêmes chemins pour la sauvegarde et la lecture des fichiers
 *   dans le répertoire locale désiré. L.43 pour uploadPath et L.137 pour appPath
 *
 */

@WebServlet("/mess")
// sert à gérer l'envoie du message et du fichier et aussi à donner des tailles max pour ne pas mettre un fichier de 1go
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 200,      // 200 MB => taille max du fichier (faire varier le 200 si on veut + ou -)
        maxRequestSize = 1024 * 1024 * 215    // 15 MB
)
public class MessageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestedFileName = req.getParameter("fileName");
        if (requestedFileName != null && !requestedFileName.isEmpty()) {

            String uploadPath = "/fichierTatoTalk";
            File file = new File(uploadPath, requestedFileName);

            if (file.exists()) {
                String contentType = getServletContext().getMimeType(file.getName());
                if (contentType == null) contentType = "application/octet-stream";
                resp.setContentType(contentType);
                resp.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
                java.nio.file.Files.copy(file.toPath(), resp.getOutputStream());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

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

        Employees employeeSendTo = em.createQuery("select e from Employees e where e.id = :employeeId", Employees.class)
                .setParameter("employeeId", employeeId).getSingleResult();

        String message = req.getParameter("message");

        int sessionUserId = (Integer) session.getAttribute("sessionUserId");

        Employees employeeSendBy = em.createQuery("select e from Employees e where e.id = :sessionUserId", Employees.class)
                .setParameter("sessionUserId", sessionUserId).getSingleResult();

        if(message != null){
            Messages messages = new Messages();
            messages.setMessage_content(message);
            messages.setSendTo(employeeSendTo);
            messages.setSendBy(employeeSendBy);
            messages.setEdited_at(LocalDateTime.now());
            messages.setCreated_at(LocalDateTime.now());

            em.getTransaction().begin();
            em.persist(messages);
            em.getTransaction().commit();
        }

        List<Messages> messages = em.createQuery("select m from Messages m where (m.sendTo.id = :employeeId and m.sendBy.id = :sessionUserId) " +
                        "or (m.sendTo.id = :sessionUserId and m.sendBy.id = :employeeId) order by m.edited_at", Messages.class)
                .setParameter("employeeId", employeeId).setParameter("sessionUserId", sessionUserId).getResultList();

        req.setAttribute("messages", messages);
        /*req.setAttribute("messagesSendBy", messagesSendBy);*/
        req.setAttribute("employeeSendTo", employeeSendTo);
        req.setAttribute("employeeSendBy", employeeSendBy);

        em.close();

        req.getRequestDispatcher("Conversation/partialMess.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession();
        Integer sessionUserId = (Integer) session.getAttribute("sessionUserId");
        if (sessionUserId == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        try {
            int employeeId = Integer.parseInt(req.getParameter("employeeId"));
            Employees employeeSendTo = em.find(Employees.class, employeeId);
            Employees employeeSendBy = em.find(Employees.class, sessionUserId);

            String messageContent = req.getParameter("message");
            Part filePart = req.getPart("fichierLink");

            boolean hasFile = filePart != null && filePart.getSize() > 0;

            if (hasFile || (messageContent != null && !messageContent.isEmpty())) {
                em.getTransaction().begin();
                Fichier fichierEntity = null;

                if (hasFile) {
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String uniqueName = System.currentTimeMillis() + "_" + fileName;

                    String appPath = "/fichierTatoTalk";
                    File uploadDir = new File(appPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    filePart.write(appPath + File.separator + uniqueName);
                    System.out.println("Sauvegardé dans : " + appPath + File.separator + uniqueName);

                    fichierEntity = new Fichier();
                    fichierEntity.setFile_name(fileName);
                    fichierEntity.setStockage_url("mess?fileName=" + uniqueName);
                    fichierEntity.setUploaded_at(LocalDateTime.now());

                    em.persist(fichierEntity);
                }
                if(messageContent != null  && !messageContent.isEmpty()){
                    Messages messages = new Messages();
                    messages.setMessage_content(messageContent);
                    messages.setSendTo(employeeSendTo);
                    messages.setSendBy(employeeSendBy);
                    messages.setEdited_at(LocalDateTime.now());
                    messages.setCreated_at(LocalDateTime.now());

                    if (fichierEntity != null) {
                        messages.setFichier(fichierEntity);
                    }
                    em.persist(messages);
                }
                em.getTransaction().commit();
            }

            List<Messages> messagesList = em.createQuery("select m from Messages m where (m.sendTo.id = :employeeId and m.sendBy.id = :sessionUserId) " +
                            "or (m.sendTo.id = :sessionUserId and m.sendBy.id = :employeeId) order by m.edited_at", Messages.class)
                    .setParameter("employeeId", employeeId).setParameter("sessionUserId", sessionUserId).getResultList();

            req.setAttribute("messages", messagesList);
            req.setAttribute("employeeSendTo", employeeSendTo);
            req.setAttribute("employeeSendBy", employeeSendBy);

            req.getRequestDispatcher("Conversation/partialMess.jsp").forward(req, resp);

        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
