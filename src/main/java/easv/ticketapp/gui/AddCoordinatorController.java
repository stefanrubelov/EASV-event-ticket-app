package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddCoordinatorController {

    final private UserService userService = new UserService();
    @FXML
    private TextField coordinatorEmail;

    @FXML
    private TextField coordinatorName;

    @FXML
    private TextField coordinatorPassword;

    public void onReturn(ActionEvent event) {
        PageManager.adminView(event);
    }

    public void save(ActionEvent event) {
        String name = coordinatorName.getText();
        String email = coordinatorEmail.getText();
        String password = coordinatorPassword.getText();

        User user = new User(name, email, password);
        userService.addUser(user);
    }
}
