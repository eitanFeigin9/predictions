package components.details.tree.action.pane.proximity;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProximityController {

    @FXML
    private VBox PropVbox;

    @FXML
    private Label propNameLabel;

    @FXML
    private AnchorPane proximityPane;
    public void setLabel(String data){
        Label label = new Label(data);
        Font cooperBlackFont = Font.font("Cooper Black", FontWeight.BOLD, 17);
        label.setFont(cooperBlackFont);
        PropVbox.getChildren().add(label);
    }

}
