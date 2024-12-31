package simulation;

import components.results.main.MainResultsController;
import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import javafx.application.Platform;
import simulation.queue.SimulationsQueueManage;
import system.file.instruction.valid.dto.IsValidDto;
import world.World;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.property.Property;
import world.property.type.BooleanType;
import world.property.type.DecimalType;
import world.property.type.FloatType;
import world.property.type.StringType;
import world.rule.management.Rule;
import java.util.*;

public class SimulationRunner implements Runnable {
    private int simoulationId;
    private boolean isTimePassed;
    private long secondsCounter;
    private boolean isSimulationRunning;
    private IsValidDto result;
    private int simoulationTick;
    Map<String, Map<String, Float>> consistency;

    private World currSimoulationWorld;
    private Map<String, Map<String, Map<String, Integer>>> propertiesHistogram;

    private Map<String, Map<String, Float>> numericPropertyAverage;

    private Map<String, Integer> entitiesPopulation;


    private Map<String, Map<Integer, Integer>> entitiesPopulationByTicks;
    private Map<Integer, SimulationExecutionDetails> simulationsDetailsMap;

    private long startTime;
    private MainResultsController controller;
private SimulationsQueueManage queueManager;
    private final boolean SIMULATION_RUNNING = true;
    private final boolean STOP_SIMULATION = false;
    private final long DEFAULT_VALUE = -99;
    private final String SECONDS_LIMIT = "by seconds";
    private final String TICKS_LIMIT = "by ticks";
    private final String STRING_TYPE = "String";
    private final String BOOL_TYPE = "Boolean";
    private final String INT_TYPE = "DecimalType";

    public SimulationRunner(World currSimoulationWorld, int simoulationId, MainResultsController controller, Map<Integer, SimulationExecutionDetails> simulationsDetailsMap, SimulationsQueueManage queueManager) {
        this.currSimoulationWorld = currSimoulationWorld;
        this.consistency = new LinkedHashMap<>();
        this.isTimePassed=false;
        this.entitiesPopulation = new LinkedHashMap<>();
        this.isSimulationRunning = SIMULATION_RUNNING;
        this.numericPropertyAverage = new LinkedHashMap<>();
        this.entitiesPopulationByTicks = new LinkedHashMap<>();
        this.propertiesHistogram = new LinkedHashMap<>();
        this.simoulationTick = 0;
        this.secondsCounter = 0;
        this.simoulationId = simoulationId;
        this.queueManager=queueManager;
        this.controller = controller;
        this.simulationsDetailsMap = simulationsDetailsMap;
        for (Map.Entry<String, EntityDefinition> currEntityDefinition : this.currSimoulationWorld.getEntities().entrySet()) {
            EntityInstance templateInstance = currEntityDefinition.getValue().getEntities().get(0);
            currEntityDefinition.getValue().resetMap();
            for (int i = 0; i < currEntityDefinition.getValue().getPopulation(); i++) {
                EntityInstance newEntity = new EntityInstance(currEntityDefinition.getKey());
                createPropertiesForInstance(newEntity, templateInstance);
                currEntityDefinition.getValue().addEntity(newEntity);
                this.currSimoulationWorld.addInstanceToGrid(newEntity);
                this.entitiesPopulation.put(currEntityDefinition.getKey(), currEntityDefinition.getValue().getPopulation());

            }
        }
        perTickMap(0);
        currSimoulationWorld.setSimulationExecutionDetails(new SimulationExecutionDetails(0, this.isSimulationRunning, this.secondsCounter, this.entitiesPopulation, this.entitiesPopulationByTicks, this.consistency, this.numericPropertyAverage, this.propertiesHistogram, this.result));

    }

