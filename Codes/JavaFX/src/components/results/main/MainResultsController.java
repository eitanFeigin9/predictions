package components.results.main;

import components.details.entity.population.PopulationInputController;
import components.details.manage.center.env.flowPaneController;
import components.execution.newExeUpdateEnvController;
import components.execution.utilities.ClearNextButtomsController;
import components.main.MainController;
import components.queue.QueueController;
import exception.*;
import input.dto.InstructionsDto;
import input.dto.intChoiceDto;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import simulation.queue.SimulationsQueueManage;
import system.file.ManageSystemToUi;
import system.file.instruction.entity.population.PopulationUpdatesDto;
import system.file.instruction.show.dto.EntitiesRunAgainDto;
import system.file.instruction.show.dto.EnviromentRunAgainDto;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import simulation.SimulationExecutionDetails;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainResultsController implements Initializable {
    @FXML
    private TableView<EntityData> EntitesPopulationId;
    @FXML
    private TableColumn<EntityData, String> entityNameColumn;
    @FXML
    private TableColumn<EntityData, Integer> amountColumn;
    @FXML
    private Label simNumber;

    @FXML
    private FlowPane SimulationFlowPane;

    @FXML
    private Label amountOfSeconds;

    @FXML
    private Label amountOfTicks;
    @FXML
    private AnchorPane mainPane;



    @FXML
    private Label menu;

    @FXML
    private Label menuBack;

    @FXML
    private Button pauseButton;

    @FXML
    private Button playButton;

    @FXML
    private Button runAgainId1;

    @FXML
    private Button simoulationProgressId1;

    @FXML
    private Button simulationRestultId1;

    @FXML
    private ScrollPane simulationScrollPane;

    @FXML
    private HBox simulationsHbox;

    @FXML
    private AnchorPane slider;
    @FXML
    private GridPane gridPane;


    @FXML
    private Button stopButton;
    private SimpleBooleanProperty isActiveSelected;
    private SimpleBooleanProperty isSimulationPaused;
    private SimpleBooleanProperty PausedButtonProp;
    private SimpleBooleanProperty endButtonProp;

    private Thread informationThread;
    private boolean isThreadRunning;
    private SimpleBooleanProperty isValidSimulation;
    private Set<Integer> endedSimulationArr;
    private Map<Integer, Boolean> isFirst=new LinkedHashMap<>();
    private Set<Integer> allSimulationsNumber= new LinkedHashSet<>();
    private SimpleStringProperty simulationStatus;
    private SimpleIntegerProperty currTick;
    private SimulationsQueueManage queueManager;
    private ManageSystemToUi currSystemConnection;
    private SimulationExecutionDetails currSimData;
    private QueueController queueController;
private BorderPane mainBorderPane;
private MainController mainController;
     private SimpleLongProperty secondsCounter;
    private int currSimulationId;
    private final String GET_AMOUNT_IN_SPECIFIC_DIMENSION = "get update specific dimension size";
    private final String SHOW_ENTITIES_RUN_AGAIN = "get entities data by run again";


    private final String SHOW_ENVIRONMENT_RUN_AGAIN = "get environment data by run again";

    private Map<Integer,SimulationExecutionDetails> simulationExecutionDetailsMap;
    public MainResultsController(){
        endedSimulationArr=new LinkedHashSet<>();
        secondsCounter = new SimpleLongProperty(0);
        currTick = new SimpleIntegerProperty(0);
        simulationStatus=new SimpleStringProperty();
        isValidSimulation = new SimpleBooleanProperty(false);
        isActiveSelected= new SimpleBooleanProperty(false);
        isSimulationPaused=new SimpleBooleanProperty(true);
        endButtonProp=new SimpleBooleanProperty(false);
        PausedButtonProp=new SimpleBooleanProperty(false);
        currSimulationId=-1;
        isThreadRunning=false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        amountOfSeconds.textProperty().bind(Bindings.format("%,d", secondsCounter));
        amountOfTicks.textProperty().bind(Bindings.format("%,d", currTick));
        simulationRestultId1.disableProperty().bind(isActiveSelected.not());
        runAgainId1.disableProperty().bind(isActiveSelected.not());
        pauseButton.disableProperty().bind(PausedButtonProp);
        playButton.disableProperty().bind(isSimulationPaused);
        stopButton.disableProperty().bind(endButtonProp);
        simNumber.disableProperty().bind(isActiveSelected.not());
        slider.setTranslateX(0);

        menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            // Slide the slider in from the left
            slide.setToX(0);
            slide.play();

            // Set the initial position of the slider to be outside the visible area again
            slider.setTranslateX(-250);

            slide.setOnFinished((ActionEvent e)-> {
                menu.setVisible(false);
                menuBack.setVisible(true);
            });
        });

        menuBack.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            // Slide the slider out to the left
            slide.setToX(-250);
            slide.play();

            // Set the initial position of the slider to be outside the visible area again
            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                menu.setVisible(true);
                menuBack.setVisible(false);
            });
        });

    }
    public void dynamicInitialize(ManageSystemToUi currSystemConnection, BorderPane mainBorderPane, MainController mainController,SimulationsQueueManage queueManager, QueueController queueController) {
        this.currSystemConnection=currSystemConnection;
        this.mainBorderPane=mainBorderPane;
        this.mainController=mainController;
        this.queueManager=queueManager;
        this.queueController=  queueController;
    }
    @FXML
    void EntitiesDataAction(ActionEvent event) {

    }

    @FXML
    void pauseMessage(MouseEvent event) {
        Tooltip tooltip = new Tooltip("In order to pause the simulation!");
        Tooltip.install(pauseButton, tooltip);

    }

    @FXML
    void pauseSimulation() {
        currSystemConnection.attractiveUserControl(this.getCurrSimulationId(),true,false,false);
        isSimulationPaused.set(false);
        PausedButtonProp.set(true);
        endButtonProp.set(false);

    }

    @FXML
    void resumeMessage(MouseEvent event) {
        Tooltip tooltip = new Tooltip("In order to resume the simulation!");
        Tooltip.install(playButton, tooltip);

    }


    @FXML
    void resumeSimulation() {
        currSystemConnection.attractiveUserControl(this.getCurrSimulationId(),false,true,false);
        isSimulationPaused.set(true);
        PausedButtonProp.set(false);
        endButtonProp.set(false);
        startListenThread();

    }

    @FXML
    void runAgainAction(ActionEvent event) throws IOException, InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException {
        intChoiceDto instructionsDto = new intChoiceDto(SHOW_ENVIRONMENT_RUN_AGAIN,this.currSimulationId);
        currSystemConnection.setUiDto(instructionsDto);
        EnviromentRunAgainDto environmentData=(EnviromentRunAgainDto) currSystemConnection.executeInstruction();
        FXMLLoader envDisplayLoader = new FXMLLoader(getClass().getResource("/components/details/manage/center/env/flowPaneFxml.fxml"));
        FlowPane envPane = envDisplayLoader.load();
        flowPaneController envPaneController = envDisplayLoader.getController();
        for (Map.Entry<String, Map<String, String>> entry : environmentData.getData().entrySet()) {
            String propertyName = entry.getKey();
            Map<String, String> propMap =  environmentData.getData().get(propertyName);
            try {
                FXMLLoader envCardLoader = new FXMLLoader(getClass().getResource("/components/execution/envInputFxml.fxml"));
                ScrollPane entityPane = envCardLoader.load();
                newExeUpdateEnvController envController = envCardLoader.getController();
                envController.setCurrSystemConnection(currSystemConnection);
                envController.createNewPropViewWithValues(propMap, propertyName,environmentData.getRes().get(propertyName));
                envPaneController.addNewFlowPaneElement(entityPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InstructionsDto instructionsDto2 = new InstructionsDto(SHOW_ENTITIES_RUN_AGAIN);
        currSystemConnection.setUiDto(instructionsDto2);
        EntitiesRunAgainDto entitesListDto =((EntitiesRunAgainDto) currSystemConnection.executeInstruction());
        List<String> entitiesNames=entitesListDto.getEntitiesNamesData();
        VBox leftVbox = new VBox();
        Label label = new Label();
        Font cooperBlackFont = Font.font("Cooper Black", FontWeight.BOLD, 22);
        label.setFont(cooperBlackFont);
        leftVbox.getChildren().add(label);
        leftVbox.setAlignment(javafx.geometry.Pos.CENTER);
        InstructionsDto populationDto = new InstructionsDto(GET_AMOUNT_IN_SPECIFIC_DIMENSION);
        currSystemConnection.setUiDto(populationDto);
        PopulationUpdatesDto amount = (PopulationUpdatesDto) currSystemConnection.executeInstruction();
        label.setText("Entities left : "+amount.getCurrDimension());
        for (String currEntityName:entitiesNames){
            FXMLLoader populationLoader = new FXMLLoader(getClass().getResource("/components/details/entity/population/populationInputFxml.fxml"));
            AnchorPane entityPane = populationLoader.load();
            PopulationInputController populationInputController = populationLoader.getController();
            populationInputController.updateEntityNameAndSetAmount(currEntityName,currSystemConnection,label,entitesListDto.getValuesMap().get(currEntityName));
            leftVbox.getChildren().add(entityPane);
        }
        this.mainBorderPane.setCenter(getScroll(envPane,240.0,600.0));
        this.mainBorderPane.setLeft(getScroll(leftVbox,100,300));
        FXMLLoader clearNextLoader = new FXMLLoader(getClass().getResource("/components/execution/utilities/ClearNextFxml.fxml"));
        AnchorPane utilityPane = clearNextLoader.load();
        ClearNextButtomsController clearNextButtomsController = clearNextLoader.getController();
        clearNextButtomsController.dynamicInitialize(this.currSystemConnection,this.mainBorderPane,mainController,null, "Show results",queueManager,queueController);
        this.mainBorderPane.setBottom(utilityPane);



    }
    public ScrollPane getScroll(Parent parent, double height, double width){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setPrefViewportHeight(height);
        scrollPane.setPrefViewportWidth(width);
        VBox.setVgrow(parent, javafx.scene.layout.Priority.ALWAYS);
        scrollPane.setContent(parent);
        return scrollPane;
    }
    @FXML
    void simoulationProgressAction(ActionEvent event) {}

    public SimulationsQueueManage getQueueManager() {
        return queueManager;
    }

    @FXML
    void simulationRestultAction(ActionEvent event) throws IOException, InvalidAction {
        FXMLLoader endSimulationsResults = new FXMLLoader(getClass().getResource("/components/results/main/EndSimulationsResultsFxml.fxml"));
        SplitPane pane = endSimulationsResults.load();
        ScrollPane scrollPane = new ScrollPane();
        EndSimulationsResultsController endSimulationsResultsController = endSimulationsResults.getController();
        endSimulationsResultsController.DynamicIntialize(this,mainBorderPane,mainPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setPrefViewportHeight(240.0);
        scrollPane.setPrefViewportWidth(600.0);
        VBox.setVgrow(pane, javafx.scene.layout.Priority.ALWAYS);
        scrollPane.setContent(pane);
        mainBorderPane.setCenter(scrollPane);
    }

    @FXML
    void stopMessage(MouseEvent event) {
        Tooltip tooltip = new Tooltip("In order to stop the simulation!");
        Tooltip.install(stopButton, tooltip);

    }

    @FXML
    void stopSimulation() {
        currSystemConnection.attractiveUserControl(this.getCurrSimulationId(),false,false,true);
        isSimulationPaused.set(true);
        PausedButtonProp.set(true);
        endButtonProp.set(true);
        startListenThread();

    }


    public void updateResults(){
        queueController.setData(queueManager);
        currTick.set(this.currSimData.getCurrTick());
        secondsCounter.set(this.currSimData.getSecondsCounter());
        buttonsManager();
        EntitesPopulationId.getItems().clear();
        Map<String, Integer> entitiesPopulationMap = this.currSimData.getEntitiesPopulation();
        setTable(entitiesPopulationMap);
}
    public void buttonsManager(){
        if(isFirst.get(currSimulationId)){
            isSimulationPaused.set(true);
            endButtonProp.set(false);
            PausedButtonProp.set(false);
            isFirst.put(currSimulationId,false);
        } else if (currSystemConnection.getRuningStatus(currSimulationId).equals("FINISH")) {
            isSimulationPaused.set(true);
            PausedButtonProp.set(true);
            endButtonProp.set(true);
        } else if(currSystemConnection.getRuningStatus(currSimulationId).equals("PAUSE")){
            pauseSimulation();
        }
       else if(currSystemConnection.getRuningStatus(currSimulationId).equals("STOP")){
            stopSimulation();
        }
       else {
            resumeSimulation();
        }
    }
   public void bindTaskToUIComponents(Map<Integer, SimulationExecutionDetails> simulationExecutionDetailsMap) {
       this.simulationExecutionDetailsMap = simulationExecutionDetailsMap;
       SimulationFlowPane.getChildren().clear();
       Map<Integer, String> simulationsStatus = getSimulationStatus(this.simulationExecutionDetailsMap);
       Button button;
       for (Map.Entry<Integer, String> currId : simulationsStatus.entrySet()) {
           if(!isFirst.containsKey(currId.getKey())){
               isFirst.put(currId.getKey(),true);
           }
           allSimulationsNumber.add(currId.getKey());
           if (currId.getValue().equals("active")) {
               button = new Button(currId.getKey() + " : " + currId.getValue());
               button.setStyle("-fx-font-family: 'Cooper Black'; -fx-font-size: 18; -fx-text-fill: yellow;");
               isActiveSelected.set(false);
           } else {
               isActiveSelected.set(true);
               button = new Button(currId.getKey() + " : " + currId.getValue());
               button.setStyle("-fx-font-family: 'Cooper Black'; -fx-font-size: 18; -fx-text-fill: green;");
           }
           //button.requestFocus();
           SimulationFlowPane.getChildren().add(button);
           Button finalButton = button;
           button.setOnAction(e -> {
               String buttonText = finalButton.getText();
               String[] parts = buttonText.split(" : ");
               this.currSimulationId = Integer.parseInt(parts[0].trim());
               buttonsManager();
               updateResults();

           });
           button.focusedProperty().addListener((obs, oldValue, newValue) -> {
               getButtonInt(finalButton);
               if(newValue){
                   startListenThread();
               }
               else {
                   shutListenThread();
               }

           });
           this.currSimulationId = currId.getKey();
           simNumber.setText("Simulation #" +this.currSimulationId);
           button.requestFocus();
           startListenThread();
       }

       currTick.set(this.simulationExecutionDetailsMap.get(this.currSimulationId).getCurrTick());
       secondsCounter.set(this.simulationExecutionDetailsMap.get(this.currSimulationId).getSecondsCounter());
       EntitesPopulationId.getItems().clear();

       Map<String, Integer> entitiesPopulationMap = this.simulationExecutionDetailsMap.get(this.currSimulationId).getEntitiesPopulation();
       setTable(entitiesPopulationMap);
   }
   public void getButtonInt(Button button){
       String buttonText = button.getText();
       String[] parts = buttonText.split(" : ");
       this.currSimulationId = Integer.parseInt(parts[0].trim());
       simNumber.setText("Simulation #" +this.currSimulationId);
   }
public void setTable(Map<String, Integer> entitiesAmountMap){
    entityNameColumn.setCellValueFactory(data -> data.getValue().entityNameProperty());
    amountColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEntityAmount()).asObject());

    Map<String, Integer> entitiesAmount = entitiesAmountMap;
    entitiesAmount.forEach((entityName, entityAmount) -> {
        EntitesPopulationId.getItems().add(new EntityData(entityName, entityAmount));
    });
}
    public Map<Integer,String> getSimulationStatus(Map<Integer,SimulationExecutionDetails> simulationsDetailsMap){
        Map<Integer,String>result=new LinkedHashMap<>();
        for (Map.Entry<Integer, SimulationExecutionDetails>currData:simulationsDetailsMap.entrySet()){
            if(currData.getValue().isRunning()){
                result.put(currData.getKey(),"active");
            }
            else {
                result.put(currData.getKey(),"finished");
            }
        }
        return result;
    }
    public Map<String,Integer> getEntitiesAmount(int simulationId){
        return simulationExecutionDetailsMap.get(simulationId).getEntitiesPopulation();
    }
    public Map<String, Map<Integer,Integer>> getEntitiesAmountPerTick(int simulationId) throws InvalidAction {
        if(simulationExecutionDetailsMap.get(simulationId).isRunning())
        {
            throw new InvalidAction("This simulation is still running");
        }
        return simulationExecutionDetailsMap.get(simulationId).getEntitiesPopulationByTicks();
    }
    public  Map<String, Map<String,Map<String,Integer>>> getHistogram(int simulationId){
        return simulationExecutionDetailsMap.get(simulationId).getPropertiesHistogram();
    }
    public Map<String,Map<String,Float>>  getConsistency(int simulationId){
        return simulationExecutionDetailsMap.get(simulationId).getConsistency();
    }
    public  Map<String,Map<String,Float>>  getAvg(int simulationId){
        return simulationExecutionDetailsMap.get(simulationId).getNumericPropertyAverage();
    }

    public void startListenThread()  {
        if (!isThreadRunning) {
            isThreadRunning = true;
            informationThread = new Thread(() -> {
                while (isThreadRunning) {
                        currSimData =  (  currSystemConnection.getSimulationData()).getSimulationExeData(this.currSimulationId);
                        Platform.runLater(() -> {
                            try {
                                updateAlways();
                                if(!currSimData.isRunning()){
                                    isThreadRunning=false;
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            if(!currSimData.isRunning()){
                                isThreadRunning=false;

                            }                        });



                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Reset the interrupted status
                        break;
                    }
                }
            });
            informationThread.start();
        }
    }


public void updateAlways() {
    updateResults();
    for(int currSim : allSimulationsNumber){
        SimulationExecutionDetails executionDetails = (currSystemConnection.getSimulationData()).getSimulationExeData(currSim);
        if(!executionDetails.isRunning()){
            if(!endedSimulationArr.isEmpty()) {
                if(!endedSimulationArr.contains(currSim)){
                    endedSimulationArr.add(currSim);
                    if( executionDetails.getResult().getValid()){
                        showInformationPopup( executionDetails.getResult().getStatus());
                    }
                    else {
                        showErrorPopup( executionDetails.getResult().getStatus());
                    }
                }
            }
            else {
                endedSimulationArr.add(currSim);
                if( executionDetails.getResult().getValid()){
                    showInformationPopup( executionDetails.getResult().getStatus());
                }
                else {
                    showErrorPopup( executionDetails.getResult().getStatus());
                }
            }
            changeButtonColor(currSim,executionDetails);
        }
        }


    }
public void changeButtonColor(int simId,SimulationExecutionDetails currData) {
    for (Node node : SimulationFlowPane.getChildren()) {
        if (node instanceof Button) {
            Button button = (Button) node;
            if (button.getText() != null && button.getText().contains(String.valueOf(simId))) {
                isActiveSelected.set(true);
                if (currData.getResult().getValid()) {
                    button.setText(simId + " : " + "finished");
                    button.setStyle("-fx-font-family: 'Cooper Black'; -fx-font-size: 18; -fx-text-fill: green;");
                } else {
                    button.setText(simId + " : " + "failed");
                    button.setStyle("-fx-font-family: 'Cooper Black'; -fx-font-size: 18; -fx-text-fill: red;");
                }
                break;
            }
        }
    }
}
    public void shutListenThread(){
        isThreadRunning=false;
        if(informationThread!=null){
            informationThread.interrupt();
        }
    }
public void clean(){
    shutListenThread();
    entityNameColumn.getColumns().clear();
   amountColumn.getColumns().clear();
   EntitesPopulationId.getItems().clear();
    EntitesPopulationId.getSelectionModel().clearSelection();
    SimulationFlowPane.getChildren().clear();
    mainBorderPane.setCenter(null);
    mainBorderPane.setLeft(null);
    mainBorderPane.setRight(null);
    mainBorderPane.setBottom(null);

}
        private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        java.awt.Toolkit.getDefaultToolkit().beep();
        alert.showAndWait();
    }
    public void showInformationPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public AnchorPane getMainPane(){
        return mainPane;
    }

    public int getCurrSimulationId() {
        return currSimulationId;
    }
}
