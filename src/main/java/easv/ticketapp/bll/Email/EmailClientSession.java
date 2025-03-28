package easv.ticketapp.bll.Email;

import easv.ticketapp.utils.Env;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

import java.util.Properties;

public class EmailClientSession {
    public static EmailClientSession INSTANCE;

    public static EmailClientSession getInstance() {
        System.setProperty("java.util.logging.config.file", "logging.properties");
        if (INSTANCE == null) {
            return new EmailClientSession();
        } else {
            return INSTANCE;
        }
    }

    public Session session() {
        final String username = Env.get("MAIL_USER");
        final String password = Env.get("MAIL_PASSWORD");

        if (username == null || password == null) {
            throw new IllegalArgumentException("Missing Mail client username and password");
        }

        String host = Env.get("MAIL_HOST");
        Integer port = Integer.valueOf(Env.get("MAIL_PORT"));

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        return session;
    }
}