import components.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;

public class TasksMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        // load main fxml
        URL mainFXML = getClass().getResource("/components/main/mainCheck.fxml");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        // wire up controller
        MainController mainController = loader.getController();
        mainController.setPrimaryStage(primaryStage);
        mainController.setCurrSystemConnection(null);

        // set stage
        primaryStage.setTitle("Predictions");
        Scene scene = new Scene(root, 1500, 700);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {

        launch(args);
    }
}