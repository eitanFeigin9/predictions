package components.execution.utilities;

import components.details.entity.EntityController;
import components.details.entity.population.value.PopulationValueController;
import components.main.MainController;
import components.queue.QueueController;
import exception.*;
import input.dto.InstructionsDto;
import simulation.queue.SimulationsQueueManage;
import system.file.ManageSystemToUi;
import system.file.instruction.entity.population.EntitesPopulationValueDto;
import system.file.instruction.show.dto.EnvironmentDto;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Map;

public class ClearNextButtomsController {

    @FXML
    private ImageView cleanButton;

    @FXML
    private ImageView nextButton;
    private QueueController queueController;
    private ManageSystemToUi currSystemConnection = null;
    private SimpleBooleanProperty  isSimulationStart;
    private SimulationsQueueManage queueManager;
    private final String CLEAR_POPULATION_INPUT = "clear population input";

    private final String CLEAR_ENVIRONMENT_PROPERTIES_INPUT = "clear env's props input";
    private final String SET_ENVIRONMENT_PROPERTIES_VALUES = "set env values";
    private final String SHOW_ENVIRONMENT_DATA = "show environment data";
    private final String SHOW_ENVIRONMENT_VALUES_DATA = "show environment values data";
    private final String GET_ENTITIES_POPULATION = "get entities populations values";
private String currButtonName;
    private BorderPane borderPane = null;
    private MainController mainController;

    @FXML
    void clearClick(MouseEvent event) throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException, IOException {
        InstructionsDto instructionsDto = new InstructionsDto(CLEAR_ENVIRONMENT_PROPERTIES_INPUT);
        currSystemConnection.setUiDto(instructionsDto);
        currSystemConnection.executeInstruction();
        instructionsDto = new InstructionsDto(CLEAR_POPULATION_INPUT);
        currSystemConnection.setUiDto(instructionsDto);
        currSystemConnection.executeInstruction();
        try {
            mainController.startExecutionHandler(queueManager);
        } catch (IOException | InvalidAction | RangeException | JAXBException | InvalidValue | ValueNotExists | ValueExists | FormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void nextButtonClick(MouseEvent event) throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException, IOException {

        InstructionsDto instructionsDto = new InstructionsDto(SET_ENVIRONMENT_PROPERTIES_VALUES);
        currSystemConnection.setUiDto(instructionsDto);
        currSystemConnection.executeInstruction();
        FlowPane res = new FlowPane();
        instructionsDto.setInstruction(SHOW_ENVIRONMENT_VALUES_DATA);
        currSystemConnection.setUiDto(instructionsDto);
        currSystemConnection.executeInstruction();
        EnvironmentDto environmentData = (EnvironmentDto) currSystemConnection.executeInstruction();
        for (Map.Entry<String, Map<String, String>> entry : environmentData.getenvironmentMapDto().entrySet()) {
            String propertyName = entry.getKey();
            Map<String, String> propMap = environmentData.getenvironmentMapDto().get(propertyName);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/entity/entityFxml.fxml"));
                ScrollPane entityPane = loader.load();
                EntityController entityController = loader.getController();
                ScrollPane currCard = entityController.createNewEntityView(propMap, null, propertyName);
                res.getChildren().add(currCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setPrefViewportHeight(240.0);
        scrollPane.setPrefViewportWidth(600.0);
        VBox.setVgrow(res, javafx.scene.layout.Priority.ALWAYS);
        scrollPane.setContent(res);
        borderPane.setCenter(scrollPane);

        FXMLLoader clearStartLoader = new FXMLLoader(getClass().getResource("/components/execution/utilities/clearStartFxml.fxml"));
        AnchorPane clearStartPane = clearStartLoader.load();
        ClearStartButtonsController clearStartButtonsController = clearStartLoader.getController();
        clearStartButtonsController.dynamicInitialize(currSystemConnection, borderPane, mainController, isSimulationStart, currButtonName,queueManager,  queueController);
        borderPane.setBottom(clearStartPane);

        instructionsDto.setInstruction(GET_ENTITIES_POPULATION);
        FlowPane populationFlowPane = new FlowPane();

        currSystemConnection.setUiDto(instructionsDto);
        Map<String, Integer> entitesPopulationValueMap = ((EntitesPopulationValueDto) currSystemConnection.executeInstruction()).getEntitesPopulationValuesMap();
        for (Map.Entry<String, Integer> currEntity : entitesPopulationValueMap.entrySet()) {
            try {
                FXMLLoader populationCardLoader = new FXMLLoader(getClass().getResource("/components/details/entity/population/value/entityValueFxml.fxml"));
                AnchorPane populationPane = populationCardLoader.load();
                PopulationValueController populationValueController = populationCardLoader.getController();
                populationValueController.updateEntityName(currEntity.getKey(), currEntity.getValue());
                ScrollPane currCardScroller = new ScrollPane();
                currCardScroller.setContent(populationPane);
                populationFlowPane.getChildren().add(currCardScroller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ScrollPane leftScrollPane = new ScrollPane();
        leftScrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        leftScrollPane.setPannable(true);
        leftScrollPane.setPrefViewportHeight(240.0);
        leftScrollPane.setPrefViewportWidth(300.0);
        VBox.setVgrow(populationFlowPane, javafx.scene.layout.Priority.ALWAYS);
        leftScrollPane.setContent(populationFlowPane);
        borderPane.setLeft(leftScrollPane);
    }

    public void dynamicInitialize(ManageSystemToUi currSystemConnection, BorderPane main, MainController mainController, SimpleBooleanProperty isSimulationStart, String currButtonName, SimulationsQueueManage queueManager, QueueController queueController) {
        this.currSystemConnection = currSystemConnection;
        this.borderPane = main;
        this.mainController = mainController;
        this.isSimulationStart =isSimulationStart;
        this.currButtonName= currButtonName;
        this.queueManager=queueManager;
        this.queueController=  queueController;
    }
}
