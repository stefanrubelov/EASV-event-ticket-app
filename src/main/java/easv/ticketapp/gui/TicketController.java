package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;
import easv.ticketapp.bll.EventManager;
import easv.ticketapp.bll.TicketManager;
import easv.ticketapp.security.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TicketController implements Initializable {
    private final EventManager eventManager = new EventManager();
    private final TicketManager ticketManager = new TicketManager();
    private final TicketPreviewController ticketPreviewController = new TicketPreviewController();

    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ComboBox<Event> eventBox;
    @FXML
    private TextField price;
    @FXML
    private ComboBox<TicketType> ticketTypeBox;
    @FXML
    private Label errorEventLbl;
    @FXML
    private Label errorTicketTypeLbl;
    @FXML
    private Label errorPriceLbl;
    @FXML
    private Label errorDescriptionLbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Event> events;
        if (Auth.getUser().isAdmin()) {
            events = eventManager.getAllEvents();

        } else {
            events = eventManager.getAllEventsByUser(Auth.getUser());
        }
        List<TicketType> ticketTypes = ticketManager.getAllTicketTypes();
        eventBox.getItems().addAll(events);
        ticketTypeBox.getItems().addAll(ticketTypes);
    }

    @FXML
    void onPreview(ActionEvent actionEvent) {
        if (!validateForm()) return;

        Event selectedEvent = eventBox.getValue();
        TicketType selectedTicketType = ticketTypeBox.getValue();
        String description = descriptionTextArea.getText();
        double ticketPrice = Double.parseDouble(price.getText());

        Ticket ticket = new Ticket(selectedEvent.getId(), ticketPrice, description, selectedTicketType, selectedEvent);

        PageManager.ticketPreview(actionEvent, ticket);

    }

    @FXML
    void onSubmit(ActionEvent event) {
        try {
            if (!validateForm()) return;

            Event selectedEvent = eventBox.getValue();
            TicketType selectedTicketType = ticketTypeBox.getValue();
            String description = descriptionTextArea.getText();
            double ticketPrice = Double.parseDouble(price.getText());

            Ticket newTicket = new Ticket(selectedEvent.getId(), ticketPrice, description, selectedTicketType, selectedEvent);
            ticketManager.addTicket(newTicket);
            PageManager.coordinatorsView(event);
        } catch (NumberFormatException e) {
        } catch (Exception e) {
        }
    }

    public void setEvent(Event event) {
        eventBox.setValue(event);
    }

    private boolean validateEventField() {
        errorEventLbl.setText("");
        eventBox.setStyle("");

        if (eventBox.getValue() == null) {
            errorEventLbl.setText("Event is required");
            eventBox.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    private boolean validateTicketTypeField() {
        errorTicketTypeLbl.setText("");
        ticketTypeBox.setStyle("");

        if (ticketTypeBox.getValue() == null) {
            errorTicketTypeLbl.setText("Ticket type is required");
            ticketTypeBox.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    private boolean validatePriceField() {
        errorPriceLbl.setText("");
        price.setStyle("");

        String priceText = price.getText();
        if (priceText == null || priceText.trim().isEmpty()) {
            errorPriceLbl.setText("Price is required");
            price.setStyle("-fx-border-color: red;");
            return false;
        }

        try {
            double ticketPrice = Double.parseDouble(priceText);
            if (ticketPrice < 0) {
                errorPriceLbl.setText("Price must be positive");
                price.setStyle("-fx-border-color: red;");
                return false;
            }
        } catch (NumberFormatException e) {
            errorPriceLbl.setText("Invalid price format");
            price.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    private boolean validateDescriptionField() {
        errorDescriptionLbl.setText("");
        descriptionTextArea.setStyle("");

        String description = descriptionTextArea.getText();
        if (description == null || description.trim().isEmpty()) {
            errorDescriptionLbl.setText("Description is required");
            descriptionTextArea.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (!validateEventField()) isValid = false;
        if (!validateTicketTypeField()) isValid = false;
        if (!validatePriceField()) isValid = false;
        if (!validateDescriptionField()) isValid = false;

        return isValid;
    }
}
