package components.details.tree;

import components.details.entity.EntityController;
import components.details.tree.action.pane.decrease.DecreaseController;
import components.details.tree.action.pane.divide.DivideController;
import components.details.tree.action.pane.increase.IncreaseController;
import components.details.tree.action.pane.kill.KillController;
import components.details.tree.action.pane.multiple.condition.MultipleConditionController;
import components.details.tree.action.pane.multiply.MultiplyController;
import components.details.tree.action.pane.proximity.ProximityController;
import components.details.tree.action.pane.replace.ReplaceController;
import components.details.tree.action.pane.set.SetController;
import components.details.tree.action.pane.single.condition.SingleConditionController;
import components.details.tree.activation.ActivitionController;
import components.details.tree.termination.TerminationController;
import system.file.instruction.show.dto.EntitiesDto;
import system.file.instruction.show.dto.EnvironmentDto;
import system.file.instruction.show.dto.RulesDto;
import system.file.instruction.show.dto.TerminationDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TreeController implements Initializable {

    @FXML
    private TreeView<String> detailsTree;
    private TreeItem<String> entitiesItem;
    private TreeItem<String> environmentItem;
    private TreeItem<String> rulesItem;
    private TreeItem<String> terminationItem;
    private Map<String, Map<String, Map<String, String>>> entitiesMap = new LinkedHashMap<>();
    private Map<String, Map<String, String>> envMap = new LinkedHashMap<>();
private  Map<String, Map<String, List<String>>> actionsMap=new LinkedHashMap<>();
    private   Map<String, List<String>>  activationMap=new LinkedHashMap<>();
    private List<String> terminationList= new ArrayList<>();

    private BorderPane borderPane = null;

    @FXML
    private HBox entityControllersHBox; // Add HBox

    @FXML
    void selectItem(MouseEvent event) throws IOException {
        boolean isFirstTry = true;
        FlowPane res=new FlowPane();
        TreeItem<String> selectedItem = detailsTree.getSelectionModel().getSelectedItem();
        EntityController entityController = null;
        //CenterDetailsPresentationController centerController = null;
        if (selectedItem != null && selectedItem.getParent() == entitiesItem) {
            String entityName = selectedItem.getValue();
            FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/components/details/manage/center/centerHboxFxml.fxml"));
            Map<String, Map<String, String>> entityMap = entitiesMap.get(entityName);
            for (Map.Entry<String, Map<String, String>> entry : entityMap.entrySet()) {
                String propertyName = entry.getKey();
                Map<String, String> entityProperties = entry.getValue();
                try {
                    if (isFirstTry) {
                        Parent centerRoot = centerLoader.load();
                   //     centerController = centerLoader.getController();
                    }
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/entity/entityFxml.fxml"));
                   ScrollPane entityPane = loader.load();
                    entityController = loader.getController();
                    ScrollPane currCard = entityController.createNewEntityView(entityProperties, entityName, propertyName);
                    res.getChildren().add(currCard);
                    isFirstTry = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           // assert centerController != null;
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
        } else if (selectedItem != null && "Environment".equals(selectedItem.getValue())) {
            FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/components/details/manage/center/centerHboxFxml.fxml"));
            for (Map.Entry<String, Map<String, String>> entry : envMap.entrySet()) {
                String propertyName = entry.getKey();
                Map<String, String> propMap = envMap.get(propertyName);
                try {
                    if (isFirstTry) {
                        Parent centerRoot = centerLoader.load();
                      //  centerController = centerLoader.getController();
                    }
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/entity/entityFxml.fxml"));
                   ScrollPane entityPane = loader.load();
                    entityController = loader.getController();
                    ScrollPane currCard = entityController.createNewEntityView(propMap, null, propertyName);
                    res.getChildren().add(currCard);
                    isFirstTry = false;
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
                //assert centerController != null;
                borderPane.setCenter(scrollPane);



        }
        else if(selectedItem != null && "Termination".equals(selectedItem.getValue())){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/termination/terminationFxml.fxml"));
            AnchorPane anchorPane = loader.load();
            TerminationController controller = loader.getController();
            controller.dynamicInitialize(terminationList);
            res.getChildren().add(anchorPane);
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
        }else if (selectedItem != null) {
            if(selectedItem.getParent()!=null)
            { if(selectedItem.getParent().getParent()!=null)
            {if(selectedItem.getParent().getParent() == rulesItem)
            { if ("Actions".equals(selectedItem.getValue())) {
                handleActionsItemSelected(selectedItem);
            } else if ("Activation".equals(selectedItem.getValue())) {
                // Handle the selection of "Activation" under a rule
                handleActivationItemSelected(selectedItem);
            }}
            }}}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        detailsTree.refresh();
        ImageView main = new ImageView(new Image("/pictures/—Pngtree—world environment day 5th june_9170049.png"));
        main.setFitWidth(25); // Set the desired width
        main.setFitHeight(25); // Set the desired height
        ImageView entitiesImage = new ImageView(new Image("/pictures/entities.png"));
        entitiesImage.setFitWidth(25); // Set the desired width
        entitiesImage.setFitHeight(25); // Set the desired height
        ImageView envImage = new ImageView(new Image("/pictures/env.png"));
        envImage.setFitWidth(25); // Set the desired width
        envImage.setFitHeight(25); // Set the desired height
        ImageView rulesImage = new ImageView(new Image("/pictures/rules.png"));
        rulesImage.setFitWidth(25); // Set the desired width
        rulesImage.setFitHeight(25); // Set the desired height
        ImageView terminationImage = new ImageView(new Image("/pictures/stop.png"));
        terminationImage.setFitWidth(25); // Set the desired width
        terminationImage.setFitHeight(25); // Set the desired height

        TreeItem<String> rootItem = new TreeItem<>("World", main);
        entitiesItem = new TreeItem<>("Entities",entitiesImage);
        environmentItem = new TreeItem<>("Environment",envImage);
        rulesItem = new TreeItem<>("Rules",rulesImage);
        terminationItem = new TreeItem<>("Termination",terminationImage);
        rootItem.getChildren().addAll(entitiesItem, environmentItem, rulesItem,terminationItem);
        detailsTree.setRoot(rootItem);
    }
    public void initializeDynamicData(EntitiesDto entitiesDto, BorderPane borderPane, EnvironmentDto environmentDto, RulesDto rulesDto, TerminationDto terminationDto){//, BorderPane borderPane
        this.borderPane=borderPane;
        this.actionsMap=rulesDto.getActionsMap();
        this.activationMap=rulesDto.getActivationMap();
        Map<String, Map<String, Map<String, String>>> entitiesMap= entitiesDto.getEntitiesMapDto();
        for (Map.Entry<String, Map<String, Map<String, String>>> outerEntry : entitiesMap.entrySet()) {
            ImageView entityImage = new ImageView(new Image("/pictures/entitiy.png"));
            entityImage.setFitWidth(25);
            entityImage.setFitHeight(25);
            TreeItem<String> entityBranch = new TreeItem<>(outerEntry.getKey(),entityImage);
            entitiesItem.getChildren().add(entityBranch);
        }
        for (Map.Entry<String,List<String>> currRule:  this.activationMap.entrySet()){
            ImageView ruleImage = new ImageView(new Image("/pictures/rulee.png"));
            ruleImage.setFitWidth(25);
            ruleImage.setFitHeight(25);
            TreeItem<String> ruleBranch = new TreeItem<>(currRule.getKey(),ruleImage);
            ImageView activationImage = new ImageView(new Image("/pictures/activeRulee.png"));
            activationImage.setFitWidth(25);
            activationImage.setFitHeight(25);
            TreeItem<String> activationItem = new TreeItem<>("Activation",activationImage);
            ImageView actionImage = new ImageView(new Image("/pictures/actions1.png"));
            actionImage.setFitWidth(25);
            actionImage.setFitHeight(25);
            TreeItem<String> actionsItem = new TreeItem<>("Actions",actionImage);

            // Add the activation and actions sub-items to the rule
            ruleBranch.getChildren().addAll(activationItem, actionsItem);
            rulesItem.getChildren().add(ruleBranch);
        }


        this.envMap=environmentDto.getenvironmentMapDto();
        this.entitiesMap=entitiesMap;
        this.terminationList=terminationDto.getTermination();
    }
    private void handleActionsItemSelected(TreeItem<String> selectedItem) throws IOException {
        TreeItem<String> ruleItem = selectedItem.getParent();
        String ruleName = ruleItem.getValue();
        Map<String, List<String>> actionsMap = this.actionsMap.get(ruleName);
        FlowPane res=new FlowPane();
        for (Map.Entry<String, List<String>> currActionType : actionsMap.entrySet()){
            String currAction=currActionType.getKey();
            List<String> stringList=currActionType.getValue();
            for(String currActionData:stringList){
            if(currAction.equals("Divide")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/divide/divideFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                DivideController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            } else if (currAction.equals("Multiply")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/multiply/multiplyFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                MultiplyController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            } else if (currAction.equals("MultipleCondition")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/multiple/condition/multipleConditionFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                MultipleConditionController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            } else if (currAction.equals("SingleCondition")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/single/condition/SingleConditionFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                SingleConditionController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);

            } else if (currAction.equals("Proximity")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/proximity/proximityFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                ProximityController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            } else if (currAction.equals("Replace")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/replace/replaceFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                ReplaceController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            } else if (currAction.equals("Decrease")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/decrease/decreaseFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                DecreaseController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            } else if (currAction.equals("Increase") ) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/increase/increaseFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                IncreaseController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            } else if (currAction.equals("Kill") ) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("components/details/tree/action/pane/kill/killFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                KillController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            } else if (currAction.equals("Set")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/action/pane/set/setFxml.fxml"));
                AnchorPane anchorPane = loader.load();
                SetController controller = loader.getController();
                controller.setLabel(currActionData);
                res.getChildren().add(anchorPane);
            }
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

    }

    private void handleActivationItemSelected(TreeItem<String> selectedItem) throws IOException {
        TreeItem<String> ruleItem = selectedItem.getParent();
        String ruleName = ruleItem.getValue();
        List<String>  activatiosMap = this.activationMap.get(ruleName);
        FlowPane res=new FlowPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/details/tree/activation/activationFxml.fxml"));
        AnchorPane anchorPane = loader.load();
        ActivitionController controller = loader.getController();
        controller.dynamicInitialize(activatiosMap);
        res.getChildren().add(anchorPane);
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
    }

    public void cleanTree(){
        detailsTree.refresh();
        if( detailsTree.getRoot().getChildren()!=null){
        detailsTree.getRoot().getChildren().clear();}
       if(borderPane!=null){
       borderPane.setCenter(null);
        borderPane.setRight(null);
        borderPane.setBottom(null);

    }}
}