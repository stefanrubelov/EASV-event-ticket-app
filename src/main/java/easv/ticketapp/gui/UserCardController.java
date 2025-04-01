package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class UserCardController {
    private User user;
    @FXML
    public Button handleDeleteBtn;

    @FXML
    public Button handleEditBtn;

    @FXML
    public Label nameLbl;

    @FXML
    public Label emailLbl;

    @FXML
    public Label createdAtLbl;

    private CoordinatorsController coordinatorsController;

    public void setUser(User user) {
        this.user = user;
        nameLbl.setText(user.getName());
        emailLbl.setText(user.getEmail());
        createdAtLbl.setText(user.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public void setController(CoordinatorsController controller) {
        this.coordinatorsController = controller;
    }

    @FXML
    public void handleEditAction(ActionEvent event) {

    }

    @FXML
        public void handleDeleteAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete user");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText(user.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            coordinatorsController.deleteUser(user);
        }
    }
}
