package easv.ticketapp.gui;

import easv.ticketapp.be.ticket.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class TicketCardController {
    private EventTicketsController eventTicketController;
    private Ticket ticket;

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
}
