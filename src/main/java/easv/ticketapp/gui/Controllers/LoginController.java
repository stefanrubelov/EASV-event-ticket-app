package easv.ticketapp.gui.Controllers;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.UserService;
import easv.ticketapp.gui.PageManager;
import easv.ticketapp.security.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private Label errorLbl;

    @FXML
    public void initialize(ActionEvent actionEvent) {
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin(actionEvent);
            }
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        validate();
        String email = emailField.getText();
        String password = passwordField.getText();

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
                        c.error("Email is required");
                    }
                })
                .decorates(emailField)
                .decorates(passwordField)
                .immediate()
                .immediateClear();
    }
}
