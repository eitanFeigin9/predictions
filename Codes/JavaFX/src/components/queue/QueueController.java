package components.queue;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import simulation.queue.SimulationsQueueManage;

import java.net.URL;
import java.util.ResourceBundle;


public class QueueController  {

    @FXML
    private Label finishedSimulationLabel;

    @FXML
    private Label progressSimulationLabel1;

    @FXML
    private Label waitSimulationLabel;
    private SimulationsQueueManage queueManager;


    public void setData(SimulationsQueueManage queueManager){
   this.queueManager=queueManager;
        waitSimulationLabel.textProperty().bind(Bindings.concat("",queueManager.getWaiting()));
        progressSimulationLabel1.textProperty().bind(Bindings.concat("",queueManager.getWorks()));
        finishedSimulationLabel.textProperty().bind(Bindings.concat("",queueManager.getFinished()));

}



}
