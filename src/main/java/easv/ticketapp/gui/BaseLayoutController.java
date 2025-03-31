package easv.ticketapp.gui;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class BaseLayoutController {

    public BorderPane baseLayout;
public VBox contentContainer;
    public void setCenterNode(Node node) {
        contentContainer.getChildren().clear();
        contentContainer.getChildren().add(node);
    }
}
