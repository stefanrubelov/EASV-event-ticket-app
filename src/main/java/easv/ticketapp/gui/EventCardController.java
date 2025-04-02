package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.security.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class EventCardController {
    @FXML
    public Button editBtn;
    @FXML
    public Button deleteBtn;
    @FXML
    private Label nameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label descriptionLabel;

    @FXML
    private HBox actionsContainer;

    private Event event;
    private EventController eventController;

    @FXML
    public void initialize() {
        if (Auth.check() && Auth.getUser().isAdmin()) {
            editBtn.setVisible(false);
        }
    }

    public void setEvent(Event event) {
        this.event = event;
        nameLabel.setText(event.getName());
        dateLabel.setText(event.getDate().toString());
        locationLabel.setText(event.getLocation());
        descriptionLabel.setText(event.getDescription());
    }

    public void setEventController(EventController eventController) {
        this.eventController = eventController;

        deleteBtn.setOnAction(e -> {
            if (event != null) {
                confirmDelete();
            } else {
                System.out.println("Error: Event is null, cannot delete.");
            }
        });
    }

    private void confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Event");
        alert.setHeaderText("Are you sure you want to delete this event?");
        alert.setContentText(event.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            eventController.deleteEvent(event);
        }
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        try {
            FXMLLoader loader = PageManager.editEventView(event);

            EditEventController editEventController = loader.getController();

            if (editEventController != null) {
                editEventController.setEditEvent(this.event);
            } else {
                System.out.println("Error: EditEventController is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Could not load the edit event view.");
        }
    }
}
