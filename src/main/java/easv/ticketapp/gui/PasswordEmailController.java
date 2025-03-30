package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.Email.EmailService;
import easv.ticketapp.bll.PasswordResetService;
import easv.ticketapp.bll.UserService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.synedra.validatorfx.Validator;

public class PasswordEmailController {
    private final PasswordResetService passwordResetService = new PasswordResetService();
    private final UserService userService = new UserService();
    private final Validator validator = new Validator();
    private boolean emailSent = false;
    private User user;

    @FXML
    TextField emailField;

    @FXML
    TextField tokenField;

    @FXML
    Label messageLbl;

    @FXML
    Label emailMessageLbl;

    @FXML
    Label tokenMessageLbl;

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
    }

    @FXML
    public void submitBtn(ActionEvent event) {
        messageLbl.setText("");
        resetFieldStyles();

        if (!emailSent) {
            if (!validateEmailField()) {
                return;
            }

            User user = userService.findByEmail(emailField.getText());
            if (user == null) {
                messageLbl.setText("Email could not be found");
                emailField.setStyle("-fx-border-color: red;");
                return;
            }

            this.user = user;
            int userId = user.getId();

            Task<Boolean> sendEmailTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    String token = passwordResetService.createToken(userId);

                    EmailService email = new EmailService()
                            .to(user.getEmail())
                            .subject("Your password reset token")
                            .html(true)
                            .body("Token:  <b>: " + token + "</b>" +
                                    "<br> <span>This token expires in 10 minutes</span>");

                    return email.send();
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    if (getValue()) {
                        emailSent = true;
                        tokenField.setVisible(true);
                        messageLbl.setText("Email sent");
                        emailField.setDisable(true);
                    } else {
                        emailMessageLbl.setText("Email not sent, try again");
                    }
                }

                @Override
                protected void failed() {
                    super.failed();
                    emailMessageLbl.setText("Email not sent, try again");
                }
            };

            // Start the task in the background
            new Thread(sendEmailTask).start();
        } else {
            if (!validateTokenField()) {
                return;
            }

            String token = tokenField.getText();
            boolean exists = passwordResetService.getToken(token);

            if (exists) {
                PageManager.passwordResetView(event, user);
            } else {
                tokenMessageLbl.setText("Invalid or expired token");
                tokenField.setStyle("-fx-border-color: red;");
            }
        }
    }

    /**
     * Validates the email field.
     *
     * @return true if validation passes, false otherwise.
     */
    private boolean validateEmailField() {
        String email = emailField.getText();
        emailMessageLbl.setText("");

        if (email == null || email.trim().isEmpty()) {
            emailMessageLbl.setText("Email is required");
            emailField.setStyle("-fx-border-color: red;");
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            emailMessageLbl.setText("Please enter a valid email address");
            emailField.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    /**
     * Validates the token field.
     *
     * @return true if validation passes, false otherwise.
     */
    private boolean validateTokenField() {
        String token = tokenField.getText();
        tokenMessageLbl.setText("");

        if (token == null || token.trim().isEmpty()) {
            tokenMessageLbl.setText("Token is required");
            tokenField.setStyle("-fx-border-color: red;");
            return false;
        }

        return true;
    }

    /**
     * Resets the style of all input fields.
     */
    private void resetFieldStyles() {
        emailField.setStyle("");
        tokenField.setStyle("");
    }

    @FXML
    public void backBtn(ActionEvent event) {
        PageManager.loginView(event);
    }
}
