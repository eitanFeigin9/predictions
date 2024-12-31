package components.details.manage.center;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class CenterDetailsPresentationController {

    @FXML
    private HBox hBoxCenter;
    public void addNewCenterElement(ScrollPane entityPane) {
        // Set the center of the BorderPane to the provided entityPane
        hBoxCenter.getChildren().add(entityPane);
    }

    public HBox gethBoxCenter() {
        return hBoxCenter;
    }
}
