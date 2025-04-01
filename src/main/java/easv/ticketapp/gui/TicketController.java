package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;
import easv.ticketapp.bll.EventManager;
import easv.ticketapp.bll.TicketManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class TicketController implements Initializable {
    private String filePath;
    private Ticket ticket;

    private final EventManager eventManager = new EventManager();
    private final TicketManager ticketManager = new TicketManager();
    @FXML
    private VBox vBoxInputContainer;
    @FXML
    private TextField availableTicketsField;
    @FXML
    private TextField descriptionTxtfield;
    @FXML
    private ComboBox<Event> eventBox;
    @FXML
    private TextField price;
    @FXML
    private ComboBox<TicketType> ticketBox;

    private final static String IMAGES_DIRECTORY_PATH = "src/EmailClient/resources/images";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Event> events = eventManager.getAllEvents();
        System.out.println(events.size());
        List<TicketType> ticketTypes = ticketManager.getAllTicketTypes();
        eventBox.getItems().addAll(events);
        ticketBox.getItems().addAll(ticketTypes);
    }

    @FXML
    void onPreview(ActionEvent event) {
        if (eventBox.getValue() == null || ticketBox.getValue() == null ||
               price.getText().isEmpty() || availableTicketsField.getText().isEmpty()) {
            System.out.println("Please fill in all fields before previewing.");
            return;
        }
        Event selectedEvent = eventBox.getValue();
        TicketType selectedTicketType = ticketBox.getValue();
        String description = descriptionTxtfield.getText();
        String location = selectedEvent.getLocation();
        LocalDateTime eventDate = selectedEvent.getDate();
        double ticketPrice = Double.parseDouble(price.getText());
        Integer availableTickets = Integer.valueOf(availableTicketsField.getText());

        Ticket ticket = new Ticket(
                0, // Generates automatically
                selectedEvent.getName(),
                ticketPrice,
                "General", // TODO
                description,
                location,
                eventDate,
                selectedTicketType,
                availableTickets);
        PageManager.ticketPreview(event,ticket);
    }

    @FXML
    void onSubmit(ActionEvent event) {
        try {
            // Validation of the input fields
            if (eventBox.getValue() == null || ticketBox.getValue() == null || price.getText().isEmpty() || descriptionTxtfield.getText().isEmpty() || availableTicketsField.getText().isEmpty()){
                System.out.println("Please fill in all fields before previewing.");
                return;
            }

            // Get the input values
            Event selectedEvent = eventBox.getValue();
            TicketType selectedTicketType = ticketBox.getValue();
            String description = descriptionTxtfield.getText();
            String location = selectedEvent.getLocation();
            LocalDateTime eventDate = selectedEvent.getDate();
            double ticketPrice = Double.parseDouble(price.getText());
            Integer availableTickets = Integer.valueOf(availableTicketsField.getText());

            // Creates new ticket
            Ticket newTicket = new Ticket(
                    0, // Generates automatically
                    selectedEvent.getName(),
                    ticketPrice,
                    "General", // TODO
                    description,
                    location,
                    eventDate,
                    selectedTicketType,
                    availableTickets
            );

            ticketManager.addTicket(newTicket);

            // Testing
            System.out.println("Ticket added!");


        } catch (NumberFormatException e) {
            System.out.println("Error: The ticket price should have a valid number");
        } catch (Exception e) {
            System.out.println("Error adding the ticket: " + e.getMessage());
        }
    }

    @FXML
    void onPrint(ActionEvent event) {

    }
}
