package system.file;


import components.results.main.MainResultsController;
import exception.*;
import input.dto.*;
import simulation.SimulationExecutionManager;
import simulation.dto.SimulationDataDto;
import system.file.dto.ManageDto;
import system.file.instruction.entity.EntitesListDto;
import system.file.instruction.entity.population.EntitesPopulationValueDto;
import system.file.instruction.entity.population.PopulationUpdatesDto;
import system.file.instruction.entity.population.ResetEntityPopulation;
import system.file.instruction.entity.population.SetEntityPopulation;
import system.file.instruction.environment.envValuesActions;
import system.file.instruction.show.Manage.CreateEntitiesMap;
import system.file.instruction.show.Manage.CreateEnvironmentMap;
import system.file.instruction.show.Manage.CreateRulesMap;
import system.file.instruction.show.dto.*;
import system.file.instruction.simulation.enviorment.SetEnvPropValue;
import system.file.instruction.valid.IfValidCreateWorld;
import world.World;
import world.property.Property;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ManageSystemToUi {
    private final String CREATE_WORLD = "create world";
    private final String SET_ENVIRONMENT_VALUE = "set environment's property value";
    private final String SHOW_ENTITIES_DATA = "show entities data";
    private final String SHOW_ENVIRONMENT_DATA = "show environment data";
    private final String SHOW_ENVIRONMENT_VALUES_DATA = "show environment values data";

    private final String CLEAR_ENVIRONMENT_PROPERTIES_INPUT = "clear env's props input";
    private final String CLEAR_POPULATION_INPUT = "clear population input";
    private final String SET_ENVIRONMENT_PROPERTIES_VALUES = "set env values";

    private final String SET_ENTITY_POPULATION= "set entity population";
    private final String GET_ENTITIES_NAMES= "get entities names";
    private final String GET_ENTITIES_POPULATION= "get entities populations values";
    private final String GET_TERMINATION ="get termination";
private final String SHOW_RULES_DATA = "get rules data";
private final String GET_AMOUNT_IN_DIMENSION = "get update dimension size";
    private final String GET_AMOUNT_IN_SPECIFIC_DIMENSION = "get update specific dimension size";

    private final String SHOW_ENVIRONMENT_RUN_AGAIN = "get environment data by run again";
    private final String SHOW_ENTITIES_RUN_AGAIN = "get entities data by run again";
    private final String SET_ENTITY_POPULATION_AGAIN="set entity population again";
    private List<World> worldList;
    private World currWorld;
    private int currDimension;
    private Property currEnvProperty;

    private Map<Integer, World> worldsMapBeforeSimulation;
    private Map<Integer, World> worldsSimulationMap;


    private int simulationChoiceId;
    private  SimulationExecutionManager simulationExecutionManager;
    private boolean isFirstWorldSimulation;
    private boolean isFirstSimulationRun;

    private InstructionsDto uiDto;
    public ManageSystemToUi(InstructionsDto uiDto) {
        this.worldList = new ArrayList<>();
        this.uiDto=uiDto;
        this.currWorld=null;
        this.worldsMapBeforeSimulation= new LinkedHashMap<>();
        this.worldsSimulationMap=new LinkedHashMap<>();
        this.currEnvProperty=null;
        this.isFirstWorldSimulation=true;
        this.isFirstSimulationRun=true;
        simulationExecutionManager=null;
        this.currDimension= 0;
    }
    public void setUiDto(InstructionsDto uiDto) {
        this.uiDto = uiDto;
    }

    public ManageDto executeInstruction() throws InvalidAction, RangeException, JAXBException, InvalidValue, ValueNotExists, ValueExists, FormatException {
        String instruction = uiDto.getInstruction();
        switch (instruction){
            case (CREATE_WORLD):
                IfValidCreateWorld validCheck = new IfValidCreateWorld();
                World newWorld = validCheck.createWorld(((FileDto)uiDto).getFilePath());
                worldList.add(newWorld.deepCopy());
                this.currWorld=newWorld;
                this.currDimension=this.currWorld.getDimensionSize();
                this.isFirstWorldSimulation=true;
                return (new ManageDto(true));
            case(SET_ENVIRONMENT_VALUE):
                this.currEnvProperty= currWorld.getEnvironment().getEnvironmentProperties().get(((envInputDto)uiDto).getPropName());
                return ((new SetEnvPropValue(((envInputDto)uiDto).getPropValue(),currEnvProperty)).isSetEnvPropValueSucceed());
            case (SHOW_ENTITIES_DATA):
                CreateEntitiesMap createEntitiesMapHandler = new CreateEntitiesMap();
                return new EntitiesDto( createEntitiesMapHandler.getEntitiesMap(this.worldList.get(0)));
            case (SHOW_ENVIRONMENT_DATA):
                CreateEnvironmentMap createEnvironmentMapHandler = new CreateEnvironmentMap();
                return new EnvironmentDto( createEnvironmentMapHandler.getEnvironmentMap(this.worldList.get(0)));
            case (SHOW_RULES_DATA):
                CreateRulesMap createRulesMapHandler=new CreateRulesMap();
                return new RulesDto( createRulesMapHandler.getActionsMap(this.worldList.get(0)),createRulesMapHandler.getActivationMap(this.worldList.get(0)));
                case (GET_ENTITIES_NAMES):
                return(new EntitesListDto(this.worldList.get(0).getEntities()));
            case(GET_AMOUNT_IN_DIMENSION):
                return new PopulationUpdatesDto(true,"ok", this.worldList.get(0).getDimensionSize());
            case(GET_AMOUNT_IN_SPECIFIC_DIMENSION):
                return new PopulationUpdatesDto(true,"ok", this.worldsMapBeforeSimulation.get(simulationChoiceId).getDimensionSize());
            case (GET_TERMINATION):
                return new TerminationDto( this.worldList.get(0).getTermination());
                case (SET_ENTITY_POPULATION):
                String entityName = ((StringAndIntDto)uiDto).getStringData();
                int populationAmount = ((StringAndIntDto)uiDto).getNumberData();
                SetEntityPopulation setEntityPopulation = new SetEntityPopulation(this.currWorld.getEntities());
                PopulationUpdatesDto populationUpdatesDto = setEntityPopulation.setPopulation(populationAmount,entityName,this.currDimension);
                this.currDimension = populationUpdatesDto.getCurrDimension();
                return populationUpdatesDto;
            case (CLEAR_ENVIRONMENT_PROPERTIES_INPUT):
                envValuesActions setIsUserInput =new envValuesActions(this.currWorld.getEnvironment());
                setIsUserInput.resetEnvPropValue();
                return new ManageDto(true);
            case (CLEAR_POPULATION_INPUT):
                ResetEntityPopulation resetEntityPopulation = new ResetEntityPopulation(this.currWorld.getEntities());
                resetEntityPopulation.resetPopulation();
                return new ManageDto(true);
            case (SET_ENVIRONMENT_PROPERTIES_VALUES):
                envValuesActions setEnvValuesInput =new envValuesActions(this.currWorld.getEnvironment());
                setEnvValuesInput.setEnvPropValue();
                return new ManageDto(true);
            case (SHOW_ENVIRONMENT_VALUES_DATA):
                CreateEnvironmentMap createEnvironmentValuesMap = new CreateEnvironmentMap();
                return new EnvironmentDto( createEnvironmentValuesMap.getEnvironmentProperitesMap(this.currWorld));
            case (GET_ENTITIES_POPULATION):
                CreateEntitiesMap createEntitiesPopulationMapHandler = new CreateEntitiesMap();
                return new EntitesPopulationValueDto( createEntitiesPopulationMapHandler.getEntitiesPopulationMap(this.currWorld));
            case (SHOW_ENVIRONMENT_RUN_AGAIN):
                simulationChoiceId=((intChoiceDto)uiDto).getUserChoice();
                CreateEnvironmentMap createEnvironmentMapManage = new CreateEnvironmentMap();
                return new EnviromentRunAgainDto(createEnvironmentMapManage.getEnvironmentProperitesValues(worldsMapBeforeSimulation.get(simulationChoiceId)),createEnvironmentMapManage.getEnvironmentMap(worldsMapBeforeSimulation.get(simulationChoiceId)));
            case (SHOW_ENTITIES_RUN_AGAIN):
               CreateEntitiesMap createEntitiesPopulationMap = new CreateEntitiesMap();
                return new EntitiesRunAgainDto(createEntitiesPopulationMap.getEntitiesPopulationByStringMap(worldsMapBeforeSimulation.get(simulationChoiceId)),createEntitiesPopulationMap.getEntitiesMap(worldsMapBeforeSimulation.get(simulationChoiceId)));
            case (SET_ENTITY_POPULATION_AGAIN):
                String entityNameForRunAgain = ((StringAndIntDto)uiDto).getStringData();
                int populationAmountForRunAgain = ((StringAndIntDto)uiDto).getNumberData();
                SetEntityPopulation setEntityPopulationAgain = new SetEntityPopulation(worldsMapBeforeSimulation.get(simulationChoiceId).getEntities());
                PopulationUpdatesDto populationUpdatesDtoAgain = setEntityPopulationAgain.setPopulation(populationAmountForRunAgain,entityNameForRunAgain,this.currDimension);
                this.currDimension = populationUpdatesDtoAgain.getCurrDimension();
                currWorld=worldsMapBeforeSimulation.get(simulationChoiceId);
                return populationUpdatesDtoAgain;
        }
        return null;
    }
public void runSimulation(MainResultsController controller){

    if( this.isFirstSimulationRun){
        this.isFirstSimulationRun=false;
        simulationExecutionManager=new SimulationExecutionManager(currWorld.getThreadPoolSize(), controller,this);
    }
    assert simulationExecutionManager != null;
    simulationExecutionManager.startSimulation(currWorld,controller.getQueueManager());

}
public SimulationDataDto getSimulationData(){
    return new SimulationDataDto(simulationExecutionManager.getSimulationsDetailsMap());
}
public void addWorldBeforeChangesToMap(int simulationId,World world){
    worldsMapBeforeSimulation.put(simulationId,world.deepCopy());
    addSimulationToSimulationsMap(simulationId,world);

}
    public void attractiveUserControl(int simulationId,boolean pause,boolean resume,boolean stop) {
        if (pause) {
            worldsSimulationMap.get(simulationId).simulationPause();
        }
        else if (resume) {
            worldsSimulationMap.get(simulationId).simulationContinue();
        }
        else if(stop){
            worldsSimulationMap.get(simulationId).simulationStop();
        }
    }
    public void addSimulationToSimulationsMap(int simulationId,World simWorld){
        worldsSimulationMap.put(simulationId,simWorld);
    }
    public String getRuningStatus(int simulationId){
        if (!worldsSimulationMap.get(simulationId).getSimulationExecutionDetails().isRunning()) {
            return "FINISH";
        }
        else if( worldsSimulationMap.get(simulationId).isStop()){
            return "STOP";
        } else if (worldsSimulationMap.get(simulationId).isPause()) {
            return "PAUSE";
        } else  {
            return "START";
        }

    }
}
