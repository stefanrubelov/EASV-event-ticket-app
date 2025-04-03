package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.utils.PdfExporter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PurchaseTicketController {
    private Ticket ticket;
    private Event event;

    @FXML
    private Label eventNameLbl;

    @FXML
    private Label ticketDescriptionLbl;

    @FXML
    private TextField recipientEmail;
    @FXML
    private TextField recipientName;
    @FXML
    private TextField amount;

    @FXML
    private VBox root;

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        ticketDescriptionLbl.setText(ticket.getDescription());
    }

    public void setEvent(Event event) {
        this.event = event;
        eventNameLbl.setText(event.getName());
    }

    public void handleEmailTicketAction(ActionEvent actionEvent) {
    }

    public void handlePreviewTicketAction(ActionEvent actionEvent) {
        Ticket previewTicket = this.ticket;
        previewTicket.setEvent(this.event);
        PageManager.ticketPreview(actionEvent, previewTicket);
    }

    public void handlePrintTicketAction(ActionEvent actionEvent) {
//        Event selectedEvent = eventBox.getValue();
//        TicketType selectedTicketType = ticketTypeBox.getValue();
//        String description = descriptionTextArea.getText();
//        double ticketPrice = Double.parseDouble(price.getText());
//
//        Ticket ticket = new Ticket(selectedEvent.getId(), ticketPrice, description, selectedTicketType, selectedEvent);
//        ticketManager.addTicket(ticket);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/easv/ticketapp/ticket-preview.fxml"));
            Parent root = loader.load();
            TicketPreviewController ticketPreviewController = loader.getController();

            ticketPreviewController.updatePreview(ticket);
            Scene previewScene = new Scene(root);

            Stage stage = (Stage) root.getScene().getWindow();
            String ticketName = event.getName() + "_" + ticket.getTicketType().getName();
            PdfExporter.exportSceneToPDF(previewScene, stage, ticketName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading ticket preview scene.");
        }
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
            e.printStackTrace();
            System.out.println("Error: Could not load the event tickets view.");
        }
    }
}
