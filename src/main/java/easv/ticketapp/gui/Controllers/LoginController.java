package easv.ticketapp.gui.Controllers;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.UserService;
import easv.ticketapp.gui.PageManager;
import easv.ticketapp.security.Auth;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class LoginController {
    private final UserService userService = new UserService();

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button togglePasswordVisibilityBtn;
    @FXML
    private Label errorLbl;
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    public void initialize() {
        System.out.println("Auth? " + Auth.check());
        Platform.runLater(() -> {
            Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
            if (stage != null) {
                stage.setResizable(false);
            }
        });

        rootAnchorPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                ActionEvent actionEvent = new ActionEvent(emailField, null);
                handleLogin(actionEvent);
            }
        });

        togglePasswordVisibilityBtn.setOnMouseClicked(this::togglePasswordVisibility);
    }

    @FXML
    private void togglePasswordVisibility(MouseEvent event) {
        if (passwordField.isVisible()) {
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            passwordTextField.requestFocus();
            passwordTextField.positionCaret(passwordTextField.getText().length());
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        errorLbl.setText("");
        resetFieldStyles();

        if (!validateLoginFields()) {
            return;
        }

        String email = emailField.getText();
        String password = passwordField.isVisible() ?
                passwordField.getText() :
                passwordTextField.getText();

        User auth = userService.authenticate(email, password);

        if (auth == null) {
            errorLbl.setText("Invalid username/email or password");
        } else {
            if (Auth.getUser().isAdmin()) {
                PageManager.adminView(event);
            } else {
                PageManager.coordinatorsView(event);
            }
        }
    }

    /**
     * Validates the login form fields.
     *
     * @return true if validation passes, false otherwise.
     */
    private boolean validateLoginFields() {
        boolean isValid = true;

        String email = emailField.getText();
        String password = passwordField.isVisible() ?
                passwordField.getText() :
                passwordTextField.getText();

        if (email == null || email.trim().isEmpty()) {
            errorLbl.setText("Email is required");
            emailField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            errorLbl.setText("Please enter a valid email address");
            emailField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (password == null || password.trim().isEmpty()) {
            if (!isValid) {
                errorLbl.setText("Email and password are required");
            } else {
                errorLbl.setText("Password is required");
            }

            if (passwordField.isVisible()) {
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordTextField.setStyle("-fx-border-color: red;");
            }

            isValid = false;
        }

        return isValid;
    }

    /**
     * Resets the style of all input fields.
     */
    private void resetFieldStyles() {
        emailField.setStyle("");
        passwordField.setStyle("");
        passwordTextField.setStyle("");
    }

    public void forgotPasswordBtn(ActionEvent event) {
        PageManager.passwordEmailView(event);
    }
}