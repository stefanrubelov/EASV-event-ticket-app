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

public class MyProfileController {
    private final UserService userService = new UserService();

    private User currentUser;

    @FXML
    public TextField nameField;
    @FXML
    public TextField emailField;
    @FXML
    private Label messageLbl;
    @FXML
    private Label nameErrorLbl;
    @FXML
    private Label emailErrorLbl;
    @FXML
    private Label passwordErrorLbl;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private PasswordField confirmationPasswordField;
    @FXML
    private TextField confirmationPasswordTextField;
    @FXML
    private Button togglePasswordVisibilityBtn;
    @FXML
    private Button toggleConfirmationPasswordVisibilityBtn;

    @FXML
    public void initialize() {
        if (Auth.check()) {
            this.currentUser = Auth.getUser();

            emailField.setText(this.currentUser.getEmail());
            nameField.setText(this.currentUser.getName());
        }

        togglePasswordVisibilityBtn.setOnMouseClicked(event -> togglePasswordVisibility(passwordField, passwordTextField));
        toggleConfirmationPasswordVisibilityBtn.setOnMouseClicked(event -> togglePasswordVisibility(confirmationPasswordField, confirmationPasswordTextField));

        clearAllErrorMessages();
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
    public void onSaveAction(ActionEvent actionEvent) {
        // Clear all previous messages
        clearAllErrorMessages();
        messageLbl.setText("");
        resetFieldStyles();

        if (!validateAllFields()) {
            return;
        }

        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.isVisible() ?
                passwordField.getText() :
                passwordTextField.getText();

        this.currentUser.setName(name);
        this.currentUser.setEmail(email);

        boolean update = true;

        if (!password.isEmpty()) {
            update = userService.updatePassword(this.currentUser, password);
        }

        boolean profileUpdate = userService.updateUser(this.currentUser);

        if (update && profileUpdate) {
            messageLbl.setText("Profile updated successfully");
        } else {
            messageLbl.setText("Something went wrong, please try again.");
        }
    }

    /**
     * Clears all error message labels.
     */
    private void clearAllErrorMessages() {
        nameErrorLbl.setText("");
        emailErrorLbl.setText("");
        passwordErrorLbl.setText("");
    }

    /**
     * Validates all input fields.
     *
     * @return true if validation passes, false otherwise.
     */
    private boolean validateAllFields() {
        boolean isNameValid = validateNameField();
        boolean isEmailValid = validateEmailField();

        // Validate passwords only if they're not empty
        // (assuming password change is optional in profile update)
        String password = passwordField.isVisible() ?
                passwordField.getText() :
                passwordTextField.getText();

        boolean isPasswordValid = password.isEmpty() || validatePasswordFields();

        return isNameValid && isEmailValid && isPasswordValid;
    }

    /**
     * Validates the name field.
     *
     * @return true if validation passes, false otherwise.
     */
    private boolean validateNameField() {
        String name = nameField.getText();

        if (name == null || name.trim().isEmpty()) {
            nameErrorLbl.setText("Name is required");
            nameField.setStyle("-fx-border-color: red;");
            return false;
        }

        if (name.length() < 2) {
            nameErrorLbl.setText("Name must be at least 2 characters long");
            nameField.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    /**
     * Validates the email field.
     *
     * @return true if validation passes, false otherwise.
     */
    private boolean validateEmailField() {
        String email = emailField.getText();

        if (email == null || email.trim().isEmpty()) {
            emailErrorLbl.setText("Email is required");
            emailField.setStyle("-fx-border-color: red;");
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            emailErrorLbl.setText("Please enter a valid email address");
            emailField.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    /**
     * Validates the password fields.
     *
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

        if (!password.isEmpty() && password.length() < 8) {
            passwordErrorLbl.setText("Password must be at least 8 characters long");
            if (passwordField.isVisible()) {
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordTextField.setStyle("-fx-border-color: red;");
            }
            isValid = false;
        }

        if (!confirmPassword.isEmpty() && !password.equals(confirmPassword)) {
            if (passwordErrorLbl.getText().isEmpty()) {
                passwordErrorLbl.setText("Passwords must match");
            } else {
                passwordErrorLbl.setText(passwordErrorLbl.getText() + ", Passwords must match");
            }

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
        nameField.setStyle("");
        emailField.setStyle("");
        passwordField.setStyle("");
        passwordTextField.setStyle("");
        confirmationPasswordField.setStyle("");
        confirmationPasswordTextField.setStyle("");
    }
}