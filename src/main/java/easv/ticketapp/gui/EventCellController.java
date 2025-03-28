package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class EventCellController {
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

    private Event event;
    private EventController eventController;

    public void setEvent(Event event) {
        this.event = event;
        nameLabel.setText(event.getName());
        dateLabel.setText(event.getStart_date().toString());
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
}