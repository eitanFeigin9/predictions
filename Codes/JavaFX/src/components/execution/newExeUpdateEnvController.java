package components.execution;
import exception.*;
import input.dto.envInputDto;
import system.file.ManageSystemToUi;
import system.file.instruction.valid.dto.IsValidDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class newExeUpdateEnvController implements Initializable {

    @FXML
    private VBox PropVbox;

    @FXML
    private Label propNameLabel;

    @FXML
    private ScrollPane scrolPaneCard;
    @FXML
    private Button saveButtonId;


    @FXML
    private TextField userInputValueTextLabel;

    @FXML
    private ImageView saveImageID;
    private boolean isValidSet;
    private String lastValidValue;



    @FXML
    void showUse(MouseEvent event) {
        Tooltip tooltip = new Tooltip("Save your input details!");

        // Attach the Tooltip to the button
        Tooltip.install(saveButtonId, tooltip);
    }

    @FXML
    void userInputValueKeyPress(ActionEvent event) throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException {
        if (userInputValueTextLabel != null) {
            String userInput = userInputValueTextLabel.getText().trim();
            envInputDto envInputDto = new envInputDto(SET_ENVIRONMENT_VALUE, selectedProperty.getValue(), userInput);
            currSystemConnection.setUiDto(envInputDto);
            IsValidDto isValidDto = (IsValidDto) currSystemConnection.executeInstruction();
            if (isValidDto.getValid()) {
                isValidSet=true;
                lastValidValue=userInput;

                showInformationPopup(isValidDto.getStatus());
            } else {
                // If the input is invalid, show an error notification or handle it accordingly.
                showErrorPopup(isValidDto.getStatus());
                if(!isValidSet){
                    userInputValueTextLabel.clear();}
                else {userInputValueTextLabel.setText(lastValidValue);}
                // Optionally, you can clear the TextField
            }
        }
    }


    private SimpleStringProperty selectedProperty;
    private ManageSystemToUi currSystemConnection;
    private final String SET_ENVIRONMENT_VALUE = "set environment's property value";
    private String type;

   /* private void userInputValueAction( ) throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException {
        String userInput = userInputValueTextLabel.getText().trim();
        envInputDto envInputDto = new envInputDto(SET_ENVIRONMENT_VALUE,selectedProperty.getName(),userInput);
        currSystemConnection.setUiDto(envInputDto);
        IsValidDto isValidDto =(IsValidDto)currSystemConnection.executeInstruction();
        if(isValidDto.getValid())
        {
            showInformationPopup(isValidDto.getStatus());
        }
         else {
            // If the input is invalid, show an error notification or handle it accordingly.
            showErrorPopup(isValidDto.getStatus());
            // Optionally, you can clear the TextField
            userInputValueTextLabel.clear();
        }
    }

    */

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


        @Override
        public void initialize (URL location, ResourceBundle resources){
            selectedProperty = new SimpleStringProperty();
            propNameLabel.textProperty().bind(selectedProperty);
            this.currSystemConnection=null;

        }
        public ScrollPane createNewPropView (Map < String, String > entityProps, String entPropName){
            Font labelFont = new Font("Cooper Black", 25);
            this.type = entityProps.get("Type: ");
            for (Map.Entry<String, String> entry : entityProps.entrySet()) {
                Label currLabel = new Label(entry.getKey() + entry.getValue());
                currLabel.setFont(labelFont);
                currLabel.setAlignment(Pos.CENTER);
                PropVbox.getChildren().add(currLabel);
            }
            selectedProperty.set(entPropName);
            return scrolPaneCard;
        }
    public ScrollPane createNewPropViewWithValues (Map < String, String > entityProps, String entPropName,String value) throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException {
        Font labelFont = new Font("Cooper Black", 25);
        this.type = entityProps.get("Type: ");
        for (Map.Entry<String, String> entry : entityProps.entrySet()) {
            Label currLabel = new Label(entry.getKey() + entry.getValue());
            currLabel.setFont(labelFont);
            currLabel.setAlignment(Pos.CENTER);
            PropVbox.getChildren().add(currLabel);
        }
        selectedProperty.set(entPropName);
        envInputDto envInputDto = new envInputDto(SET_ENVIRONMENT_VALUE, selectedProperty.getValue(), value);
        currSystemConnection.setUiDto(envInputDto);
        IsValidDto isValidDto = (IsValidDto) currSystemConnection.executeInstruction();
        userInputValueTextLabel.setText(value);
        isValidSet=true;
        lastValidValue=value;
        return scrolPaneCard;
    }
    public void setCurrSystemConnection(ManageSystemToUi currSystemConnection) {
        this.currSystemConnection = currSystemConnection;
    }

    }



