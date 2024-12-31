package world.rule.action;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.execution.EntityInstance;
import world.environment.Environment;

public interface Execute {
    public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue;
}
