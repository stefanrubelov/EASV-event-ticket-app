package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.dal.db.EventRepository;
import easv.ticketapp.dal.db.QueryBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditEventController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField dateField;  // Keep as TextField
    @FXML
    private TextField locationField;
    @FXML
    private TextArea descriptionTextArea;

    private Event event;

    EventRepository eventRepository = new EventRepository();

    public void setEditEvent(Event event) {
        if (event == null) {
            System.out.println("Error: Event is null!");
            return;
        }

        descriptionTextArea.setWrapText(true);

        this.event = event;
        nameField.setText(event.getName());
        dateField.setText(event.getDate().toString());
        locationField.setText(event.getLocation());
        descriptionTextArea.setText(event.getDescription());
    }

    @FXML
    public void goBack(ActionEvent event) {
        PageManager.coordinatorsView(event);
    }

    @FXML
    public void saveChanges(ActionEvent actionEvent) {
        String updatedName = nameField.getText();
        String updatedDate = dateField.getText();
        String updatedLocation = locationField.getText();
        String updatedDescription = descriptionTextArea.getText();

        Date parsedDate = null;
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Ensure the correct format with time
        SimpleDateFormat sdfDateOnly = new SimpleDateFormat("yyyy-MM-dd"); // Date format only (without time)

        try {
            parsedDate = sdfDate.parse(updatedDate);
        } catch (ParseException e) {
            try {
                parsedDate = sdfDateOnly.parse(updatedDate);
                String updatedDateWithTime = updatedDate + " 00:00:00";
                parsedDate = sdfDate.parse(updatedDateWithTime);
            } catch (ParseException ex) {
                showAlert(AlertType.ERROR, "Invalid Date Format", "Please enter a valid date in the format yyyy-MM-dd or yyyy-MM-dd HH:mm:ss.");
                return;
            }
        }

        java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
        event.setName(updatedName);
        event.setDate(sqlDate);

        event.setLocation(updatedLocation);
        event.setDescription(updatedDescription);

        boolean isUpdated = updateEventInDatabase(event);

        if (isUpdated) {
            showAlert(AlertType.INFORMATION, "Success", "Event updated successfully!");
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to update event.");
        }
    }




    // Helper method to show alert messages
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to update the event in the database (Assuming you have a method like this)
    private boolean updateEventInDatabase(Event event) {
        try {
            // Call your database service here to save the updated event
            eventRepository.updateEvent(event);
            return true; // Event updated successfully
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Error during the update
        }
    }
}
