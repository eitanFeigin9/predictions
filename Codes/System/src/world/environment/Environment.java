package world.environment;

import world.property.Property;

import java.io.Serializable;
import java.util.*;

public class Environment implements Serializable {
    private Map<String,Property> environmentProperties;

    public Environment() {
        this.environmentProperties = new LinkedHashMap<>();
    }

    public Map<String, Property> getEnvironmentProperties() {
        return environmentProperties;
    }

    public void addProperty(Property newProperty) {
        environmentProperties.put(newProperty.getPropertyName(), newProperty);
    }
}
