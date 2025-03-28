package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.UserService;
import easv.ticketapp.security.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import net.synedra.validatorfx.Validator;

public class LoginController {
    private final UserService userService = new UserService();
    private final Validator validator = new Validator();

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
    public void initialize() {
        // Completely remove previous initialization logic
        togglePasswordVisibilityBtn.setOnMouseClicked(event -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        // Simple direct toggle mechanism
        if (passwordField.isVisible()) {
            // Switch to visible text field
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            passwordTextField.requestFocus();
        } else {
            // Switch back to password field
            passwordField.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordField.requestFocus();
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        validate();
        // Use getText() on the visible field
        String email = emailField.getText();
        String password = passwordField.isVisible() ?
                passwordField.getText() :
                passwordTextField.getText();

        User auth = userService.authenticate(email, password);
        if (auth == null) {
            errorLbl.setText("Invalid username/email or password");
        } else {
            if(Auth.getUser().isAdmin()){
                PageManager.adminView(event);
            }else{
                PageManager.coordinatorsView(event);
            }
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
                        c.error("Password is required");
                    }
                })
                .decorates(emailField)
                .decorates(passwordField)
                .immediate()
                .immediateClear();
    }
}