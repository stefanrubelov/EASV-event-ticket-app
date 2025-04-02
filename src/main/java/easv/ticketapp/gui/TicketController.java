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
import java.util.Optional;
import java.util.ResourceBundle;

public class TicketController implements Initializable {
    private String filePath;
    private Ticket ticket;

    private final EventManager eventManager = new EventManager();
    private final TicketManager ticketManager = new TicketManager();
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
        List<TicketType> ticketTypes = ticketManager.getAllTicketTypes();
        eventBox.getItems().addAll(events);
        ticketBox.getItems().addAll(ticketTypes);

        // Add a special placeholder for "+ Add new type..."
        ticketBox.getItems().add(new TicketType("+ Add new type..."));

        // Handle selection event
        ticketBox.setOnAction(event -> handleTicketTypeSelection());

    }

    private void handleTicketTypeSelection() {
        TicketType selectedType = ticketBox.getValue();

        if (selectedType != null && "+ Add new type...".equals(selectedType.getType())) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Ticket Type");
            dialog.setHeaderText("Enter the new ticket type:");
            dialog.setContentText("Ticket Type:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newType -> {
                if (!newType.trim().isEmpty()) {
                    // Create a new TicketType object
                    TicketType newTicketType = new TicketType(newType);

                    // Save it to the database (if needed)
                    ticketManager.addTicketType(newTicketType);

                    // Update the ComboBox (add before "+ Add new type...")
                    int lastIndex = ticketBox.getItems().size() - 1;
                    ticketBox.getItems().add(lastIndex, newTicketType);
                    ticketBox.setValue(newTicketType); // Set the new selection
                }
            });
        }
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

    public void addTicketType(ActionEvent event) {
/*
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Ticket Type");
        dialog.setHeaderText("Enter a new ticket type:");
        Optional<TicketType> result = dialog.showAndWait();

        result.ifPresent(newType -> {
            if (!newType.trim().isEmpty() && !ticketBox.getItems().contains(newType)) {
                ticketBox.getItems().add(newType);
                ticketBox.setValue(newType);
            }
        });
    });*/
    }
}
