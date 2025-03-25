package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

    public void setEvent(Event event) {
        // Set the data from the Event object to the UI elements
        nameLabel.setText(event.getName());
        dateLabel.setText(event.getDate().toString()); // Format the date as needed
        locationLabel.setText(event.getLocation());
        descriptionLabel.setText(event.getDescription());
    }
}
