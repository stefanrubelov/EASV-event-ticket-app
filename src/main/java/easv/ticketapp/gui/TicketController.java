package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;
import easv.ticketapp.bll.EventManager;
import easv.ticketapp.bll.TicketManager;
import easv.ticketapp.utils.PdfExporter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
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
    private Button savePdfBtn;

    private Scene scene;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Event> events = eventManager.getAllEvents();
        List<TicketType> ticketTypes = ticketManager.getAllTicketTypes();
        eventBox.getItems().addAll(events);
        ticketTypeBox.getItems().addAll(ticketTypes);
    }

    @FXML
    void onPreview(ActionEvent actionEvent) {
        if (eventBox.getValue() == null || ticketTypeBox.getValue() == null || price.getText().isEmpty()) {
            System.out.println("Please fill in all fields before previewing.");
            return;
        }
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
            // Validation of the input fields
            if (eventBox.getValue() == null || ticketTypeBox.getValue() == null || price.getText().isEmpty() || descriptionTextArea.getText().isEmpty()) {
                System.out.println("Please fill in all fields before previewing.");
                return;
            }

            // Get the input values
            Event selectedEvent = eventBox.getValue();
            TicketType selectedTicketType = ticketTypeBox.getValue();
            String description = descriptionTextArea.getText();
            double ticketPrice = Double.parseDouble(price.getText());

            // Creates new ticket
            Ticket newTicket = new Ticket(selectedEvent.getId(), ticketPrice, description, selectedTicketType, selectedEvent);
            ticketManager.addTicket(newTicket);

            // Testing
            System.out.println("Ticket added!");


        } catch (NumberFormatException e) {
            System.out.println("Error: The ticket price should have a valid number");
        } catch (Exception e) {
            System.out.println("Error adding the ticket: " + e.getMessage());
        }
    }
}
