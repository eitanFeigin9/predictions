package components.results.main;

import exception.InvalidAction;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.util.Map;

public class EndSimulationsResultsController {

    @FXML
    private Label SimulationID;

    @FXML
    private Label SimulationID2;

    @FXML
    private ScrollPane graphScroolPane;
  /*  @FXML
    private TableColumn<String, Integer> amountColumn;
    @FXML
    private TableColumn<String, Integer> valueColumn;
    @FXML
    private TableView<Map.Entry<String, Integer>> tableView;

   */
  @FXML
  private TableView<EntityData> tableView;
    @FXML
    private TableColumn<EntityData, String> valueColumn;
    @FXML
    private TableColumn<EntityData, Integer> amountColumn;

    @FXML
    private FlowPane entitiesFlowPane;


    @FXML
    private FlowPane propertiresFlowPane;
    @FXML
    private Button backButton;

    @FXML
    private Label consistencyLabel;


    @FXML
    private LineChart<Number, Number> entitiesGraph;

    @FXML
    private Label propertyAvg;

    @FXML
    private Label propertyAvg1;

    @FXML
    private Label avgValue;

    private int simulationId;
    private MainResultsController resultsController;
    private String selectedEntity;
    private BorderPane mainBorderPane;

    private AnchorPane backPane;
    private String selectedProperty;
    private SimpleBooleanProperty isNumericProperty;
    private SimpleBooleanProperty isPropertySelected;

    private SimpleIntegerProperty currSimulationId;
    private SimpleFloatProperty consistencyValue;
    private SimpleFloatProperty avg;


    public void DynamicIntialize(MainResultsController resultsController,BorderPane mainBorderPane,AnchorPane scrollPane) throws InvalidAction {
        this.resultsController = resultsController;
        this.mainBorderPane=mainBorderPane;
        this.simulationId = this.resultsController.getCurrSimulationId();
        currSimulationId=new SimpleIntegerProperty(  this.simulationId);
        isNumericProperty = new SimpleBooleanProperty(false);
        isPropertySelected=new SimpleBooleanProperty(false);
        propertyAvg.visibleProperty().bind(isNumericProperty);
        propertyAvg1.visibleProperty().bind(isNumericProperty);
        avgValue.visibleProperty().bind(isNumericProperty);
        consistencyLabel.visibleProperty().bind(isPropertySelected);
        consistencyValue = new SimpleFloatProperty();
        avg = new SimpleFloatProperty();
        this.backPane=scrollPane;
        avgValue.textProperty().bind(Bindings.format("%.2f", avg));
        consistencyLabel.textProperty().bind(Bindings.format("%.2f", consistencyValue));
        SimulationID.textProperty().bind(Bindings.format("%d", currSimulationId));
        SimulationID2.textProperty().bind(Bindings.format("%d", currSimulationId));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("Value"));

        setEntitiesGraph();
        setEntitiesButtons();

    }

    @FXML
    void BackResultsMessege(MouseEvent event) {
        Tooltip tooltip = new Tooltip("In order to back to the results menu!");
        Tooltip.install(backButton, tooltip);
    }

    @FXML
    void backToResultsMain(ActionEvent event) {
        mainBorderPane.setCenter(backPane);

    }


    public void setEntitiesGraph() throws InvalidAction {
        Map<String, Map<Integer, Integer>> entitiesAmountPerTick = this.resultsController.getEntitiesAmountPerTick(this.simulationId);
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Tick");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of entities");

        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Entities Graph");

        for (String entity : entitiesAmountPerTick.keySet()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(entity);

            Map<Integer, Integer> dataPoints = entitiesAmountPerTick.get(entity);
            for (Integer x : dataPoints.keySet()) {
                Integer y = dataPoints.get(x);
                series.getData().add(new XYChart.Data<>(x, y));
            }

            lineChart.getData().add(series);
        }
        graphScroolPane.setContent(lineChart);

    }
    public void setEntitiesButtons() {
        propertiresFlowPane.getChildren().clear();
        entitiesFlowPane.getChildren().clear();
        isNumericProperty.set(false);
        isPropertySelected.set(false);
        Map<String, Integer> entitesNamesMap = this.resultsController.getEntitiesAmount(simulationId);
        for (String entity : entitesNamesMap.keySet()) {
            Button button = new Button(entity);
            button.setStyle("-fx-font-size: 15px; -fx-font-family: 'Cooper Black'; -fx-text-fill: #ab8e0a;");
            button.setOnAction(event -> {
                // Handle the button press event to get the name
                selectedEntity = ((Button) event.getSource()).getText();
                setPropertiesButtons();
            });
            entitiesFlowPane.getChildren().add(button);
        }


    }
public void clean(){
    propertiresFlowPane.getChildren().clear();
    valueColumn.getColumns().clear();
    amountColumn.getColumns().clear();
    tableView.getItems().clear();
    tableView.getSelectionModel().clearSelection();
    this.resultsController.clean();
}

    public void setPropertiesButtons() {
        propertiresFlowPane.getChildren().clear();
        isNumericProperty.set(false);
        isPropertySelected.set(false);

        Map<String, Map<String, Map<String, Integer>>> histogramMap = this.resultsController.getHistogram(simulationId);
        Map<String, Map<String, Integer>> entityPropertiesMap = histogramMap.get(selectedEntity);
        if (entityPropertiesMap != null) {
            for (String property : entityPropertiesMap.keySet()) {
                Button button = new Button(property);
                button.setStyle("-fx-font-size: 12px; -fx-font-family: 'Cooper Black'; -fx-text-fill: #ab8e0a;");
                button.setOnAction(event -> {
                    // Handle the button press event to get the name
                    selectedProperty = ((Button) event.getSource()).getText();
                    setHistogram(entityPropertiesMap.get(selectedProperty));
                    consistencyValue.set(resultsController.getConsistency(simulationId).get(selectedEntity).get(selectedProperty));
                    isPropertySelected.set(true);
                    if (resultsController.getAvg(simulationId) != null) {
                        if (resultsController.getAvg(simulationId).get(selectedEntity) != null) {
                            if (resultsController.getAvg(simulationId).get(selectedEntity).get(selectedProperty) != null) {
                                isNumericProperty.set(true);
                                avg.set(resultsController.getAvg(simulationId).get(selectedEntity).get(selectedProperty));
                            }

                        }

                    }
                });
                propertiresFlowPane.getChildren().add(button);
            }


        } else {
            tableView.getItems().clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No entities left");
            alert.setHeaderText(null);
            alert.setContentText("No instances left in this entity definition!\nTherefore no histogram will show.");
            alert.showAndWait();

        }
    }


   public void setHistogram(Map<String, Integer> propertyHistogram) {
       // Clear the existing items in the TableView
       tableView.getItems().clear();

       valueColumn.getColumns().clear();
       amountColumn.getColumns().clear();
       valueColumn.setCellValueFactory(data -> data.getValue().entityNameProperty());
       amountColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEntityAmount()).asObject());

       Map<String, Integer> propsAmount = propertyHistogram;
       propsAmount.forEach((entityName, entityAmount) -> {
           tableView.getItems().add(new EntityData(entityName, entityAmount));
       });
   }
}
