package world.rule.action.function;

import exception.InvalidAction;
import exception.ValueNotExists;
import world.property.Property;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

public class functions implements Serializable {

    public static Object environment(Map<String, Property> environmentProperties, String environmentName) throws InvalidAction, ValueNotExists {
        if (environmentName != null) {
            if (environmentProperties.containsKey(environmentName)) {
                return environmentProperties.get(environmentName).getValue();
            } else {
                throw new ValueNotExists("The environment's properties doesn't contain: " + environmentName + "!");
            }
        } else {
            throw new InvalidAction("The environment expression didn't receive a value!");
        }
    }

    public static int random(int number) {
        Random random = new Random();
        return random.nextInt(number + 1);
    }
}

