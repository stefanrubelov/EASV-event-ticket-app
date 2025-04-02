package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.Email.EmailService;
import easv.ticketapp.bll.UserService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddCoordinatorController {
    final private UserService userService = new UserService();

    @FXML
    private TextField coordinatorEmail;
    @FXML
    private TextField coordinatorName;
    @FXML
    private Label errorLbl;

    public void onReturnAction(ActionEvent event) {
        PageManager.adminView(event);
    }

    public void onSaveAction(ActionEvent event) {
        resetFieldStyles();
        if (validate()) {
            String name = coordinatorName.getText();
            String email = coordinatorEmail.getText();
            Integer user_type = 2;
            User user = new User(name, email, user_type);
            userService.addUser(user);
            sendEmailToUser(email);
            PageManager.adminView(event);
        }
    }

    /**
     * Validates the coordinator form fields.
     *
     * @return true if validation passes, false otherwise.
     */
    private boolean validate() {
        boolean isValid = true;

        String name = coordinatorName.getText();
        String email = coordinatorEmail.getText();

        if (name == null || name.trim().isEmpty()) {
            errorLbl.setText("Name is required");
            coordinatorName.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (email == null || email.trim().isEmpty()) {
            if (!isValid) {
                errorLbl.setText("Name and email are required");
            } else {
                errorLbl.setText("Email is required");
            }
            coordinatorEmail.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email != null && !email.matches(emailRegex)) {
            if (!isValid) {
                errorLbl.setText(errorLbl.getText() + " and email format is invalid");
            } else {
                errorLbl.setText("Please enter a valid email address");
            }
            coordinatorEmail.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Resets the style of all input fields.
     */
    private void resetFieldStyles() {
        coordinatorName.setStyle("");
        coordinatorEmail.setStyle("");
        errorLbl.setText("");
    }

    private void sendEmailToUser(String emailAddress) {
        Task<Boolean> sendEmailTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {

                EmailService email = new EmailService()
                        .to(emailAddress)
                        .subject("Registered as coordinator")
                        .html(true)
                        .body("Welcome to EASV" +
                                "<br/> <span>You have been registered as a coordinator. </span> <br/> <span> When logging in for the first time in the system click 'forgot password' to set up your new password.</span>");

                return email.send();
            }
        };

        new Thread(sendEmailTask).start();
    }
}
