package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.bll.TicketManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class EventTicketsController {
    @FXML
    private ListView<Ticket> ticketListView;
    private TicketManager ticketManager;

    public EventTicketsController() {
        this.ticketManager = new TicketManager();
    }

    public void setEvent(Event event) {
        if (event != null) {
            loadTicketsForEvent(event);
        }
    }

    private void loadTicketsForEvent(Event event) {
        List<Ticket> tickets = ticketManager.getTicketsByEvent(event.getId()); // Buscar os bilhetes do evento
        ticketListView.getItems().setAll(tickets);
    }
}
