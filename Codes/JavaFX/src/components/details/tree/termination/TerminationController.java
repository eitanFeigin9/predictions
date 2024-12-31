package components.details.tree.termination;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TerminationController implements Initializable {

    @FXML
    private Label firstLabel;

    @FXML
    private Label secondLabel;
    private SimpleBooleanProperty isTwoProperties;
    public TerminationController(){
        isTwoProperties = new SimpleBooleanProperty(false);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        secondLabel.visibleProperty().bind(isTwoProperties);

    }
    public void dynamicInitialize(List<String> stringList) {
        firstLabel.setText(stringList.get(0));
        if(stringList.size()>1)
        {
            secondLabel.setText(stringList.get(1));
            isTwoProperties.set(true);
        }

    }
}
