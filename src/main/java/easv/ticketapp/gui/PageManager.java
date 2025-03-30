package easv.ticketapp.gui;

import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.gui.Controllers.TicketPreviewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PageManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchView(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PageManager.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = primaryStage != null ? primaryStage : (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }

    public static void loginView(ActionEvent event) {
        switchView("/easv/ticketapp/login-view.fxml", event);
    }

    public static void adminView(ActionEvent event) {
        switchView("/easv/ticketapp/coordinators-scene.fxml", event);
    }
    public static void coordinatorsView(ActionEvent event) {
        switchView("/easv/ticketapp/event-scene.fxml", event);
    }
    public static void ticketView(ActionEvent event) {
        switchView("/easv/ticketapp/ticket-factory-view.fxml", event);
    }
    public static void ticketPreview(ActionEvent event, Ticket ticket, String imagePath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PageManager.class.getResource("/easv/ticketapp/ticket-preview.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Get the controller
            TicketPreviewController controller = fxmlLoader.getController();

            // Pass data to the preview
            controller.updatePreview(
                    ticket.getEventName(),
                    ticket.getDate().toString(),
                    ticket.getLocation(),
                    ticket.getPrice(),
                    ticket.getSeatNumber(),
                    ticket.getDescription(),
                    ticket.getPerks())
            ;

            if (imagePath != null) {
                controller.setBackgroundImage(imagePath);
            }

            // Show the new scene
            Stage stage = primaryStage != null ? primaryStage : (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ticket preview", e);
        }
    }
    public static void addCoordinatorView(ActionEvent event) {
        switchView("/easv/ticketapp/add-coordinator-scene.fxml", event);
    }
}
