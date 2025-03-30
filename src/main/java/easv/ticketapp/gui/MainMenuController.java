package easv.ticketapp.gui;

import easv.ticketapp.security.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class MainMenuController {
    @FXML
    private VBox menuItemsContainer;
    @FXML
    private Label userNameLbl;
    @FXML
    private Label userEmailLbl;

    private Button activeButton; // Stores the currently active button

    public void initialize() {
        fillUserMenu();

        String user = Auth.getUser().getName() != null && !Auth.getUser().getName().isEmpty() ? Auth.getUser().getName() : "My profile";
        userNameLbl.setText(user);
        String email = Auth.getUser().getEmail();
        userEmailLbl.setText(email);
    }

    private void fillUserMenu() {
        Button eventsButton = addMenuItem("Events", this::eventsBtnClick);

        Button coordinatorsButton = null;
        if (Auth.getUser().isAdmin()) {
            coordinatorsButton = addMenuItem("Coordinators", this::coordinatorsBtnClick);
        }

        // Set the default active menu item based on the current view
        String currentView = PageManager.getCurrentView();
        if ("coordinators".equals(currentView) && coordinatorsButton != null) {
            setActiveButton(coordinatorsButton);
        } else {
            setActiveButton(eventsButton);
        }
    }

    private Button addMenuItem(String text, javafx.event.EventHandler<ActionEvent> action) {
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 10; -fx-text-fill: #000000;");
        button.setPrefHeight(40);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("menu-item");
        button.setOnAction(event -> {
            setActiveButton(button);
            action.handle(event);
        });

        Label label = new Label(text);
        label.setFont(new Font("System Bold", 14));
        label.setTextFill(Paint.valueOf("#000000"));
        label.getStyleClass().add("menu-item-label");

        HBox hbox = new HBox(10, label);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        button.setGraphic(hbox);

        menuItemsContainer.getChildren().add(button);

        return button;
    }

    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("active");
        }
        button.getStyleClass().add("active");
        activeButton = button;
    }

    @FXML
    private void eventsBtnClick(ActionEvent event) {
        setActiveButton((Button) event.getSource());
        PageManager.coordinatorsView(event);
    }

    @FXML
    private void coordinatorsBtnClick(ActionEvent event) {
        setActiveButton((Button) event.getSource());
        PageManager.adminView(event);
    }

    @FXML
    private void logoutBtnClick(ActionEvent event) {
        Auth.logout();
        PageManager.loginView(event);
    }
}
