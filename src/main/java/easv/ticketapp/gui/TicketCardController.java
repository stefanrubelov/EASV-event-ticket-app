package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class TicketCardController {
    private EventTicketsController eventTicketController;
    private Ticket ticket;
    private Event event;

    @FXML
    private Label priceLbl;
    @FXML
    private Label descriptionLbl;
    @FXML
    private Label ticketTypeLbl;


    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        descriptionLbl.setText(ticket.getDescription());
        priceLbl.setText(ticket.getPrice() + " dkk");
        ticketTypeLbl.setText(ticket.getTicketType().getName());
    }

    public void setController(EventTicketsController controller) {
        this.eventTicketController = controller;
    }

    public void handleDeleteAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete ticket");
        alert.setHeaderText("Are you sure you want to delete this ticket?");
        alert.setContentText(ticket.getTicketType().getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            eventTicketController.deleteTicket(ticket);
        }
    }

    public void handlePurchaseAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = PageManager.purchaseTicketView(actionEvent);

            PurchaseTicketController purchaseTicketController = loader.getController();
            if (purchaseTicketController != null) {
                purchaseTicketController.setTicket(this.ticket);
                purchaseTicketController.setEvent(this.ticket.getEvent());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Could not load the purchase ticket view.");
        }
    }
    public void handlePreviewAction(ActionEvent actionEvent) {

        PageManager.ticketPreview(actionEvent, ticket);
    }
}
