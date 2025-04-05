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

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class EmailClient {
    private final MailjetClient client;
    private static final String FROM_EMAIL = Env.get("ADMIN_EMAIL");
    private static final String FROM_NAME = "EASV Admin";

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

    // New method for sending an email with attachments
    public MailjetResponse sendSimpleEmailWithAttachments(
            String toEmail, String toName,
            String subject, String textContent,
            String htmlContent, List<File> attachments) throws MailjetException {

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

        if (attachments != null && !attachments.isEmpty()) {
            try {
                JSONArray attachmentArray = prepareAttachments(attachments);
                message.put(Emailv31.Message.ATTACHMENTS, attachmentArray);
            } catch (Exception e) {
                throw new MailjetException("Failed to encode attachments", e);
            }
        }

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray().put(message));

        return client.post(request);
    }

    // New method for sending a template-based email with attachments
    public MailjetResponse sendTemplateEmailWithAttachments(String toEmail, String toName,
                                                            String subject, int templateId,
                                                            JSONObject variables, List<File> attachments) throws MailjetException {

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

        if (attachments != null && !attachments.isEmpty()) {
            try {
                JSONArray attachmentArray = prepareAttachments(attachments);
                message.put(Emailv31.Message.ATTACHMENTS, attachmentArray);
            } catch (Exception e) {
                throw new MailjetException("Failed to encode attachments", e);
            }
        }

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray().put(message));

        return client.post(request);
    }

    private JSONArray prepareAttachments(List<File> attachments) throws Exception {
        JSONArray attachmentsArray = new JSONArray();
        for (File file : attachments) {
            if (!file.exists()) {
                System.out.println("File does not exist: " + file.getAbsolutePath());
                continue;
            }
            if (!file.canRead()) {
                System.out.println("Cannot read file: " + file.getAbsolutePath());
                continue;
            }

            byte[] fileData = Files.readAllBytes(file.toPath());
            System.out.println("File size: " + fileData.length + " bytes for " + file.getName());

            String fileBase64 = java.util.Base64.getEncoder().encodeToString(fileData);
            System.out.println("Base64 length: " + fileBase64.length() + " for " + file.getName());

            JSONObject attachment = new JSONObject();
            attachment.put("ContentType", determineMimeType(file.getName())); // See method below
            attachment.put("Filename", file.getName());
            attachment.put("Base64Content", fileBase64);

            attachmentsArray.put(attachment);
        }
        System.out.println("Total attachments prepared: " + attachmentsArray.length());
        return attachmentsArray;
    }

    // Helper method to determine MIME type based on file extension
    private String determineMimeType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            // Add more types as needed
            default:
                return "application/octet-stream";
        }
    }

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
