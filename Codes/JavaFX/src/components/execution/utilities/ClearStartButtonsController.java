package components.execution.utilities;

import components.main.MainController;
import components.queue.QueueController;
import components.results.main.MainResultsController;
import exception.*;
import input.dto.InstructionsDto;
import simulation.queue.SimulationsQueueManage;
import system.file.ManageSystemToUi;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class ClearStartButtonsController {
    private ManageSystemToUi currSystemConnection=null;
    private final String CLEAR_POPULATION_INPUT = "clear population input";
    private final String SIMULATION_START = "simulation start";
    private SimulationsQueueManage queueManager;
    private QueueController queueController;

    private final String CLEAR_ENVIRONMENT_PROPERTIES_INPUT = "clear env's props input";
    private final String SET_ENVIRONMENT_PROPERTIES_VALUES = "set env values";
    private final String SHOW_ENVIRONMENT_DATA = "show environment data";
    private final String SHOW_ENVIRONMENT_VALUES_DATA = "show environment values data";
    private final  String GET_SIMULATIONS_DATA= "get simulations data";
    private String currButtonName;
    private SimpleBooleanProperty isSimulationStart;

    private BorderPane borderPane = null;
    private MainController mainController;
    private  Parent parent;


    @FXML
    private ImageView clearButtom;

    @FXML
    private ImageView startButton;

    @FXML
    void clearClicked(MouseEvent event) throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException, IOException {
        InstructionsDto instructionsDto = new InstructionsDto(CLEAR_ENVIRONMENT_PROPERTIES_INPUT);
        currSystemConnection.setUiDto(instructionsDto);
        currSystemConnection.executeInstruction();
        instructionsDto = new InstructionsDto(CLEAR_POPULATION_INPUT);
        currSystemConnection.setUiDto(instructionsDto);
        currSystemConnection.executeInstruction();
        try {
            mainController.startExecutionHandler(queueManager);
        } catch (IOException | InvalidAction | RangeException | JAXBException | InvalidValue | ValueNotExists | ValueExists | FormatException e) {
            showErrorPopup(e.getMessage());
        }

    }

    @FXML
    void startClicked(MouseEvent event) throws IOException {
            if(mainController.getResultsController()==null)
            {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/results/main/mainResultsFxml.fxml"));
                AnchorPane anchorPaneResults=loader.load();
                MainResultsController resultsMainController = loader.getController();
                resultsMainController.dynamicInitialize(currSystemConnection,borderPane,mainController,queueManager,  queueController);
                currButtonName="Show results";
                this.parent=anchorPaneResults;
                mainController.setResultsController(resultsMainController);
            }
            else {

                this.parent=  mainController.getResultsController().getMainPane();
            }
            currSystemConnection.runSimulation(mainController.getResultsController());

          //  mainController.getResultsController().dynamicInitialize(currSystemConnection,borderPane,mainController);
            borderPane.setLeft(null);
            if(isSimulationStart!=null)
        isSimulationStart.set(true);
        borderPane.setBottom(null);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setPrefViewportHeight(240.0);
        scrollPane.setPrefViewportWidth(600.0);
        VBox.setVgrow(parent, javafx.scene.layout.Priority.ALWAYS);
        scrollPane.setContent(parent);
        borderPane.setCenter(scrollPane);
    }
    public void dynamicInitialize(ManageSystemToUi currSystemConnection, BorderPane main, MainController mainController,SimpleBooleanProperty isSimulationStart,String currButtonName,SimulationsQueueManage queueManager, QueueController queueController){
        this.currSystemConnection=currSystemConnection;
        this.borderPane=main;
        this.mainController=mainController;
        this.isSimulationStart=isSimulationStart;
        this.currButtonName=currButtonName;
        this.queueManager=queueManager;
        this.queueController=  queueController;
    }


    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        java.awt.Toolkit.getDefaultToolkit().beep();
        // Show the pop-up and wait for the user to close it
        alert.showAndWait();
    }
    private void showInformationPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Show the pop-up and wait for the user to close it
        alert.showAndWait();
    }
}
