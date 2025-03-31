package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.dal.db.EventRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;

public class AddEventController {

    EventRepository eventRepository = new EventRepository();

    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DateTimePicker dateTimePicker;

    @FXML
    private Button createEvent;

    private Event event;

    @FXML
    private void goBack(ActionEvent actionEvent) {
        PageManager.coordinatorsView(actionEvent);
    }


    @FXML
    public void createEvent(ActionEvent actionEvent) {

        event = new Event();

        String Name = nameField.getText();
        String Location = locationField.getText();
        String Description = descriptionTextArea.getText();

        LocalDateTime selectedDateTime = dateTimePicker.getDateTimeValue();
        if (selectedDateTime == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please select a valid date.");
            return;
        }

        event.setName(Name);
        event.setDate(selectedDateTime);
        event.setLocation(Location);
        event.setDescription(Description);

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

    private boolean updateEventInDatabase(Event event) {
        try {
            eventRepository.createEvent(event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