    @Override
    public void run() {
        String activeDetermination = null;
        this.simoulationTick++;
        this.startTime = System.nanoTime();
        long endTime = DEFAULT_VALUE;
        if (currSimoulationWorld.getSecondsCount() != null) {
            endTime = startTime + currSimoulationWorld.getSecondsCount() * 1_000_000_000L; // Convert seconds to nanoseconds
        }
        controller.bindTaskToUIComponents(simulationsDetailsMap);
        queueManager.increaseWorks();
        while (isSimulationRunning) {
            if (currSimoulationWorld.isPause()) {
                synchronized (currSimoulationWorld.getAttractiveLock()) {
                    while (currSimoulationWorld.isPause()) {
                        try {
                            currSimoulationWorld.getAttractiveLock().wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            if (currSimoulationWorld.isStop()) {
                isSimulationRunning=false;
                result = (new IsValidDto("The simulation ended successfully!\nThe termination condition that stopped the simulation is: by user\nThe simulation ID is:" + this.simoulationId, true));
                this.currSimoulationWorld.getSimulationExecutionDetails().setResult(result);
                break;
            }

            long currentTime = System.nanoTime();
            long elapsedTimeInNanoseconds = currentTime - startTime;
            this.secondsCounter = elapsedTimeInNanoseconds / 1000000000;
            currSimoulationWorld.getSimulationExecutionDetails().setCurrTick(this.simoulationTick);
            currSimoulationWorld.getSimulationExecutionDetails().setSecondsCounter(this.secondsCounter);
            for (Map.Entry<String, EntityDefinition> currEntityDef : currSimoulationWorld.getEntities().entrySet()) {
                for (Map.Entry<Integer, EntityInstance> currEntityInstance : currEntityDef.getValue().getEntities().entrySet()) {
                    currEntityInstance.getValue().randomPlaceInGrid(this.currSimoulationWorld.getGridArray());
                }
            }

            List<Rule> activateRules = new LinkedList<>();
            for (Map.Entry<String, Rule> currRule : currSimoulationWorld.getRules().getRuleMap().entrySet()) {
                if (currRule.getValue().isRuleActivate(this.simoulationTick)) {
                    activateRules.add(currRule.getValue());

                }
            }
            for (Map.Entry<String, EntityDefinition> currEntityDef : currSimoulationWorld.getEntities().entrySet()) {
                for (Map.Entry<Integer, EntityInstance> currEntityInstance : currEntityDef.getValue().getEntities().entrySet()) {
                    for (Rule rule : activateRules) {
                        try {
                            currentTime = System.nanoTime();
                            elapsedTimeInNanoseconds = currentTime - startTime;
                            this.secondsCounter = elapsedTimeInNanoseconds / 1000000000;
                            currSimoulationWorld.getSimulationExecutionDetails().setCurrTick(this.simoulationTick);
                            currSimoulationWorld.getSimulationExecutionDetails().setSecondsCounter(this.secondsCounter);
                            if (currSimoulationWorld.getSecondsCount() != null) {
                                if (System.nanoTime() >= endTime&& !isTimePassed) {
                                    isTimePassed = true;
                                    Platform.runLater(() -> {
                                        controller.showInformationPopup("The simulation " + simoulationId + " time has pass\nplease wait until the end of the tick for results!");

                                    });
                                }
                            }


                            rule.activeRule(this.simoulationTick, currSimoulationWorld.getEntities(), currSimoulationWorld.getEnvironment(), currEntityInstance.getValue());
                        } catch (InvalidAction | ValueNotExists | FormatException | InvalidValue e) {
                            result = new IsValidDto("The simulation has failed!\nIn the rule: " + rule.getName() + "\n" + e.getMessage() + "\nThe values of the world will now be the values that were updated in the simulation until the fault occurred and the values that were not updated will be left as they were before the simulation was run." + "\nThe simulation ID is:" + this.simoulationId, false);
                            this.currSimoulationWorld.getSimulationExecutionDetails().setResult(result);
                            isSimulationRunning = STOP_SIMULATION;
                            break;
                        }
                    }
                }
            }
            for (Map.Entry<String, EntityDefinition> currEntityDef : currSimoulationWorld.getEntities().entrySet()) {
                for (Map.Entry<Integer, EntityInstance> currEntityInstance : currEntityDef.getValue().getEntities().entrySet()) {
                    currentTime = System.nanoTime();
                    elapsedTimeInNanoseconds = currentTime - startTime;
                    this.secondsCounter = elapsedTimeInNanoseconds / 1000000000;
                    currSimoulationWorld.getSimulationExecutionDetails().setCurrTick(this.simoulationTick);
                    currSimoulationWorld.getSimulationExecutionDetails().setSecondsCounter(this.secondsCounter);
                    for (Rule rule : activateRules) {
                        try {
                            rule.activeEndActions(this.simoulationTick, currSimoulationWorld.getEntities(), currSimoulationWorld.getEnvironment(), currEntityInstance.getValue());
                        } catch (InvalidAction | ValueNotExists | FormatException | InvalidValue e) {
                            result = new IsValidDto("The simulation has failed!\nIn the rule: " + rule.getName() + "\n" + e.getMessage() + "\nThe values of the world will now be the values that were updated in the simulation until the fault occurred and the values that were not updated will be left as they were before the simulation was run." + "\nThe simulation ID is:" + this.simoulationId, false);
                            this.currSimoulationWorld.getSimulationExecutionDetails().setResult(result);

                            isSimulationRunning = STOP_SIMULATION;
                        }

                    }

                }
            }
            currSimoulationWorld.getSimulationExecutionDetails().setCurrTick(this.simoulationTick);
            currSimoulationWorld.getSimulationExecutionDetails().setSecondsCounter(this.secondsCounter);
            if ( checkLiveEntities() > currSimoulationWorld.getDimensionSize()) {
                result = (new IsValidDto("The simulation has failed!\nIn the tick: " + this.simoulationTick + "the population size has exceeded the size of the grid!" + "\nThe simulation ID is:" + this.simoulationId, false));
                this.currSimoulationWorld.getSimulationExecutionDetails().setResult(result);

                isSimulationRunning = STOP_SIMULATION;

            }
            if (this.simoulationTick % 1000 == 0) {
                perTickMap(simoulationTick);
            }
            if (currSimoulationWorld.getSecondsCount() != null) {
                if (System.nanoTime() >= endTime) {
                    activeDetermination = SECONDS_LIMIT;
                    isSimulationRunning = STOP_SIMULATION;
                    break;
                }
            }
            if (currSimoulationWorld.getTicksCount() != null) {
                if (currSimoulationWorld.getTicksCount() == this.simoulationTick) {
                    activeDetermination = TICKS_LIMIT;
                    this.simoulationTick--;
                    isSimulationRunning = STOP_SIMULATION;
                }
            } else if (currSimoulationWorld.getTicksCount() == null && currSimoulationWorld.getSecondsCount() == null) {
                activeDetermination = "by user";
            }
            currentTime = System.nanoTime();
            elapsedTimeInNanoseconds = currentTime - startTime;
            this.secondsCounter = elapsedTimeInNanoseconds / 1000000000;
            currSimoulationWorld.getSimulationExecutionDetails().setCurrTick(this.simoulationTick);
            currSimoulationWorld.getSimulationExecutionDetails().setSecondsCounter(this.secondsCounter);
            this.simoulationTick++;

        }

        currSimoulationWorld.getSimulationExecutionDetails().setCurrTick( this.simoulationTick);
        currSimoulationWorld.getSimulationExecutionDetails().setSecondsCounter( this.secondsCounter);
        if(result==null) {
            result = (new IsValidDto("The simulation ended successfully!\nThe termination condition that stopped the simulation is: " + activeDetermination + "\nThe simulation ID is:" + this.simoulationId, true));
            this.currSimoulationWorld.getSimulationExecutionDetails().setResult(result);

        }
        this.isSimulationRunning = false;
        if (this.simoulationTick % 1000 != 0)
        {
            perTickMap(simoulationTick);
        }

        queueManager.increaseFinished();
         getSimulationData();
        currSimoulationWorld.getSimulationExecutionDetails().setRunning(false);


    }
public int checkLiveEntities() {
    int populationCounter = 0;
    for (Map.Entry<String, EntityDefinition> currEntityDefinition : currSimoulationWorld.getEntities().entrySet()) {
        Iterator<Map.Entry<Integer, EntityInstance>> iterator = currEntityDefinition.getValue().getEntities().entrySet().iterator();
        currSimoulationWorld.getSimulationExecutionDetails().setCurrTick(this.simoulationTick);
        currSimoulationWorld.getSimulationExecutionDetails().setSecondsCounter(this.secondsCounter);
        while (iterator.hasNext()) {
            this.entitiesPopulation.put(currEntityDefinition.getKey(), currEntityDefinition.getValue().getPopulation());

            Map.Entry<Integer, EntityInstance> currInstance = iterator.next();
            if (!currInstance.getValue().isEntityAlive()) {
                currEntityDefinition.getValue().setPopulation(currEntityDefinition.getValue().getPopulation() - 1);
                iterator.remove();
                this.entitiesPopulation.put(currEntityDefinition.getKey(), currEntityDefinition.getValue().getPopulation());
                   }
        }
        populationCounter = populationCounter + currEntityDefinition.getValue().getPopulation();
    }
    return populationCounter;
}
    public void createPropertiesForInstance(EntityInstance newInstance,EntityInstance propertiesTemplate){
        Map<String, Property> propertyMap=propertiesTemplate.getEntityProperties();
        for(Map.Entry<String, Property> currProp :propertyMap.entrySet() ){
            insertProperty(newInstance,currProp.getValue());
        }
    }
    public void insertProperty(EntityInstance newInstance,Property currProp)
    {
        if(currProp.getValue().getClass().getSimpleName().equals(STRING_TYPE)){
            if(((StringType)(currProp)).isRandomInitialize())
            {
                StringType newProp = new StringType(currProp.getPropertyName());
                newInstance.addProperty(newProp);
            }
            else {
                StringType newProp = new StringType(currProp.getPropertyName(), ((StringType)(currProp)).getInitValue());
                newInstance.addProperty(newProp);
            }
        }
        else if(currProp.getValue().getClass().getSimpleName().equals(INT_TYPE)){
                if(((DecimalType)(currProp)).isRandomInitialize())
            {
                DecimalType newProp = new DecimalType(currProp.getPropertyName(),((DecimalType)(currProp)).getRange());
                newInstance.addProperty(newProp);
            }
            else {
                DecimalType newProp = new DecimalType(currProp.getPropertyName(),((DecimalType)(currProp)).getRange(), ((DecimalType)(currProp)).getInitValue());
                newInstance.addProperty(newProp);
            }
        }
        else if(currProp.getValue().getClass().getSimpleName().equals(BOOL_TYPE)) {
            if (((BooleanType) (currProp)).isRandomInitialize()) {
                BooleanType newProp = new BooleanType(currProp.getPropertyName());
                newInstance.addProperty(newProp);
            } else {
                BooleanType newProp = new BooleanType(currProp.getPropertyName(), ((BooleanType) (currProp)).getInitValue());
                newInstance.addProperty(newProp);
            }
        }
        else {
            if(((FloatType)(currProp)).isRandomInitialize())
            {
                FloatType newProp = new FloatType(currProp.getPropertyName(),((FloatType)(currProp)).getRange());
                newInstance.addProperty(newProp);
            }
            else {
                FloatType newProp = new FloatType(currProp.getPropertyName(),((FloatType)(currProp)).getRange(), ((FloatType)(currProp)).getInitValue());
                newInstance.addProperty(newProp);
            }
        }
    }

    public IsValidDto getResult() {
        return result;
    }

    public  void getSimulationData(){

        for (Map.Entry<String, EntityDefinition> currEntityDef : currSimoulationWorld.getEntities().entrySet())
        {

            entitiesPopulation.put(currEntityDef.getKey(),currEntityDef.getValue().getPopulation());
                Map<String,Map<String,Integer>> propertiesValueCount=new LinkedHashMap<>();
                Map<String,Integer> propertyValueCount;
                Map<String,Float> numericPropertiesValuesSum=new LinkedHashMap<>();
                Map<String,Integer> numericPropertiesValuesCounter=new LinkedHashMap<>();
                Map<String,Float> propertiesConsistencyMap=new LinkedHashMap<>();
                for (Map.Entry<Integer, EntityInstance> currEntityInstance : currEntityDef.getValue().getEntities().entrySet()) {
                   // propertyValueCount=new LinkedHashMap<>();
            for (Map.Entry<String, Property> currProp:currEntityInstance.getValue().getEntityProperties().entrySet()){
                int sum=0;
                 propertyValueCount=new LinkedHashMap<>();
                 if( propertiesValueCount.containsKey(currProp.getKey()))
                 {
                     propertyValueCount=propertiesValueCount.get(currProp.getKey());
                 }
                for (int currValue: currProp.getValue().getNoChangesCount())
                {
                    sum =sum+currValue;
                }
                float avgTicksWithoutChange;
                if(!currProp.getValue().getNoChangesCount().isEmpty()) {
                     avgTicksWithoutChange = (float) sum / currProp.getValue().getNoChangesCount().size();
                }else{   avgTicksWithoutChange =simoulationTick;}
                if(propertiesConsistencyMap.containsKey(currProp.getKey())){
                    float currAvg= propertiesConsistencyMap.get(currProp.getKey());
                    propertiesConsistencyMap.put(currProp.getKey(),(currAvg+avgTicksWithoutChange)/2);
                }
                else {
                    propertiesConsistencyMap.put(currProp.getKey(),avgTicksWithoutChange);
                }

                if(!propertyValueCount.containsKey(currProp.getValue().getValue().toString())){
                    propertyValueCount.put(currProp.getValue().getValue().toString(),1);
                }
                else {
                   int counter= propertyValueCount.get(currProp.getValue().getValue().toString())+1;
                    propertyValueCount.put(currProp.getValue().getValue().toString(),counter);
                }
                propertiesValueCount.put(currProp.getKey(),propertyValueCount);
                if(currProp.getValue() instanceof DecimalType ||currProp.getValue() instanceof FloatType ){
                    float currValue;
                    if(currProp.getValue() instanceof DecimalType)
                    {
                        Integer intValue = (Integer) currProp.getValue().getValue();
                        currValue=intValue.floatValue();
                    }
                    else {
                        currValue= (float) currProp.getValue().getValue();
                    }
                    if(numericPropertiesValuesSum.containsKey(currProp.getKey())){
                        float valuesSum=numericPropertiesValuesSum.get(currProp.getKey())+currValue;
                        numericPropertiesValuesSum.put(currProp.getKey(),valuesSum);
                        int counter= numericPropertiesValuesCounter.get(currProp.getKey())+1;
                        numericPropertiesValuesCounter.put(currProp.getKey(),counter);
                    }
                    else {
                        numericPropertiesValuesCounter.put(currProp.getKey(),1);
                        numericPropertiesValuesSum.put(currProp.getKey(),currValue);
                    }
                }
            }
            propertiesHistogram.put(currEntityDef.getKey(), propertiesValueCount);

                }
            Map<String,Float> avargeMap = new LinkedHashMap<>();
                for (Map.Entry<String, Float> numericProperty : numericPropertiesValuesSum.entrySet()) {
                    float average =numericProperty.getValue()/numericPropertiesValuesCounter.get( numericProperty.getKey());
                    avargeMap.put(numericProperty.getKey(),average);
                }
                numericPropertyAverage.put(currEntityDef.getKey(),avargeMap);
                consistency.put(currEntityDef.getKey(),propertiesConsistencyMap) ;

            }
    }
    public void perTickMap(int currTick){
        for(EntityDefinition currEntity:this.currSimoulationWorld.getEntities().values()){
            Map<Integer,Integer>temp=new LinkedHashMap<>();

            if(currTick!=0){
                temp= this.entitiesPopulationByTicks.get(currEntity.getEntityName());
                temp.put(currTick,currEntity.getPopulation());
            }
            else {
                temp.put(currTick, currEntity.getPopulation());
            }
            this.entitiesPopulationByTicks.put(currEntity.getEntityName(),temp);

        }


    }

}

