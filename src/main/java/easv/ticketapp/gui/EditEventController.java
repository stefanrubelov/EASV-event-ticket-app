package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.dal.db.EventRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;

public class EditEventController {

    EventRepository eventRepository = new EventRepository();

    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DateTimePicker dateTimePicker;

    private Event event;

    public void setEditEvent(Event event) {
        if (event == null) {
            System.out.println("Error: Event is null!");
            return;
        }

        this.event = event;

        nameField.setText(event.getName());
        locationField.setText(event.getLocation());
        descriptionTextArea.setText(event.getDescription());

        if (event.getDate() != null) {
            dateTimePicker.setDateTimeValue(event.getDate());
        }
    }

    @FXML
    public void saveChanges(ActionEvent actionEvent) {
        String updatedName = nameField.getText();
        String updatedLocation = locationField.getText();
        String updatedDescription = descriptionTextArea.getText();

        LocalDateTime selectedDateTime = dateTimePicker.getDateTimeValue();
        if (selectedDateTime == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please select a valid date.");
            return;
        }

        event.setName(updatedName);
        event.setDate(selectedDateTime);
        event.setLocation(updatedLocation);
        event.setDescription(updatedDescription);

        boolean isUpdated = updateEventInDatabase(event);

        if (isUpdated) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Event updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update event.");
        }
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

    private boolean updateEventInDatabase(Event event) {
        try {
            eventRepository.update(event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
