package system.file.instruction.show.Manage;

import world.World;
import world.entity.definition.EntityDefinition;
import world.property.Property;

import java.util.LinkedHashMap;
import java.util.Map;

public class CreateEntitiesMap {
    public Map<String, Map<String, Map<String, String>>>  getEntitiesMap(World world){
        Map<String, Map<String, Map<String, String>>> result=new LinkedHashMap<>();
        for (Map.Entry<String, EntityDefinition> entry : world.getEntities().entrySet()) {
            result.put(entry.getKey(), new LinkedHashMap<>());
            for (Map.Entry<String, Property> currProp : entry.getValue().getEntities().get(0).getEntityProperties().entrySet()) {
                result.get(entry.getKey()).put(currProp.getKey(), currProp.getValue().getProperyMap());
            }
        }
        return result;
    }
    public Map<String,Integer>   getEntitiesPopulationMap(World world){
    Map<String,Integer> result=new LinkedHashMap<>();
        for (Map.Entry<String, EntityDefinition> entry : world.getEntities().entrySet()) {
            result.put(entry.getKey(),entry.getValue().getPopulation());
        }
        return result;
    }
    public Map<String,String>   getEntitiesPopulationByStringMap(World world){
        Map<String,String> result=new LinkedHashMap<>();
        for (Map.Entry<String, EntityDefinition> entry : world.getEntities().entrySet()) {
            result.put(entry.getKey(), String.valueOf(entry.getValue().getPopulation()));
        }
        return result;
    }
}
