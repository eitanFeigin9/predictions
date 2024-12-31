package components.details.entity.population.value;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PopulationValueController implements Initializable {

    @FXML
    private Label entityNameLabel;

    @FXML
    private Label populationSizeLabel;
    @FXML
    private AnchorPane entityPopulationData;
    private SimpleStringProperty selectedEntity;
    private SimpleStringProperty populationAmount;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedEntity = new SimpleStringProperty();
        populationAmount = new SimpleStringProperty();
        populationSizeLabel.textProperty().bind(populationAmount);
        entityNameLabel.textProperty().bind(selectedEntity);
    }
    public void updateEntityName(String entityName,int count){
        selectedEntity.set(entityName);
        populationAmount.set(String.valueOf(count));
    }

}
