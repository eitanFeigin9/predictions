package simulation;
import system.file.instruction.valid.dto.IsValidDto;

import java.io.Serializable;
import java.util.Map;

public class SimulationExecutionDetails implements Serializable {
    private int currTick;
    private boolean isRunning;
    private long secondsCounter;
    private Map<String,Integer> entitiesPopulation;
    private Map<String,Map<Integer,Integer>> entitiesPopulationByTicks;
    private Map<String,Map<String,Float>> consistency;

    private Map<String,Map<String,Float>> numericPropertyAverage;
    private Map<String, Map<String,Map<String,Integer>>> propertiesHistogram;
    private IsValidDto result;


    public SimulationExecutionDetails(int currTick, boolean isRunning, long secondsCounter, Map<String, Integer> entitiesPopulation, Map<String, Map<Integer, Integer>> entitiesPopulationByTicks, Map<String,Map<String,Float>> consistency, Map<String,Map<String,Float>>numericPropertyAverage, Map<String, Map<String, Map<String, Integer>>> propertiesHistogram,IsValidDto result) {
        this.currTick = currTick;
        this.isRunning = isRunning;
        this.secondsCounter = secondsCounter;
        this.entitiesPopulation = entitiesPopulation;
        this.entitiesPopulationByTicks = entitiesPopulationByTicks;
        this.consistency = consistency;
        this.numericPropertyAverage = numericPropertyAverage;
        this.propertiesHistogram = propertiesHistogram;
        this.result=result;
    }





    public void setCurrTick(int currTick) {
        this.currTick = currTick;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setSecondsCounter(long secondsCounter) {
        this.secondsCounter = secondsCounter;
    }

    public void setResult(IsValidDto result) {
        this.result = result;
    }

    public int getCurrTick() {
        return currTick;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public long getSecondsCounter() {
        return secondsCounter;
    }

    public Map<String, Integer> getEntitiesPopulation() {
        return entitiesPopulation;
    }

    public Map<String, Map<Integer, Integer>> getEntitiesPopulationByTicks() {
        return entitiesPopulationByTicks;
    }

    public Map<String, Map<String, Float>> getConsistency() {
        return consistency;
    }

    public Map<String, Map<String, Float>> getNumericPropertyAverage() {
        return numericPropertyAverage;
    }

    public Map<String, Map<String, Map<String, Integer>>> getPropertiesHistogram() {
        return propertiesHistogram;
    }

    public IsValidDto getResult() {
        return result;
    }
}
