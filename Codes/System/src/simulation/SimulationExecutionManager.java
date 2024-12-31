package simulation;

import components.results.main.MainResultsController;
import simulation.queue.SimulationsQueueManage;
import system.file.ManageSystemToUi;
import world.World;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationExecutionManager  {
    private Map<Integer,SimulationExecutionDetails>simulationsDetailsMap;
    private int threadPoolSize;
    private ExecutorService threadPool;
    private int simulationIdCounter;
    private MainResultsController controller;
    private ManageSystemToUi manageSystemToUi;


    public SimulationExecutionManager(int threadPoolSize,MainResultsController controller,ManageSystemToUi manageSystemToUi) {
        this.simulationsDetailsMap = new LinkedHashMap<>();
        this.threadPoolSize = threadPoolSize;
        this.threadPool = Executors.newFixedThreadPool(this.threadPoolSize);
        this.simulationIdCounter = 1;
        this.controller=controller;
        this.manageSystemToUi=manageSystemToUi;
    }
    public void startSimulation(World currWorld, SimulationsQueueManage queueManager)  {
        queueManager.increaseWaiting();
        int simulationId = simulationIdCounter++;
        manageSystemToUi.addWorldBeforeChangesToMap(simulationId,currWorld);
        SimulationRunner simulationRunnable = new SimulationRunner(currWorld, simulationId,controller,simulationsDetailsMap,queueManager);
        SimulationExecutionDetails executionDetails = currWorld.getSimulationExecutionDetails();
        simulationsDetailsMap.put(simulationId, executionDetails);
        threadPool.execute(simulationRunnable);

    }

    public Map<Integer, SimulationExecutionDetails> getSimulationsDetailsMap() {
        return simulationsDetailsMap;
    }
}
