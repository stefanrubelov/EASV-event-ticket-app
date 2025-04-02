package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.dal.db.EventRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;

public class AddEventController {
    private EventRepository eventRepository = new EventRepository();
    private Event event;

    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DateTimePicker dateTimePicker;
    @FXML
    private Button createEventBtn;
    @FXML
    private Label errorLbl;

    @FXML
    public void onCreateEventAction(ActionEvent actionEvent) {
        resetFieldStyles();

        if (validateFields()) {
            event = new Event();

            String name = nameField.getText();
            String location = locationField.getText();
            String description = descriptionTextArea.getText();
            LocalDateTime selectedDateTime = dateTimePicker.getDateTimeValue();

            event.setName(name);
            event.setDate(selectedDateTime);
            event.setLocation(location);
            event.setDescription(description);

            boolean isUpdated = updateEventInDatabase(event);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Event created successfully!");
                PageManager.coordinatorsView(actionEvent);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create event.");
            }
        }
    }

    /**
     * Validates all input fields for the event form.
     *
     * @return true if validation passes, false otherwise.
     */
    private boolean validateFields() {
        boolean isValid = true;

        String name = nameField.getText();
        String location = locationField.getText();
        String description = descriptionTextArea.getText();
        LocalDateTime selectedDateTime = dateTimePicker.getDateTimeValue();

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            errorLbl.setText("Event name is required");
            nameField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        // Validate location
        if (location == null || location.trim().isEmpty()) {
            if (!isValid) {
                errorLbl.setText("Event name and location are required");
            } else {
                errorLbl.setText("Location is required");
            }
            locationField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        // Validate date/time
        if (selectedDateTime == null) {
            if (!isValid) {
                errorLbl.setText(errorLbl.getText() + " and date/time must be selected");
            } else {
                errorLbl.setText("Please select a valid date and time");
            }
            dateTimePicker.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            // Check if date is in the future
            if (selectedDateTime.isBefore(LocalDateTime.now())) {
                if (!isValid) {
                    errorLbl.setText(errorLbl.getText() + " and date/time must be in the future");
                } else {
                    errorLbl.setText("Event date must be in the future");
                }
                dateTimePicker.setStyle("-fx-border-color: red;");
                isValid = false;
            }
        }

        // Validate description (optional but should have a minimum length if provided)
        if (description != null && !description.trim().isEmpty() && description.trim().length() < 10) {
            if (!isValid) {
                errorLbl.setText(errorLbl.getText() + " and description should be at least 10 characters");
            } else {
                errorLbl.setText("Description should be at least 10 characters if provided");
            }
            descriptionTextArea.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Resets the style of all input fields.
     */
    private void resetFieldStyles() {
        nameField.setStyle("");
        locationField.setStyle("");
        descriptionTextArea.setStyle("");
        dateTimePicker.setStyle("");
        if (errorLbl != null) {
            errorLbl.setText("");
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

    @FXML
    private void onGoBackAction(ActionEvent actionEvent) {
        PageManager.coordinatorsView(actionEvent);
    }
}