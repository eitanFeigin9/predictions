package components.details.entity.population;


import exception.*;
import system.file.ManageSystemToUi;
import system.file.instruction.entity.population.PopulationUpdatesDto;
import input.dto.StringAndIntDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.util.ResourceBundle;

public class PopulationInputController implements Initializable {

    @FXML
    private Label entityNameLabel;

    @FXML
    private TextField populationInput;
    @FXML
    private AnchorPane populationCardId;
    private SimpleStringProperty selectedEntity;
    private final String SET_ENTITY_POPULATION= "set entity population";
    private final String SET_ENTITY_POPULATION_AGAIN="set entity population again";

    private ManageSystemToUi currSystemConnection;
    private Label dataLabel;
    private boolean isValidSet;
    private String lastValidValue;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedEntity = new SimpleStringProperty();
        entityNameLabel.textProperty().bind(selectedEntity);
        this.currSystemConnection=null;
        isValidSet=false;
    }

    @FXML
    void confirmPopulation(ActionEvent event) {
        String userInput = populationInput.getText().trim();
        try {
            int intValue = Integer.parseInt(userInput);
            if(intValue>=0) {
                StringAndIntDto populationDto = new StringAndIntDto(SET_ENTITY_POPULATION,intValue,selectedEntity.getValue());
                currSystemConnection.setUiDto(populationDto);
                PopulationUpdatesDto isValidDto = (PopulationUpdatesDto) currSystemConnection.executeInstruction();
                if(isValidDto.isInstructionSucceed()){
                    isValidSet=true;
                    lastValidValue=userInput;
                    dataLabel.setText("Entities left : " + isValidDto.getCurrDimension());
                    showInformationPopup(isValidDto.getStatus());
                }
                else{
                    showErrorPopup(isValidDto.getStatus());
                    if(!isValidSet){
                        populationInput.clear();}
                    else {populationInput.setText(lastValidValue);}
                }
            }
            else {
                showErrorPopup("Please enter not negative integer!");
                if(!isValidSet){
                    populationInput.clear();}
                else {populationInput.setText(lastValidValue);}
            }

        } catch (NumberFormatException e) {
            showErrorPopup("Please enter an integer!");
            if(!isValidSet){
                populationInput.clear();}
            else {populationInput.setText(lastValidValue);}
        } catch (InvalidAction | RangeException | JAXBException | InvalidValue | ValueNotExists | ValueExists |
                 FormatException e) {
            throw new RuntimeException(e);
        }
    }
    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        java.awt.Toolkit.getDefaultToolkit().beep();
        alert.showAndWait();
    }
    private void showInformationPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void updateEntityName(String entityName,ManageSystemToUi currSystemConnection,Label dataLabel){
        this.currSystemConnection=currSystemConnection;
        selectedEntity.set(entityName);
        this.dataLabel=dataLabel;
    }
    public void updateEntityNameAndSetAmount(String entityName,ManageSystemToUi currSystemConnection,Label dataLabel,String amount) throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException {
        this.currSystemConnection = currSystemConnection;
        this.selectedEntity.set(entityName);
        this.dataLabel = dataLabel;
        this.populationInput.setText(amount);
        int intValue = Integer.parseInt(amount);
        StringAndIntDto populationDto = new StringAndIntDto(SET_ENTITY_POPULATION_AGAIN, intValue, selectedEntity.getValue());
        currSystemConnection.setUiDto(populationDto);
        PopulationUpdatesDto isValidDto = (PopulationUpdatesDto) currSystemConnection.executeInstruction();
        if (isValidDto.isInstructionSucceed()) {
            isValidSet = true;
            lastValidValue = amount;
            dataLabel.setText("Entities left : " + isValidDto.getCurrDimension());
        }
    }
    public AnchorPane getPopulationCardId() {
        return populationCardId;
    }
}
