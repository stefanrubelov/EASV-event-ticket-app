package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    }

    public void handlePrintTicketAction(ActionEvent actionEvent) {

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
