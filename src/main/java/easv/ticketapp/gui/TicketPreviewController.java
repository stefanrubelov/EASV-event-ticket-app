package easv.ticketapp.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class TicketPreviewController {
    @FXML private Label eventNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label locationLabel;
    @FXML private Label priceLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label perksLabel;
    @FXML
    private Pane rootPane;

    public void setBackgroundImage(String imagePath) {
        rootPane.setStyle("-fx-background-image: url(");
    }
    public void updatePreview(String eventName, String date, String location, Double price, String description, String perks) {
        eventNameLabel.setText(eventName);
        dateLabel.setText(date);
        locationLabel.setText(location);
        priceLabel.setText(String.valueOf(price));
        descriptionLabel.setText(description);
        perksLabel.setText(perks);
    }
}
