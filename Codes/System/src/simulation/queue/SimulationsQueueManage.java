package simulation.queue;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;

public class SimulationsQueueManage {
    private final SimpleIntegerProperty waiting;
    private final SimpleIntegerProperty works;
    private final SimpleIntegerProperty finished;
    private final int RESET=0;
    public SimulationsQueueManage() {
        waiting=new SimpleIntegerProperty(RESET);
        works=new SimpleIntegerProperty(RESET);
        finished=new SimpleIntegerProperty(RESET);
    }
    public void increaseWaiting(){
        Platform.runLater(() -> {
        waiting.set(waiting.getValue()+1);});
    }
    public void increaseWorks(){
        Platform.runLater(() -> {
        waiting.set(waiting.getValue()-1);
        works.set(works.getValue()+1);});
    }
    public void increaseFinished(){
        Platform.runLater(() -> {
        works.set(works.getValue()-1);
        finished.set(finished.getValue()+1);});
    }

    public SimpleIntegerProperty getWaiting() {
        return waiting;
    }

    public SimpleIntegerProperty getWorks() {
        return works;
    }

    public SimpleIntegerProperty getFinished() {
        return finished;
    }
}
