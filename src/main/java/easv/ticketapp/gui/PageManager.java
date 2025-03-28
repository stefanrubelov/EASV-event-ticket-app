package easv.ticketapp.gui;

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

    public static FXMLLoader switchView(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PageManager.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = primaryStage != null ? primaryStage : (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            return fxmlLoader;  // Return the FXMLLoader instance
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }

    // Switch to the login view
    public static FXMLLoader loginView(ActionEvent event) {
        return switchView("/easv/ticketapp/login-view.fxml", event);
    }

    // Switch to the admin view
    public static FXMLLoader  adminView(ActionEvent event) {
        return switchView("/easv/ticketapp/coordinators-scene.fxml", event);
    }

    // Switch to the coordinators view
    public static FXMLLoader  coordinatorsView(ActionEvent event) {
       return switchView("/easv/ticketapp/event-scene.fxml", event);
    }

    // Switch to the ticket view
    public static FXMLLoader  ticketView(ActionEvent event) {
       return switchView("/easv/ticketapp/ticket-scene.fxml", event);
    }

    // Switch to the edit event view
    public static FXMLLoader  editEventView(ActionEvent event) {
       return switchView("/easv/ticketapp/edit-event.fxml", event);
    }

    // Switch to the add coordinator view
    public static FXMLLoader addCoordinatorView(ActionEvent event) {
       return switchView("/easv/ticketapp/add-coordinator-scene.fxml", event);
    }
}
