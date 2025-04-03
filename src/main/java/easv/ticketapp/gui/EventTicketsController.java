package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.bll.TicketManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class EventTicketsController {
    private final TicketManager ticketManager = new TicketManager();
    private static final int ITEMS_PER_PAGE = 10;

    public VBox ticketsContainer;
    private List<Ticket> tickets;

    @FXML
    private Label eventNameLbl;
    @FXML
    private Label eventDescriptionLbl;
    @FXML
    private Label eventLocationLbl;
    @FXML
    private Label eventDateLbl;
    @FXML
    private Pagination pagination;

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        eventNameLbl.setText(event.getName());
        eventDescriptionLbl.setText(event.getDescription());
        eventLocationLbl.setText(event.getLocation());
        eventDateLbl.setText(event.getDateFormatted());
        tickets = ticketManager.getTicketsByEvent(event.getId());
        setupPagination();
    }

    private void setupPagination() {
        int pageCount = (int) Math.ceil((double) tickets.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> loadPage(newIndex.intValue()));
        loadPage(0);
    }

    private void loadPage(int pageIndex) {
        ticketsContainer.getChildren().clear();

        int start = pageIndex * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, tickets.size());

        for (int i = start; i < end; i++) {
            Ticket ticket = tickets.get(i);
            try {
                FXMLLoader childLoader = new FXMLLoader(getClass().getResource("/easv/ticketapp/ticket-card.fxml"));
                Pane userCard = childLoader.load();

                TicketCardController ticketCardController = childLoader.getController();
                ticketCardController.setTicket(ticket);
                ticketCardController.setController(this);

                ticketsContainer.getChildren().add(userCard);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading ticket-card.fxml");
            }
        }
    }

    public void handleCreateTicketBtn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = PageManager.addTicketView(actionEvent);

            TicketController ticketController = loader.getController();
            if (ticketController != null) {
                ticketController.setEvent(this.event);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Could not load the purchase ticket view.");
        }
    }

    public void deleteTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticketManager.deleteTicket(ticket);
        setupPagination();
        loadPage(pagination.getCurrentPageIndex());
    }
}
