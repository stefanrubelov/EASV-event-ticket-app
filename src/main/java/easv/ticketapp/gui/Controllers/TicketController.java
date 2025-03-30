package easv.ticketapp.gui.Controllers;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;
import easv.ticketapp.bll.EventManager;
import easv.ticketapp.bll.TicketManager;
import easv.ticketapp.gui.PageManager;
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
    private DatePicker datePicker;
    @FXML
    private TextField descriptionTxtfield;
    @FXML
    private ComboBox<Event> eventBox;
    @FXML
    private Label lblPath;
    @FXML
    private TextField locationTxtfield;
    @FXML
    private TextField price;
    @FXML
    private ComboBox<TicketType> ticketBox;

    private final static String IMAGES_DIRECTORY_PATH = "src/main/resources/images";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Event> events = eventManager.getAllEvents();
        System.out.println(events.size());
        List<TicketType> ticketTypes = ticketManager.getAllTicketTypes();
        eventBox.getItems().addAll(events);
        ticketBox.getItems().addAll(ticketTypes);
    }
    @FXML
    void onYesSeats(ActionEvent event) {

    }

    @FXML
    void selectImage(ActionEvent event) throws IOException{
        Stage stage = (Stage) vBoxInputContainer.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files (*.jpg, *.png)", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        // set initial directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            lblPath.setText("Selected Path: " + selectedFile.getAbsolutePath());
            setFilePath(selectedFile.getAbsolutePath());

        } else {
            lblPath.setText("No file selected");
        }
    }
    private void setFilePath(String filePath) throws IOException {
        Path sourcePath = Paths.get(filePath);

        Path destinationDir = Paths.get(IMAGES_DIRECTORY_PATH);

        Path destinationPath = destinationDir.resolve(sourcePath.getFileName());

        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

        this.filePath = destinationPath.toString();
    }
    @FXML
    void onPreview(ActionEvent event) {
        if (eventBox.getValue() == null || ticketBox.getValue() == null || datePicker.getValue() == null ||
                locationTxtfield.getText().isEmpty() || price.getText().isEmpty()) {
            System.out.println("Please fill in all fields before previewing.");
            return;
        }
        Ticket ticket = new Ticket(1, "name name ", 39.0, "1f", "test", "description", "locaiton", LocalDateTime.now(), new TicketType("vip"), "default.jpg");
        PageManager.ticketPreview(event,ticket,filePath);
    }

    @FXML
    void onSubmit(ActionEvent event) {
        try {
            // Validation of the input fields
            if (eventBox.getValue() == null || ticketBox.getValue() == null || datePicker.getValue() == null ||
                    locationTxtfield.getText().isEmpty() || price.getText().isEmpty() || descriptionTxtfield.getText().isEmpty()) {
                System.out.println("Please fill in all fields before previewing.");
                return;
            }

            // Get the input values
            Event selectedEvent = eventBox.getValue();
            TicketType selectedTicketType = ticketBox.getValue();
            String location = locationTxtfield.getText();
            String description = descriptionTxtfield.getText();
            LocalDateTime eventDate = datePicker.getValue().atStartOfDay();
            double ticketPrice = Double.parseDouble(price.getText());
            String ticketImage = lblPath.getText();

            // Creates new ticket
            Ticket newTicket = new Ticket(
                    0, // Generates automatically
                    selectedEvent.getName(),
                    ticketPrice,
                    "1A", // TODO
                    "General", // TODO
                    description,
                    location,
                    eventDate,
                    selectedTicketType,
                    ticketImage
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
