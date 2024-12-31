package convert.environment;
import convert.environment.property.EnvPropertyConvert;
import exception.FormatException;
import exception.InvalidValue;
import exception.RangeException;
import exception.ValueExists;
import schema.ex2.PRDEnvProperty;
import schema.ex2.PRDEnvironment;
import world.environment.Environment;

public class EnvironmentConvert {
private PRDEnvironment prdEvironment;

    public EnvironmentConvert(PRDEnvironment newEnvironment) {
        this.prdEvironment = newEnvironment;
    }

    public Environment createNewEnvironment() throws RangeException, FormatException, ValueExists, InvalidValue {
        Environment newEnvironment = new Environment();
        for (PRDEnvProperty property : prdEvironment.getPRDEnvProperty()) {
            if (!newEnvironment.getEnvironmentProperties().containsKey(property.getPRDName())) {
                newEnvironment.addProperty(new EnvPropertyConvert(property).convertPrdEnvPropertyToProperty());
            } else {
                throw new ValueExists("In the evironment, the property: " + property.getPRDName() + " is already exists!");
            }
        }
        return newEnvironment;
    }
}
