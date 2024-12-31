package components.details.entity;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class EntityController  implements Initializable {

    @FXML
    private Label entityNameLabel;
    private SimpleStringProperty selectedEntity;
    private SimpleStringProperty selectedProperty;

    @FXML
    private Label propEntNameLabel;

    @FXML
    private VBox entityPropVbox;
    @FXML
    private ScrollPane scrolPaneCard;

    @FXML
    private FlowPane dataFlowPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedEntity = new SimpleStringProperty();
        selectedProperty= new SimpleStringProperty();
        entityNameLabel.textProperty().bind(selectedEntity);
        propEntNameLabel.textProperty().bind(selectedProperty);

    }
   public ScrollPane createNewEntityView(Map<String, String> entityProps, String entityName, String entPropName){
      Font labelFont ;
      String currType = null;
       for (Map.Entry<String, String> entry : entityProps.entrySet()){
           labelFont = new Font("Cooper Black", 20);
           Label currLabel=new Label(entry.getKey()+entry.getValue());
           if(entry.getKey().equals("Type: ")){
               currType=entry.getValue();
           }
           if(entry.getKey().equals("Value: "))
           {
               assert currType != null;
               if(currType.equals("String\n")){
                   String currStringValue = entry.getValue();
                   currLabel=new Label(entry.getKey()+"\n");
                   currLabel.setFont(labelFont);
                   currLabel.setAlignment(Pos.CENTER);
                   dataFlowPane.getChildren().add(currLabel);
                   currLabel=new Label(entry.getValue());
                   if(currStringValue.length()>=44)
                   {
                       labelFont = new Font("Cooper Black", 8.5);
                   }
                   else if(currStringValue.length()>38)
                   {
                       labelFont = new Font("Cooper Black", 10);
                   }
                   else if(currStringValue.length()>=35)
                   {

                       labelFont = new Font("Cooper Black", 11.5);
                   }
                   else if(currStringValue.length()>=32)
                   {
                       labelFont = new Font("Cooper Black", 13);
                   }
                   else if(currStringValue.length()>=27)
                   {
                       labelFont = new Font("Cooper Black", 16.5);
                   }
                   else if(currStringValue.length()>25)
                   {
                       labelFont = new Font("Cooper Black", 18);
                   }
                   else if(currStringValue.length()>20)
                   {
                       labelFont = new Font("Cooper Black", 20);
                   }
               }
           }
           currLabel.setFont(labelFont);
           currLabel.setAlignment(Pos.CENTER);
           dataFlowPane.getChildren().add(currLabel);
       }
       if(entityName == null)
       {
           selectedEntity.set(entPropName);

       }
       else {
           selectedEntity.set(entityName);
           selectedProperty.set(entPropName);

       }
       return scrolPaneCard;
   }


}
/*
 Label currLabel = new Label("p1");
        Font newFont = new Font("Cooper Black", 40);
        currLabel.setFont(newFont);

        // Remove the border around entityPropVbox
        entityPropVbox.setBorder(new Border(new BorderStroke(
                Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, new BorderWidths(0))
        ));

        Label currLabel1 = new Label("p2");
        Label currLabel2 = new Label("p3");
        Label currLabel3 = new Label("p4");
        Label currLabel4 = new Label("p5");
        Label currLabel5 = new Label("p6");
        Label currLabel6 = new Label("p7");

        selectedEntity.set("Smoker");

        entityPropVbox.getChildren().addAll(
                currLabel, currLabel1, currLabel2, currLabel3, currLabel4, currLabel5, currLabel6
        );
 */