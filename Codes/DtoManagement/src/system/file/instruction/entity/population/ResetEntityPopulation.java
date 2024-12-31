package system.file.instruction.entity.population;

import world.entity.definition.EntityDefinition;

import java.util.Map;

public class ResetEntityPopulation {
    Map<String,EntityDefinition> entitiesMap;

    public ResetEntityPopulation(Map<String, EntityDefinition> entitiesMap) {
        this.entitiesMap = entitiesMap;
    }
    public void resetPopulation(){
        for (  Map.Entry<String,EntityDefinition> currEntity : entitiesMap.entrySet() ){
            currEntity.getValue().setUserInputPopulation(false);
            currEntity.getValue().setPopulation(0);
        }
    }
}
