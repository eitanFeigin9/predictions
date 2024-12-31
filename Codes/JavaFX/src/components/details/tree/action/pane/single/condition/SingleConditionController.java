package components.details.tree.action.pane.single.condition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SingleConditionController {

    @FXML
    private VBox PropVbox;

    @FXML
    private Label propNameLabel;

    @FXML
    private AnchorPane singlePane;
    public void setLabel(String data){
        Label label = new Label(data);
        Font cooperBlackFont = Font.font("Cooper Black", FontWeight.BOLD, 14);
        label.setFont(cooperBlackFont);
        PropVbox.getChildren().add(label);
    }
}
