package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.User;
import easv.ticketapp.dal.db.EventRepository;
import easv.ticketapp.dal.db.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignCoordinatorController {
    @FXML
    private ListView<Event> eventListView;

    @FXML
    private ListView<User> coordinatorListView;

    @FXML
    private Label errorLbl;

    private User user;
    private final EventRepository eventRepository = new EventRepository();
    private final UserRepository userRepository= new UserRepository();


    public void initialize() {
       loadEvents();
       loadUsers();
    }

    public void loadEvents() {
        List<Event> events = eventRepository.getAll();
        ObservableList<Event> observableList = FXCollections.observableList(events);
        eventListView.setItems(observableList);
    }

    public void loadUsers() {
        List<User> users = userRepository.getAllCoordinators();
        ObservableList<User> observableList = FXCollections.observableList(users);
        coordinatorListView.setItems(observableList);
    }

    @FXML
    private void onSaveClicked() {
        Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();
        User selectedCoordinator = coordinatorListView.getSelectionModel().getSelectedItem();

        if (selectedEvent == null || selectedCoordinator == null) {
            errorLbl.setText("Please select both an event and a coordinator.");
            return;
        }

        if (eventRepository.isUserAssignedToEvent(selectedEvent, selectedCoordinator)) {
            showAlert(Alert.AlertType.WARNING, "Already Assigned", "This user is already assigned to the selected event.");
            return;
        }

        eventRepository.addCoordinator(selectedEvent, selectedCoordinator);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Coordinator assigned successfully!");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void goBack(ActionEvent actionEvent) {
        PageManager.coordinatorsView(actionEvent);

    }

}
