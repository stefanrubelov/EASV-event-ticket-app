package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PageManager {
    private static Stage primaryStage;

    private static BaseLayoutController baseLayoutController;

    private static String currentView = null;

    public static FXMLLoader switchView(String fxmlPath, ActionEvent event, String title) {
        try {
            if (primaryStage == null) {
                primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            }

            if (baseLayoutController == null) {
                FXMLLoader baseLoader = new FXMLLoader(PageManager.class.getResource("/easv/ticketapp/base-layout.fxml"));
                Parent root = baseLoader.load();
                baseLayoutController = baseLoader.getController();
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("EASV");
            }

            FXMLLoader sceneLoader = new FXMLLoader(PageManager.class.getResource(fxmlPath));
            Node sceneView = sceneLoader.load();
            baseLayoutController.setCenterNode(sceneView);

            primaryStage.sizeToScene();
            primaryStage.setResizable(sceneView.prefWidth(-1) > 0 || sceneView.prefHeight(-1) > 0);
            primaryStage.show();
            return sceneLoader;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }

    public static FXMLLoader switchViewNewScene(String fxmlPath, ActionEvent event, String title) {
        try {
            FXMLLoader sceneLoader = new FXMLLoader(PageManager.class.getResource(fxmlPath));
            Parent root = sceneLoader.load();
            Scene scene = new Scene(root);

            Stage stage = primaryStage != null ? primaryStage : (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);

            stage.sizeToScene();
            stage.setResizable(root.prefWidth(-1) > 0 || root.prefHeight(-1) > 0);

            stage.centerOnScreen();
            stage.show();

            return sceneLoader;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }

    public static FXMLLoader loginView(ActionEvent event) {
        return switchViewNewScene("/easv/ticketapp/auth/login-view.fxml", event, "Login");
    }

    public static FXMLLoader adminView(ActionEvent event) {
        currentView = "coordinators";
        return switchView("/easv/ticketapp/coordinators-scene.fxml", event, "Coordinators");
    }

    public static FXMLLoader coordinatorsView(ActionEvent event) {
        currentView = "events";
        return switchView("/easv/ticketapp/event-scene.fxml", event, "Events");
    }

    public static FXMLLoader ticketView(ActionEvent event) {
        return switchView("/easv/ticketapp/ticket-scene.fxml", event, "Tickets");
    }

    public static FXMLLoader editEventView(ActionEvent event) {
        return switchView("/easv/ticketapp/edit-event.fxml", event, "Edit event");
    }

    public static FXMLLoader addCoordinatorView(ActionEvent event) {
        return switchView("/easv/ticketapp/add-coordinator-scene.fxml", event, "Add coordinator");
    }

    public static FXMLLoader passwordEmailView(ActionEvent event) {
        return switchViewNewScene("/easv/ticketapp/auth/password-email.fxml", event, "Password Reset");
    }

    public static FXMLLoader passwordResetView(ActionEvent event, User user) {
        try {
            FXMLLoader sceneLoader = new FXMLLoader(PageManager.class.getResource("/easv/ticketapp/auth/password-reset.fxml"));
            Parent root = sceneLoader.load();

            PasswordResetController controller = sceneLoader.getController();
            controller.setUser(user);

            Scene scene = new Scene(root);
            Stage stage = primaryStage != null ? primaryStage : (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Password Reset");

            stage.sizeToScene();
            stage.setResizable(root.prefWidth(-1) > 0 || root.prefHeight(-1) > 0);

            stage.centerOnScreen();
            stage.show();

            return sceneLoader;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: /easv/ticketapp/password-reset.fxml", e);
        }
    }

    public static String getCurrentView() {
        return currentView;
    }
}
