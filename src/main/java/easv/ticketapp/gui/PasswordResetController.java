package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.PasswordResetService;
import easv.ticketapp.bll.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.synedra.validatorfx.Validator;

public class PasswordResetController {
    private final UserService userService = new UserService();
    private final PasswordResetService passwordResetService = new PasswordResetService();
    private final Validator validator = new Validator();

    private User user;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button togglePasswordVisibilityBtn;

    @FXML
    private PasswordField confirmationPasswordField;
    @FXML
    private TextField confirmationPasswordTextField;
    @FXML
    private Button toggleConfirmationPasswordVisibilityBtn;

    @FXML
    private Label messageLbl;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
            if (stage != null) {
                stage.setResizable(false);
            }
        });

        togglePasswordVisibilityBtn.setOnMouseClicked(event -> togglePasswordVisibility(passwordField, passwordTextField));
        toggleConfirmationPasswordVisibilityBtn.setOnMouseClicked(event -> togglePasswordVisibility(confirmationPasswordField, confirmationPasswordTextField));
    }

    private void togglePasswordVisibility(PasswordField passwordField, TextField passwordTextField) {
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
    public void saveBtn(ActionEvent event) {
        messageLbl.setText("");
        resetFieldStyles();

        if (!validatePasswordFields()) {
            return;
        }

        String password = passwordField.isVisible() ?
                passwordField.getText() :
                passwordTextField.getText();
        System.out.println(password);
        boolean update = userService.updatePassword(user, password);
        System.out.println(update);
        if (update) {
            passwordResetService.removeToken(user.getId());
            PageManager.loginView(event);
        } else {
            messageLbl.setText("Something went wrong, please try again.");
        }
    }

    /**
     * Validates the password fields.
     * @return true if validation passes, false otherwise.
     */
    private boolean validatePasswordFields() {
        boolean isValid = true;

        String password = passwordField.isVisible() ?
                passwordField.getText() :
                passwordTextField.getText();

        String confirmPassword = confirmationPasswordField.isVisible() ?
                confirmationPasswordField.getText() :
                confirmationPasswordTextField.getText();

        if (password == null || password.trim().isEmpty()) {
            messageLbl.setText("Password is required");
            if (passwordField.isVisible()) {
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordTextField.setStyle("-fx-border-color: red;");
            }
            isValid = false;
        }
        else if (password.length() < 8) {
            messageLbl.setText("Password must be at least 8 characters long");
            if (passwordField.isVisible()) {
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordTextField.setStyle("-fx-border-color: red;");
            }
            isValid = false;
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            if (!isValid) {
                messageLbl.setText(messageLbl.getText() + ", Confirmation password is required");
            } else {
                messageLbl.setText("Confirmation password is required");
            }

            if (confirmationPasswordField.isVisible()) {
                confirmationPasswordField.setStyle("-fx-border-color: red;");
            } else {
                confirmationPasswordTextField.setStyle("-fx-border-color: red;");
            }
            isValid = false;
        }

        if (isValid && !password.equals(confirmPassword)) {
            messageLbl.setText("Passwords must match");

            if (passwordField.isVisible()) {
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordTextField.setStyle("-fx-border-color: red;");
            }

            if (confirmationPasswordField.isVisible()) {
                confirmationPasswordField.setStyle("-fx-border-color: red;");
            } else {
                confirmationPasswordTextField.setStyle("-fx-border-color: red;");
            }

            isValid = false;
        }

        return isValid;
    }

    /**
     * Resets the style of all input fields.
     */
    private void resetFieldStyles() {
        passwordField.setStyle("");
        passwordTextField.setStyle("");
        confirmationPasswordField.setStyle("");
        confirmationPasswordTextField.setStyle("");
    }

    @FXML
    public void backBtn(ActionEvent event) {
        PageManager.loginView(event);
    }
    public void setUser(User user) {
        this.user = user;
    }
}
