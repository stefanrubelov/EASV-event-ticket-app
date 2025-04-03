package easv.ticketapp.bll.Email;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import easv.ticketapp.utils.Env;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A client for sending emails via the Mailjet API v3.1.
 */
public class EmailClient {
    private final MailjetClient client;
    private static final String FROM_EMAIL = Env.get("ADMIN_EMAIL");
    private static final String FROM_NAME = "EASV Admin";

    /**
     * Creates a new EmailClient with the provided API key and secret.
     */
    public EmailClient() {
        String apiKey = Env.get("MJ_APIKEY_PUBLIC");
        String apiSecretKey = Env.get("MJ_APIKEY_PRIVATE");

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecretKey)
                .build();

        this.client = new MailjetClient(options);
    }

    /**
     * Sends a simple email with basic parameters.
     *
     * @param toEmail     Recipient email address
     * @param toName      Recipient name
     * @param subject     Email subject
     * @param textContent Plain text content of the email
     * @param htmlContent HTML content of the email (can be null if only sending text)
     * @return MailjetResponse from the API
     * @throws MailjetException if the request fails
     */
    public MailjetResponse sendSimpleEmail(
            String toEmail, String toName,
            String subject, String textContent,
            String htmlContent)
            throws MailjetException {

        JSONObject message = new JSONObject();
        message.put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", FROM_EMAIL)
                .put("Name", FROM_NAME));

        message.put(Emailv31.Message.TO, new JSONArray()
                .put(new JSONObject()
                        .put("Email", toEmail)
                        .put("Name", toName)));

        message.put(Emailv31.Message.SUBJECT, subject);
        message.put(Emailv31.Message.TEXTPART, textContent);

        if (htmlContent != null && !htmlContent.isEmpty()) {
            message.put(Emailv31.Message.HTMLPART, htmlContent);
        }

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray().put(message));

        return client.post(request);
    }

    /**
     * Sends an advanced email with additional parameters like CC, BCC, template ID, etc.
     *
     * @param emailData A JSONObject containing all the email data as per Mailjet API specs
     * @return MailjetResponse from the API
     * @throws MailjetException if the request fails
     */
    public MailjetResponse sendAdvancedEmail(JSONObject emailData)
            throws MailjetException {

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray().put(emailData));

        return client.post(request);
    }

    /**
     * Sends a template-based email using Mailjet templates.
     *
     * @param toEmail    Recipient email address
     * @param toName     Recipient name
     * @param subject    Email subject
     * @param templateId Mailjet template ID to use
     * @param variables  JSONObject containing variables to inject into the template
     * @return MailjetResponse from the API
     * @throws MailjetException if the request fails
     */
    public MailjetResponse sendTemplateEmail(String toEmail, String toName,
                                             String subject, int templateId,
                                             JSONObject variables)
            throws MailjetException {

        JSONObject message = new JSONObject();
        message.put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", FROM_EMAIL)
                .put("Name", FROM_NAME));

        message.put(Emailv31.Message.TO, new JSONArray()
                .put(new JSONObject()
                        .put("Email", toEmail)
                        .put("Name", toName)));

        message.put(Emailv31.Message.SUBJECT, subject);
        message.put(Emailv31.Message.TEMPLATEID, templateId);
        message.put(Emailv31.Message.TEMPLATELANGUAGE, true);

        if (variables != null && !variables.isEmpty()) {
            message.put(Emailv31.Message.VARIABLES, variables);
        }

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray().put(message));

        return client.post(request);
    }

    /**
     * Processes the API response and returns a human-readable status message.
     *
     * @param response The MailjetResponse from an API call
     * @return A string describing the result of the API call
     */
    public static String processResponse(MailjetResponse response) {
        int statusCode = response.getStatus();

        if (statusCode == 200 || statusCode == 201) {
            return "Email sent successfully! Status: " + statusCode;
        } else {
            return "Failed to send email. Status: " + statusCode +
                    ", Error: " + response.getData().toString();
        }
    }
}