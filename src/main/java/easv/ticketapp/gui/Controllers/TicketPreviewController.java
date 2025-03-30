package easv.ticketapp.gui.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class TicketPreviewController {
    @FXML private Label eventNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label locationLabel;
    @FXML private Label priceLabel;
    @FXML private Label seatNumberLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label perksLabel;
    @FXML
    private Pane rootPane;

    public void setBackgroundImage(String imagePath) {
        rootPane.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover; -fx-background-position: center;");
    }
    public void updatePreview(String eventName, String date, String location, Double price, String seatNumber, String description, String perks) {
        eventNameLabel.setText(eventName);
        dateLabel.setText(date);
        locationLabel.setText(location);
        priceLabel.setText(String.valueOf(price));
        seatNumberLabel.setText(seatNumber);
        descriptionLabel.setText(description);
        perksLabel.setText(perks);
    }
}
