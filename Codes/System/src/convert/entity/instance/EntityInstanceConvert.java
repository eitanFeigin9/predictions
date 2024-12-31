package convert.entity.instance;

import convert.entity.property.PropertyConvert;

import exception.*;
import schema.ex2.PRDEntity;
import schema.ex2.PRDProperty;
import world.entity.execution.EntityInstance;
import world.property.Property;

import java.util.Map;

public class EntityInstanceConvert {
    private final int RESET_ID=1;
    private PRDEntity newEntity;

    public EntityInstanceConvert(PRDEntity newEntity) {
        this.newEntity = newEntity;
    }


    public EntityInstance convertEntityPrdToEntity(int id) throws RangeException, FormatException, ValueExists, InvalidAction, InvalidValue {
        EntityInstance newEntityInstance = new EntityInstance(newEntity.getName());
        if(id==RESET_ID)
        {
            newEntityInstance.resetId();
        }

        Map<String, Property> propertiesMap = newEntityInstance.getEntityProperties();
        for (PRDProperty property : newEntity.getPRDProperties().getPRDProperty()) {
            if (!propertiesMap.containsKey(property.getPRDName())) {
                newEntityInstance.addProperty(new PropertyConvert(property).convertPrdPropertyToProperty());
            } else {
                throw new ValueExists("In the entity: " + newEntity.getName() + " the property: " + property.getPRDName() + " is already exists!");
            }

        }
        return newEntityInstance;
    }
}
