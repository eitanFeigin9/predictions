package system.file.instruction.entity.population;

import world.entity.definition.EntityDefinition;

import java.util.Map;

public class SetEntityPopulation {
    private Map<String, EntityDefinition> entitiesMap;

    public SetEntityPopulation(Map<String, EntityDefinition> entitiesMap) {
        this.entitiesMap = entitiesMap;
    }
    public PopulationUpdatesDto setPopulation(int populationSize, String entityName, int currDimension){
        int currEntityPopulation= entitiesMap.get(entityName).getPopulation();
         if(currDimension-(populationSize-currEntityPopulation)>=0){
            entitiesMap.get(entityName).setPopulation(populationSize);
            return new PopulationUpdatesDto(true,"The entity population has set!",currDimension-(populationSize-currEntityPopulation));
        }
        else {
            return new PopulationUpdatesDto(false,"The entity population hasn't set!\nThere is not enough space in the dimension!",currDimension);

        }
    }
}
