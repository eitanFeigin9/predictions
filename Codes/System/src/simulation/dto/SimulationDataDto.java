package simulation.dto;

import exception.InvalidAction;

import simulation.SimulationExecutionDetails;
import system.file.dto.ManageDto;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationDataDto extends ManageDto {
    private Map<Integer, SimulationExecutionDetails> simulationsDetailsMap;
    public SimulationDataDto( Map<Integer, SimulationExecutionDetails> simulationsDetailsMap) {
        super(true);
        this.simulationsDetailsMap=simulationsDetailsMap;
    }
    public SimulationExecutionDetails getSimulationExeData(int simId){return simulationsDetailsMap.get(simId);}
public Map<Integer,String> getSimulationStatus(){
    Map<Integer,String>result=new LinkedHashMap<>();
    for (Map.Entry<Integer, SimulationExecutionDetails>currData:simulationsDetailsMap.entrySet()){
        if(currData.getValue().isRunning()){
            result.put(currData.getKey(),"active");
        }
        else {
            result.put(currData.getKey(),"finished");
        }
    }
    return result;
    }
    public int getCurrTick(int simulationId){
        return simulationsDetailsMap.get(simulationId).getCurrTick();
    }
    public double getSecondsCounter(int simulationId){
        return simulationsDetailsMap.get(simulationId).getSecondsCounter();
    }
    public Map<String,Integer> getEntitiesAmount(int simulationId){
        return simulationsDetailsMap.get(simulationId).getEntitiesPopulation();
    }
    public Map<String, Map<Integer,Integer>> getEntitiesAmountPerTick(int simulationId) throws InvalidAction {
        if(simulationsDetailsMap.get(simulationId).isRunning())
        {
            throw new InvalidAction("This simulation is still running");
        }
        return simulationsDetailsMap.get(simulationId).getEntitiesPopulationByTicks();
    }
    public  Map<String, Map<String,Map<String,Integer>>> getHistograma(int simulationId){
        return simulationsDetailsMap.get(simulationId).getPropertiesHistogram();
    }
    public Map<String,Map<String,Float>>  getConsistency(int simulationId){
        return simulationsDetailsMap.get(simulationId).getConsistency();
    }
    public  Map<String,Map<String,Float>>  getAvg(int simulationId){
        return simulationsDetailsMap.get(simulationId).getNumericPropertyAverage();
    }
    public String getResultStatus(int simulationId) throws InvalidAction {
        if(simulationsDetailsMap.get(simulationId).getResult()!=null){
            return simulationsDetailsMap.get(simulationId).getResult().getStatus();
        }
        else {
            throw new InvalidAction("This simulation is still running");
        }

    }

}
