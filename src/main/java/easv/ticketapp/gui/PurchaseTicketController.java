package easv.ticketapp.gui;

import com.mailjet.client.MailjetResponse;
import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.bll.Email.EmailClient;
import easv.ticketapp.bll.PurchaseTicketService;
import easv.ticketapp.utils.PdfExporter;
import easv.ticketapp.utils.UuidGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurchaseTicketController {
    final private Logger logger = Logger.getAnonymousLogger();

    private Ticket ticket;
    private Event event;

    @FXML
    private Label eventNameLbl;
    @FXML
    private Label ticketDescriptionLbl;
    @FXML
    private TextField recipientEmailInput;
    @FXML
    private Label errorRecipientEmailLbl;
    @FXML
    private TextField recipientNameInput;
    @FXML
    private Label errorRecipientNameLbl;
    @FXML
    private Spinner<Integer> amountOfTicketsSpinner;
    @FXML
    private VBox root;

    public void initialize() {
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12);
        spinnerValueFactory.setValue(1);
        amountOfTicketsSpinner.setValueFactory(spinnerValueFactory);
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        ticketDescriptionLbl.setText(ticket.getDescription());
    }

    public void setEvent(Event event) {
        this.event = event;
        eventNameLbl.setText(event.getName());
    }

    public void handlePrintTicketAction(ActionEvent actionEvent) {
        if (!validateEmailField() && !validateNameField()) {
            return;
        }

        try {
            Stage stage = generateStage();
            String ticketName = generateTicketName();
            Integer amountOfTickets = amountOfTicketsSpinner.getValue();
            String recipientName = recipientNameInput.getText();
            String ticketCode;
            List<Scene> scenes = new ArrayList<>();
            for (int i = 0; i < amountOfTickets; i++) {
                ticketCode = UuidGenerator.generate();
                Scene previewScene = null;
                previewScene = generatePreviewScene(ticketCode, recipientName);

                scenes.add(previewScene);
            }
            PdfExporter.exportScenesToMultiplePDFsWithDialog(scenes, stage, ticketName, 1)
                    .thenAccept(success -> {
                        if (success) {

                        } else {
                        }
                    });

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void handleEmailTicketAction(ActionEvent actionEvent) {
        if (!validateEmailField() && !validateNameField()) {
            return;
        }

        Integer amountOfTickets = amountOfTicketsSpinner.getValue();
        try {
            Stage stage = generateStage();
            String ticketName = generateTicketName();
            String recipientName = recipientNameInput.getText();

            String ticketCode;
            List<Scene> scenes = new ArrayList<>();
            for (int i = 0; i < amountOfTickets; i++) {
                ticketCode = UuidGenerator.generate();
                Scene previewScene = null;
                previewScene = generatePreviewScene(ticketCode, recipientName);

                scenes.add(previewScene);
            }
            CompletableFuture<List<String>> futureFilePaths = PdfExporter.exportScenesToMultiplePDFs(
                    scenes, stage, ticketName, 1);

            futureFilePaths.thenAccept(filePaths -> {
                List<File> attachments = PurchaseTicketService.convertPathsToFiles(filePaths);

                try {
                    EmailClient emailClient = new EmailClient();
                    MailjetResponse response = emailClient.sendSimpleEmailWithAttachments(
                            recipientEmailInput.getText(),
                            recipientNameInput.getText(),
                            "Your tickets are here",
                            "The tickets for your event are now available.",
                            "<b>Tickets available to download</b>",
                            attachments);

                    String status = EmailClient.processResponse(response);
//                    System.out.println(status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void handlePreviewTicketAction(ActionEvent actionEvent) {
        Ticket previewTicket = this.ticket;
        previewTicket.setEvent(this.event);
        PageManager.ticketPreview(actionEvent, previewTicket);
    }

    public void onBackButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = PageManager.eventTicketsiew(actionEvent);

            EventTicketsController eventTicketsController = loader.getController();
            if (eventTicketsController != null) {
                eventTicketsController.setEvent(this.event);
            } else {
                System.out.println("Error: EventTicketsController is null");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private Stage generateStage() {
        Stage stage = (Stage) root.getScene().getWindow();

        return stage;
    }

    private Scene generatePreviewScene(String code, String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/easv/ticketapp/ticket-preview.fxml"));
        Parent root = loader.load();
        TicketPreviewController ticketPreviewController = loader.getController();
        ticketPreviewController.setBarcode(code);
        ticketPreviewController.setCustomerName(name);
        ticketPreviewController.updatePreview(ticket);
        Scene previewScene = new Scene(root);
        return previewScene;
    }

    private String generateTicketName() {
        return event.getName() + "_" + ticket.getTicketType().getName();
    }

    private boolean validateEmailField() {
        String email = recipientEmailInput.getText();
        errorRecipientEmailLbl.setText("");

        if (email == null || email.trim().isEmpty()) {
            errorRecipientEmailLbl.setText("Email is required");
            recipientEmailInput.setStyle("-fx-border-color: red;");
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            errorRecipientEmailLbl.setText("Please enter a valid email address");
            recipientEmailInput.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    private boolean validateNameField() {
        String name = recipientNameInput.getText();
        errorRecipientNameLbl.setText("");

        if (name == null || name.trim().isEmpty()) {
            errorRecipientNameLbl.setText("Name is required");
            recipientNameInput.setStyle("-fx-border-color: red;");
            return false;
        }
        if (name.length() < 3) {
            errorRecipientNameLbl.setText("Name must be at least 3 characters");
            recipientNameInput.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }
}
