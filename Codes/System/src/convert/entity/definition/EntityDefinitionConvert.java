package convert.entity.definition;

import convert.entity.instance.EntityInstanceConvert;
import exception.*;
import schema.ex2.PRDEntity;
import world.entity.definition.EntityDefinition;

public class EntityDefinitionConvert {
    private PRDEntity newEntity;

    public EntityDefinitionConvert(PRDEntity newEntity) {
        this.newEntity = newEntity;
    }


    public EntityDefinition createEntityDefinition() throws RangeException, ValueExists, FormatException, InvalidAction, InvalidValue {
        if(newEntity.getName().contains(" "))
        {
            throw new InvalidValue("The entity name must be without spaces!\nTherefore the entity name: "+newEntity.getName()+" isn't valid!");
        }
        EntityDefinition newEntityDefinition=new EntityDefinition(newEntity.getName(),0);
        EntityInstanceConvert newInstance = new EntityInstanceConvert(this.newEntity);
        newEntityDefinition.addEntity(newInstance.convertEntityPrdToEntity(1));
        return newEntityDefinition;
    }

}
