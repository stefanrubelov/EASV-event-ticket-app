package easv.ticketapp.bll.Email;

import easv.ticketapp.utils.Env;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailService {
    private String from;
    private String to;
    private String subject;
    private String body;
    private boolean isHtml;
    private final Session session;

    public EmailService() {
        this.session = EmailClientSession.getInstance().session();
        this.from = Env.get("ADMIN_EMAIL");
    }

    public EmailService to(String to) {
        this.to = to;
        return this;
    }

    public EmailService from(String from) {
        this.from = from;
        return this;
    }

    public EmailService subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailService body(String body) {
        this.body = body;
        return this;
    }

    public EmailService html(boolean isHtml) {
        this.isHtml = isHtml;
        return this;
    }

    public boolean send() {
        if (to == null || subject == null || body == null) {
            throw new IllegalArgumentException("Missing required email fields.");
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            if (isHtml) {
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(body, "text/html");

                MimeMultipart multipart = new MimeMultipart("alternative");
                multipart.addBodyPart(htmlPart);

                message.setContent(multipart);
            } else {
                message.setText(body);
            }

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
