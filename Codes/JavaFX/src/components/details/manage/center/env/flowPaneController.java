package components.details.manage.center.env;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

public class flowPaneController {

    @FXML
    private FlowPane flowPaneId;


    public void addNewFlowPaneElement(ScrollPane entityPane) {
        // Set the center of the BorderPane to the provided entityPane
        flowPaneId.getChildren().add(entityPane);
    }

    public FlowPane getFlowPaneCenter() {
        return flowPaneId;
    }


}



