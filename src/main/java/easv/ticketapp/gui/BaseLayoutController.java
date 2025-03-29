package easv.ticketapp.gui;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class BaseLayoutController {

    public BorderPane baseLayout;

    public void setCenterNode(Node node) {
        baseLayout.setCenter(node);
    }
}
