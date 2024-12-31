package system.file.instruction.show.Manage;

import world.World;
import world.entity.definition.EntityDefinition;
import world.property.Property;

import java.util.LinkedHashMap;
import java.util.Map;

public class CreateEnvironmentMap {
    public Map<String, Map<String, String>>  getEnvironmentMap(World world){
        Map<String, Map<String, String>> result=new LinkedHashMap<>();
        for (Map.Entry<String, Property> entry : world.getEnvironment().getEnvironmentProperties().entrySet()) {
            result.put(entry.getKey(), new LinkedHashMap<>());
            for (Map.Entry<String, String> currProp : entry.getValue().getProperyMap().entrySet()) {
                if(!currProp.getKey().equals("Random initialize?\n")) {
                    result.get(entry.getKey()).put(currProp.getKey(), currProp.getValue());
                }
            }
        }
        return result;
    }
    public Map<String, Map<String, String>>  getEnvironmentProperitesMap(World world){
        Map<String, Map<String, String>> result=new LinkedHashMap<>();
        for (Map.Entry<String, Property> entry : world.getEnvironment().getEnvironmentProperties().entrySet()) {
            result.put(entry.getKey(), new LinkedHashMap<>());
            for (Map.Entry<String, String> currProp : entry.getValue().getEnvProperyMap().entrySet()) {
                    result.get(entry.getKey()).put(currProp.getKey(), currProp.getValue());
            }
        }
        return result;
    }
    public Map<String,  String>  getEnvironmentProperitesValues(World world){
        Map<String,  String>  result=new LinkedHashMap<>();
        for (Map.Entry<String, Property> entry : world.getEnvironment().getEnvironmentProperties().entrySet()) {
            result.put(entry.getKey(),entry.getValue().getValue().toString()) ;
        }
        return result;
    }
}
