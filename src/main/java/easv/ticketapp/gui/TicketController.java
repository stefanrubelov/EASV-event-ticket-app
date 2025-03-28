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
import java.sql.SQLException;
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
    private Button previewBtn;
    @FXML
    private TextField price;
    @FXML
    private Button submitBtn;
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

}
