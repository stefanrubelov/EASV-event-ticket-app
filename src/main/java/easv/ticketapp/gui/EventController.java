package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.bll.EventManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class EventController {
    private final EventManager eventManager = new EventManager();
    private static final int ITEMS_PER_PAGE = 15;
    private List<Event> allEvents;

    @FXML
    private VBox eventContainer;
    @FXML
    private Pagination pagination;
    @FXML
    private Button createBtn;

    @FXML
    public void initialize() {
        allEvents = eventManager.getAllEvents();
        setupPagination();
    }

    private void setupPagination() {
        int pageCount = (int) Math.ceil((double) allEvents.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> loadPage(newIndex.intValue()));
        loadPage(0);
    }

    private void loadPage(int pageIndex) {
        eventContainer.getChildren().clear();

        int start = pageIndex * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, allEvents.size());

        for (int i = start; i < end; i++) {
            Event event = allEvents.get(i);
            try {
                FXMLLoader childLoader = new FXMLLoader(getClass().getResource("/easv/ticketapp/event-card.fxml"));
                HBox eventCell = childLoader.load();

                EventCellController eventCellController = childLoader.getController();
                eventCellController.setEvent(event);
                eventCellController.setEventController(this);

                eventContainer.getChildren().add(eventCell);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading event-card.fxml");
            }
        }
    }

    public void deleteEvent(Event event) {
        allEvents.remove(event);
        eventManager.deleteEvent(event);
        setupPagination();
        loadPage(pagination.getCurrentPageIndex());
    }

    @FXML
    public void createEvent(javafx.event.ActionEvent actionEvent) {
        PageManager.addEventView(actionEvent);
    }
}