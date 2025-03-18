package easv.ticketapp;

import easv.ticketapp.be.Event;
import easv.ticketapp.bll.EventManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

public class EventController {
    private final EventManager eventManager = new EventManager();

    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, String> nameColumn;
    @FXML
    private TableColumn<Event, Date> dateColumn;
    @FXML
    private TableColumn<Event, String> locationColumn;
    @FXML
    private TableColumn<Event, String> descriptionColumn;

    private final ObservableList<Event> eventList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        fetchEvents();
        setupTableColumns();
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        eventTable.setItems(eventList);
    }

    private void fetchEvents() {
        List<easv.ticketapp.be.Event> events = eventManager.getAllEvents();

        eventList.clear();
        eventList.setAll(events);
        eventTable.setItems(eventList);

        System.out.println("Fetched events: " + events.size());

        if (events.isEmpty()) {
            System.out.println("No events found in the database!");
        }
    }

}
