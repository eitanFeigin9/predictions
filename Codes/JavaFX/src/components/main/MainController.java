package components.main;
import components.details.entity.population.PopulationInputController;
import components.details.manage.center.env.flowPaneController;
import components.details.tree.TreeController;
import components.execution.newExeUpdateEnvController;
import components.execution.utilities.ClearNextButtomsController;
import components.queue.QueueController;
import components.results.main.MainResultsController;
import exception.*;
import input.dto.FileDto;
import input.dto.InstructionsDto;
import simulation.queue.SimulationsQueueManage;
import system.file.ManageSystemToUi;
import system.file.dto.ManageDto;
import system.file.instruction.entity.EntitesListDto;
import system.file.instruction.entity.population.PopulationUpdatesDto;
import system.file.instruction.show.dto.EntitiesDto;
import system.file.instruction.show.dto.EnvironmentDto;
import system.file.instruction.show.dto.RulesDto;
import system.file.instruction.show.dto.TerminationDto;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class MainController {



        @FXML
        private Button detailsButton;

        @FXML
        private ImageView exitButton;

        @FXML
        private Label fileNameLabel;

        @FXML
        private Button loadButton;

        @FXML
        private Button newExeButton;

        @FXML
        private Button queueButton;

        @FXML
        private Button resultsButton;
        @FXML
        private BorderPane mainBorderPane;

        @FXML
        private FlowPane flowPaneId;

        @FXML
        private AnchorPane anchorPaneId;
        private MainResultsController resultsController;
    private SimulationsQueueManage queueManager;

    private final String GET_AMOUNT_IN_DIMENSION = "get update dimension size";

private TreeController treeControllerManage;

        private SimpleBooleanProperty isFileSelected;
        private SimpleBooleanProperty isSimulationStart;
        private boolean isFirstFile;
        private String currButtonName=null;
        private   QueueController queueController=null;
        private SimpleStringProperty selectedFileProperty;
        private Stage primaryStage;
        private ManageSystemToUi currSystemConnection;
        private String absolutePath;

        private final String CREATE_WORLD = "create world";
        private final boolean FIRST_SIMULATION = true;
        private final String SHOW_ENTITIES_DATA = "show entities data";
        private final String SHOW_ENVIRONMENT_DATA = "show environment data";
        private final String GET_ENTITIES_NAMES= "get entities names";
        private final String CLEAR_POPULATION_INPUT = "clear population input";
    private final String SHOW_RULES_DATA = "get rules data";
    private final String GET_TERMINATION ="get termination";



    private final String CLEAR_ENVIRONMENT_PROPERTIES_INPUT = "clear env's props input";





        public MainController() {
                isFirstFile=FIRST_SIMULATION;
                this.queueManager=new SimulationsQueueManage();
                selectedFileProperty = new SimpleStringProperty();
                isFileSelected = new SimpleBooleanProperty(false);
                isSimulationStart= new SimpleBooleanProperty(false);
        }
        @FXML
        private void initialize() {
                detailsButton.disableProperty().bind(isFileSelected.not());
                resultsButton.disableProperty().bind(isSimulationStart.not());
                newExeButton.disableProperty().bind(isFileSelected.not());
                queueButton.disableProperty().bind(isFileSelected.not());
                fileNameLabel.textProperty().bind(selectedFileProperty);
        }
        public void setPrimaryStage(Stage primaryStage) {
                this.primaryStage = primaryStage;
        }
        public void setCurrSystemConnection(ManageSystemToUi currSystemConnection) {
                this.currSystemConnection = currSystemConnection;
        }

        @FXML
        void QueueManagw(ActionEvent event) throws IOException {
            currButtonName="QueueManager";
            handleQueue();
        }


        @FXML
        void exitProgram(MouseEvent event) {
                showInformationPopup("Thanks for using our program!");
                exit(1);
        }

        @FXML
        void loadNewFile() throws IOException {
           // this.queueManager=new SimulationsQueueManage();
          //  FXMLLoader queue = new FXMLLoader(getClass().getResource("/components/queue/queueFxml.fxml"));
            //AnchorPane queuePane = queue.load();
           // QueueController queueController = queue.getController();
           // queueController.setData(queueManager);
         //   this.queueController=queueController;
                ManageSystemToUi systemConnection;
                ManageDto systemResponse;
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select predictions file");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if(selectedFile!=null){
                     //selectedFile = fileChooser.showOpenDialog(primaryStage);

                String absolutePath = selectedFile.getAbsolutePath();
                FileDto fileDto = new FileDto(CREATE_WORLD, absolutePath);
                systemConnection = new ManageSystemToUi(fileDto);
                systemConnection.setUiDto(fileDto);
                try {
                        assert systemConnection != null;
                        systemResponse = systemConnection.executeInstruction();
                        if (systemResponse.isInstructionSucceed()) {
                            this.queueManager=new SimulationsQueueManage();
                            FXMLLoader queue = new FXMLLoader(getClass().getResource("/components/queue/queueFxml.fxml"));
                            AnchorPane queuePane = queue.load();
                            QueueController queueController = queue.getController();
                            queueController.setData(queueManager);
                            this.queueController=queueController;
                            showInformationPopup("The file: " + absolutePath + " has been successfully loaded");
                                this.selectedFileProperty.set(absolutePath);
                                this.isFileSelected.set(true);
                                this.isFirstFile=false;
                                this.currSystemConnection=systemConnection;
                                this.absolutePath=absolutePath;
                            updateData();

                            isSimulationStart.set(false);
                        }


                }
                catch (InvalidAction | RangeException | FormatException | ValueExists | ValueNotExists | InvalidValue |
                       JAXBException e) {
                        if(isFirstFile==FIRST_SIMULATION) {
                                showErrorPopup("An error has occurred!\n" + e.getMessage() + "\nPlease try again!");
                                return;

                        }
                        else{
                                showErrorPopup("An error has occurred!\n" + e.getMessage()+"\nThe last file that you entered is now represent the world!");
                                return;
                        }
                }
                }

        }
        private void showErrorPopup(String message) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(message);
                java.awt.Toolkit.getDefaultToolkit().beep();
                alert.showAndWait();
        }
        private void showInformationPopup(String message) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }

        @FXML
        void newExcetution(ActionEvent event) throws IOException, InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException {
            currButtonName="new Execution";

                startExecutionHandler(this.queueManager);



        }

        @FXML
        void showDetails(ActionEvent event) {
            showDetailsHandler();
        }

        @FXML
        void showResults(ActionEvent event) throws IOException {
            currButtonName="Show results";
            handleResults();


        }
        public void handleResults(){mainBorderPane.setCenter(resultsController.getMainPane());
            mainBorderPane.setLeft(null);
            mainBorderPane.setBottom(null);
            mainBorderPane.setRight(null);
        }




    public void startExecutionHandler(SimulationsQueueManage queueManager) throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException, IOException {

        FileDto fileDto = new FileDto(CREATE_WORLD, absolutePath);
               currSystemConnection.setUiDto(fileDto);
               currSystemConnection.executeInstruction();

               InstructionsDto instructionsDto = new InstructionsDto(CLEAR_ENVIRONMENT_PROPERTIES_INPUT);
                currSystemConnection.setUiDto(instructionsDto);
                currSystemConnection.executeInstruction();
                instructionsDto = new InstructionsDto(CLEAR_POPULATION_INPUT);
                currSystemConnection.setUiDto(instructionsDto);
                currSystemConnection.executeInstruction();
                ScrollPane leftScrollPane = new ScrollPane();
                instructionsDto = new InstructionsDto(SHOW_ENVIRONMENT_DATA);
                currSystemConnection.setUiDto(instructionsDto);
                EnvironmentDto environmentData=(EnvironmentDto) currSystemConnection.executeInstruction();
                FXMLLoader envDisplayLoader = new FXMLLoader(getClass().getResource("/components/details/manage/center/env/flowPaneFxml.fxml"));
                FlowPane envPane = envDisplayLoader.load();
                flowPaneController envPaneController = envDisplayLoader.getController();
                for (Map.Entry<String, Map<String, String>> entry : environmentData.getenvironmentMapDto().entrySet()) {
                        String propertyName = entry.getKey();
                        Map<String, String> propMap = environmentData.getenvironmentMapDto().get(propertyName);
                        try {
                                FXMLLoader envCardLoader = new FXMLLoader(getClass().getResource("/components/execution/envInputFxml.fxml"));
                                ScrollPane entityPane = envCardLoader.load();
                                newExeUpdateEnvController envController = envCardLoader.getController();
                                envController.setCurrSystemConnection(currSystemConnection);
                                ScrollPane currCard = envController.createNewPropView(propMap, propertyName);
                                envPaneController.addNewFlowPaneElement(currCard);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                instructionsDto = new InstructionsDto(GET_ENTITIES_NAMES);
                currSystemConnection.setUiDto(instructionsDto);
                List<String> entitiesNames=((EntitesListDto) currSystemConnection.executeInstruction()).getEntitiesNames();
                VBox leftVbox = new VBox();
                Label label = new Label();
                Font cooperBlackFont = Font.font("Cooper Black", FontWeight.BOLD, 22);
                 label.setFont(cooperBlackFont);
                  leftVbox.getChildren().add(label);
                    leftVbox.setAlignment(javafx.geometry.Pos.CENTER);
                    InstructionsDto populationDto = new InstructionsDto(GET_AMOUNT_IN_DIMENSION);
                      currSystemConnection.setUiDto(populationDto);
                PopulationUpdatesDto amount = (PopulationUpdatesDto) currSystemConnection.executeInstruction();
                  label.setText("Entities left : "+amount.getCurrDimension());
                for (String currEntityName:entitiesNames){
                        FXMLLoader populationLoader = new FXMLLoader(getClass().getResource("/components/details/entity/population/populationInputFxml.fxml"));
                        AnchorPane entityPane = populationLoader.load();
                        PopulationInputController populationInputController = populationLoader.getController();
                        populationInputController.updateEntityName(currEntityName,currSystemConnection,label);
                        leftVbox.getChildren().add(populationInputController.getPopulationCardId());
                }
        this.mainBorderPane.setCenter(getScroll(envPane,240.0,600.0));
        this.mainBorderPane.setLeft(getScroll(leftVbox,100,300));
                FXMLLoader clearNextLoader = new FXMLLoader(getClass().getResource("/components/execution/utilities/ClearNextFxml.fxml"));
                AnchorPane utilityPane = clearNextLoader.load();
                ClearNextButtomsController clearNextButtomsController = clearNextLoader.getController();
                clearNextButtomsController.dynamicInitialize(this.currSystemConnection,this.mainBorderPane,this,isSimulationStart, currButtonName,queueManager,queueController);
                this.mainBorderPane.setBottom(utilityPane);

        }
    public void setResultsController(MainResultsController resultsController) {
        this.resultsController = resultsController;
        this.currButtonName="Show results";
    }
    public ScrollPane getScroll(Parent parent,double height,double width){
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
    public void showDetailsHandler(){
    currButtonName="Show details";
    this.mainBorderPane.setBottom(null);
    this.mainBorderPane.setCenter(null);
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/treeFxml.fxml"));
        Parent treeViewRoot = loader.load();

        TreeController treeController = loader.getController();
        InstructionsDto instructionsDto = new InstructionsDto(SHOW_ENTITIES_DATA);
        currSystemConnection.setUiDto(instructionsDto);
        EntitiesDto entitiesData=(EntitiesDto) currSystemConnection.executeInstruction();
        instructionsDto.setInstruction(SHOW_ENVIRONMENT_DATA);
        currSystemConnection.setUiDto(instructionsDto);
        EnvironmentDto environmentData=(EnvironmentDto) currSystemConnection.executeInstruction();
        instructionsDto.setInstruction(SHOW_RULES_DATA);
        currSystemConnection.setUiDto(instructionsDto);
        RulesDto rulesDto=(RulesDto)currSystemConnection.executeInstruction();
        instructionsDto.setInstruction(GET_TERMINATION);
        currSystemConnection.setUiDto(instructionsDto);
        TerminationDto terminationDto=(TerminationDto)currSystemConnection.executeInstruction();

        treeController.initializeDynamicData(entitiesData,this.mainBorderPane,environmentData,rulesDto,terminationDto);
        treeControllerManage=treeController;

        mainBorderPane.setLeft(treeViewRoot);
    } catch (IOException e) {
        e.printStackTrace();
    } catch (InvalidAction | FormatException | ValueExists | ValueNotExists | InvalidValue | JAXBException |
             RangeException e) {
        throw new RuntimeException(e);
    }
}
    public MainResultsController getResultsController() {
        return resultsController;
    }

    public void handleQueue() throws IOException {
        FXMLLoader queue = new FXMLLoader(getClass().getResource("/components/queue/queueFxml.fxml"));
        AnchorPane queuePane = queue.load();
        QueueController queueController = queue.getController();
        queueController.setData(queueManager);
        this.queueController=queueController;
        mainBorderPane.setCenter(queuePane);
    }

    public void updateData() throws InvalidAction, RangeException, JAXBException, InvalidValue, IOException, ValueNotExists, ValueExists, FormatException {
            if(currButtonName!=null){
                switch (currButtonName) {
                    case ("Show results"):
                    {
                        resultsController.clean();
                        mainBorderPane.setBottom(null);
                        mainBorderPane.setRight(null);
                        mainBorderPane.setLeft(null);
                        mainBorderPane.setCenter(null);
                        if(resultsController!=null){
                            resultsController.clean();
                            resultsController=null;
                        }
                        break;
                    }
                    case ("Show details"):
                    {
                        if(resultsController!=null){
                            resultsController.clean();
                            resultsController=null;

                        }
                        showDetailsHandler();
                        break;
                    }
                    case ("new Execution"):
                    {
                        if(resultsController!=null){
                            resultsController.clean();
                            resultsController=null;

                        }
                        startExecutionHandler(queueManager);
                        break;
                    }
                    case ("QueueManager"):
                    {
                        if(resultsController!=null){
                            resultsController.clean();
                            resultsController=null;

                        }
                        this.queueController=null;
                        handleQueue();
                        break;
                    }
                }

            }
}
}


