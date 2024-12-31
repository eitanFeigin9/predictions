package world.rule.action.type.simple;

import exception.FormatException;
import exception.InvalidAction;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.action.Action;
import world.rule.action.Execute;
import world.rule.action.type.complex.condition.Condition;

import java.io.Serializable;

public class Kill extends Action implements Execute, Serializable {
    private final boolean ENTITY_NOT_ALIVE = false;
    private EntityDefinition entityDefinition;
    public Kill(String mainEntityInstance, EntityDefinition mainEntityDefinition, EntityDefinition secondryEntity, String count, Condition seconderyCondition) {
        super(mainEntityInstance,secondryEntity,count,seconderyCondition);
        this.entityDefinition=mainEntityDefinition;
    }
    @Override
    public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance seconderyEntityInstance) throws InvalidAction, ValueNotExists, FormatException {
        mainEntityInstance.setAlive(ENTITY_NOT_ALIVE);
    }
    public String getData() {
        if (super.getSecondryEntity() != null) {
            return "Action Type : Kill\nMain entity : " + super.getMainEntity() + "\nSecondary entity : "+super.getSecondryEntity().getEntityName();
        }
        else {
            return "Action Type : Kill\nMain entity : " + super.getMainEntity();
        }
    }
    @Override
    public String toString() {
        return (super.toString() + "kill");
    }


}
