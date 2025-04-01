package easv.ticketapp.gui;

import easv.ticketapp.be.User;
import easv.ticketapp.bll.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CoordinatorsController {
    private final UserService userService = new UserService();
    private static final int ITEMS_PER_PAGE = 10;

    @FXML
    private VBox usersContainer;
    @FXML
    private Pagination pagination;

    private List<User> users;

    @FXML
    public void initialize() {
        users = userService.getCoordinators();
        setupPagination();
    }

    private void setupPagination() {
        int pageCount = (int) Math.ceil((double) users.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> loadPage(newIndex.intValue()));
        loadPage(0);
    }

    private void loadPage(int pageIndex) {
        usersContainer.getChildren().clear();

        int start = pageIndex * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, users.size());

        for (int i = start; i < end; i++) {
            User user = users.get(i);
            try {
                FXMLLoader childLoader = new FXMLLoader(getClass().getResource("/easv/ticketapp/user-card.fxml"));
                Pane userCard = childLoader.load();

                UserCardController userCardController = childLoader.getController();
                userCardController.setUser(user);
                userCardController.setController(this);

                usersContainer.getChildren().add(userCard);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading user-card.fxml");
            }
        }
    }

    public void deleteUser(User user) {
        users.remove(user);
        userService.deleteUser(user);
        setupPagination();
        loadPage(pagination.getCurrentPageIndex());
    }

    public void handleCreateCoordinatorBtn(ActionEvent actionEvent) {

    }
}
