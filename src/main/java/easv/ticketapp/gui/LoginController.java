package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.UserService;
import easv.ticketapp.security.Auth;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.synedra.validatorfx.Validator;

public class LoginController {
    private final UserService userService = new UserService();
    private final Validator validator = new Validator();

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLbl;

    public void intialize() {
        Platform.runLater(() -> {
            Scene scene = emailField.getScene();
            if (scene != null) {
                scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        handleLogin();
                    }
                });
            }
        });
    }

    @FXML
    private void handleLogin() {
        validate();
        String email = emailField.getText();
        String password = passwordField.getText();

        User auth = userService.authenticate(email, password);
        if (auth == null) {
            errorLbl.setText("Invalid email or password");
        } else {
            System.out.println("logged in");
            System.out.println(Auth.getUser().isAdmin());
            //TODO redirect
        }
    }

    private void validate() {
        validator.createCheck()
                .dependsOn("emailField", emailField.textProperty())
                .dependsOn("passwordField", passwordField.textProperty())
                .withMethod(c -> {
                    String emailValue = c.get("emailField");
                    if (emailValue.isEmpty()) {
                        c.error("Email is required");
                    }
                })
                .withMethod(c -> {
                    String passwordValue = c.get("passwordField");
                    if (passwordValue.isEmpty()) {
                        c.error("Email is required");
                    }
                })
                .decorates(emailField)
                .decorates(passwordField)
                .immediate()
                .immediateClear();
    }
}
