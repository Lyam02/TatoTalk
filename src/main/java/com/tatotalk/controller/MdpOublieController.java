package com.tatotalk.controller;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

@WebServlet("/mdp-oublie")
public class MdpOublieController extends HttpServlet {

    // Méthode GET - Afficher le formulaire ou récupérer des données
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Spécifiez le chemin vers la "vue" (le fichier JSP mdpOublie)
        String jspPagePath = "/connexion/mdpOublie.jsp";

        // 2. Obtenez le "dispatcher" pour cette page
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspPagePath);

        // 3. Transférez la requête (request) et la réponse (response) au JSP
        // Le serveur fait le transfert en interne, l'URL ne change pas
        dispatcher.forward(request, response);
    }

    // Méthode POST - Traiter les données du formulaire
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        Email emailLogic = new Email(emf);
        EmailService emailService = new EmailService();
        // Récupérer les paramètres du formulaire
        String email = request.getParameter("email");

        // --- ÉTAPE 2 : Vérifier si l'e-mail existe (code select sql BDD) ---
         boolean userExiste = emailLogic.emailExist(email);

        // --- ÉTAPE 3 : Si oui, générer le token et envoyer l'e-mail ---
        if (userExiste) {
            try {
                // ... (votre logique pour générer le token) ...
                String token = UUID.randomUUID().toString();

                // ... (votre logique pour sauvegarder le token en BDD) ...

                // ... (votre logique pour construire le lien) ...
                String resetLink = request.getScheme() + "://" +    // (1)
                        request.getServerName() + ":" +             // (2)
                        request.getServerPort() +                   // (3)
                        request.getContextPath() +                  // (4)
                        "/nouveau-mdp?token=" + token;              // (5)

                // ... (votre logique pour envoyer l'e-mail)
                // fonction envoie eMail
                try {
                    String sujet = "TatoTalk - Réinitialisez votre mot de passe";
                    String corps = "Bonjour,\n\n" +
                            "Pour réinitialiser votre mot de passe, cliquez sur ce lien :\n" +
                            resetLink + "\n\n" +
                            "Ce lien expirera dans 30 minutes.";

                    // Appel direct à votre nouvelle classe !
                    EmailService.sendEmail(email, sujet, corps);

                } catch (Exception e) {
                    System.err.println("Erreur fatale lors de l'envoi de l'e-mail : " + e.getMessage());
                    // (Même si l'e-mail échoue, on continue vers la page de succès
                    // pour ne pas révéler d'infos à l'utilisateur)
                }

                System.out.println("Lien de réinitialisation : " + resetLink);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // --- ÉTAPE 4 : Rediriger vers la page de succès ---
            // On utilise "sendRedirect" ici, car c'est une action terminée.
            // L'URL dans le navigateur du client va changer.
            response.sendRedirect(request.getContextPath() + "/connexion/connexion.jsp");

            System.out.println(email);
        }else {
            // --- L'UTILISATEUR N'EXISTE PAS ---

            // 1. Définir le message d'erreur
            request.setAttribute("errorMessage", "Cette adresse e-mail n'existe pas.");

            // 2. Transférer (forward) vers la page (identique)
            RequestDispatcher dispatcher = request.getRequestDispatcher("/connexion/mdpOublie.jsp");
            dispatcher.forward(request, response);
        }

    }

}

class Email{
    private EntityManagerFactory emf;
    public Email(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public boolean emailExist(String email) {
        boolean emailExist = false;

        try (EntityManager em = emf.createEntityManager()) {
            String sql = "SELECT COUNT(*) FROM Employees WHERE email = :email";
            Query query = em.createQuery(sql);
            query.setParameter("email", email);
            long count = (long) query.getSingleResult();
            emailExist = count > 0;
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return emailExist;
    }

}

class EmailService {

    // 1. L'adresse e-mail de votre compte Gmail que vous utilisez pour envoyer
    private static final String GMAIL_USERNAME = "yatogroupfr@gmail.com";
    // 2. Le "Mot de passe d'application" de 16 lettres généré par Google
    //    NE METTEZ PAS VOTRE VRAI MOT DE PASSE GMAIL ICI !
    private static final String GMAIL_APP_PASSWORD = "tmpf kjur zbts lkkn";

    /**
     * Envoie un e-mail.
     * @param recipientEmail L'adresse du destinataire (ex: l'utilisateur qui a oublié son MDP)
     * @param subject Le sujet de l'e-mail
     * @param body Le corps du message (texte simple)
     */
    public static void sendEmail(String recipientEmail, String subject, String body) {

        // 1. Définir les propriétés du serveur SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");         // Hôte SMTP de Gmail
        props.put("mail.smtp.port", "587");                   // Port pour TLS
        props.put("mail.smtp.auth", "true");                  // Authentification requise
        props.put("mail.smtp.starttls.enable", "true");       // Activer STARTTLS (connexion sécurisée)

        // 2. Créer la session avec l'authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Utilise les identifiants configurés ci-dessus
                return new PasswordAuthentication(GMAIL_USERNAME, GMAIL_APP_PASSWORD);
            }
        });

        try {
            // 3. Créer l'objet Message (l'e-mail)
            Message message = new MimeMessage(session);
            // Définir l'expéditeur
            message.setFrom(new InternetAddress(GMAIL_USERNAME));
            // Définir le destinataire
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            // Définir le sujet
            message.setSubject(subject);
            // Définir le corps du message
            message.setText(body);
            // 4. Envoyer l'e-mail
            Transport.send(message);
            System.out.println("EmailService: E-mail envoyé avec succès à " + recipientEmail);

        } catch (MessagingException e) {
            // Gérer les erreurs d'envoi
            System.err.println("EmailService: Erreur lors de l'envoi de l'e-mail.");
            e.printStackTrace();
            // Il est important de "lancer" l'erreur pour que le contrôleur sache que ça a échoué
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail", e);
        }
    }
}