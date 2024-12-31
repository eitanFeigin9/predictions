package components.details.tree.action.pane.set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SetController {

    @FXML
    private VBox PropVbox;

    @FXML
    private Label propNameLabel;

    @FXML
    private AnchorPane setPane;
    public void setLabel(String data){
        Label label = new Label(data); // Replace with your desired text
        Font cooperBlackFont = Font.font("Cooper Black", FontWeight.BOLD, 20);
        label.setFont(cooperBlackFont);
        PropVbox.getChildren().add(label);
    }
}
