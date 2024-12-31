package system.file.instruction.environment;

import world.environment.Environment;
import world.property.Property;

import java.util.Map;

public class envValuesActions {
    private Environment currEnv;

    public envValuesActions(Environment currEnv) {
        this.currEnv = currEnv;
    }
    public void resetEnvPropValue() {
        Map<String, Property> environmentPropertiesMap = this.currEnv.getEnvironmentProperties();
        for (Map.Entry<String, Property> currProp : environmentPropertiesMap.entrySet()) {
            currProp.getValue().setUserEnterValue(false);
        }
    }
    public void setEnvPropValue(){
            Map<String, Property > environmentPropertiesMap=this.currEnv.getEnvironmentProperties();
            for( Map.Entry<String, Property > currProp : environmentPropertiesMap.entrySet()){
                if(!currProp.getValue().isUserEnterValue())
                {
                    currProp.getValue().setRandomValue();
                }
            }
    }
}
