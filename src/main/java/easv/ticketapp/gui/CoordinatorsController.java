package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CoordinatorsController {
    private final UserService userService = new UserService();

    @FXML
    private TableView<User> coordinatorsTable;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private ContextMenu contextMenuCoordinators;
    @FXML
    private MenuItem delete;
    @FXML
    private MenuItem edit;
    @FXML
    private MenuItem save;

    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        fetchUsers();
        setupTableColumns();
        setUpContextMenu();
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        coordinatorsTable.setItems(userList);
    }

    private void fetchUsers() {
        List<User> users = userService.getCoordinators();

        userList.clear();
        userList.setAll(users);
        coordinatorsTable.setItems(userList);

        System.out.println("Fetched users: " + users.size());

        if (users.isEmpty()) {
            System.out.println("No users found in the database!");
        }
    }

    private void setUpContextMenu() {

        delete.setOnAction(_ -> {
            User selectedUser = coordinatorsTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                userService.deleteUser(selectedUser);
                ObservableList<User> currentItems = coordinatorsTable.getItems();
                currentItems.remove(selectedUser);
            }
        });

        coordinatorsTable.setRowFactory(_ -> {
            TableRow<User> row = new TableRow<>();
            row.setContextMenu(contextMenuCoordinators);
            return row;
        });
    }

    public void onCreateCoordinator(ActionEvent event) {
        PageManager.addCoordinatorView(event);
    }
}
