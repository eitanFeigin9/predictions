package components.details.tree.action.pane.multiple.condition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MultipleConditionController {

    @FXML
    private VBox PropVbox;

    @FXML
    private AnchorPane multiplePane;

    @FXML
    private Label propNameLabel;
    public void setLabel(String data){
        Label label = new Label(data);
        Font cooperBlackFont = Font.font("Cooper Black", FontWeight.BOLD, 14);
        label.setFont(cooperBlackFont);
        PropVbox.getChildren().add(label);
    }
}
