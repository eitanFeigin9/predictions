package world.rule.action.type.complex.calculation.manage;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.property.Property;
import world.rule.action.Action;
import world.rule.action.Execute;
import world.rule.action.type.complex.condition.Condition;

import java.io.Serializable;
import java.util.Map;

abstract public class Calculation extends Action implements Execute, Serializable {

    private Property resultProp;
    private Map<String, Property > environmentProperties;
    private Object arg1;
    private Object arg2;
    private String stringExpression1;
    private String stringExpression2;
    public Calculation(Map<String, Property > environmentProperties, Property resultProp, Object arg1, Object arg2, String mainEntityInstance,String stringExpression1,String stringExpression2, EntityDefinition secondryEntity, String count, Condition seconderyCondition) {
        super(mainEntityInstance,secondryEntity,count,seconderyCondition);
        this.resultProp=resultProp;
        this.environmentProperties=environmentProperties;
        this.arg1=arg1;
        this.arg2=arg2;
        this.stringExpression1=stringExpression1;
        this.stringExpression2=stringExpression2;
    }

    public Property getResultProp() {
        return resultProp;
    }

    public Object getArg1() {
        return arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public String getStringExpression1() {
        return stringExpression1;
    }

    public String getStringExpression2() {
        return stringExpression2;
    }

    public Map<String, Property> getEnvironmentProperties() {
        return environmentProperties;
    }

    @Override
    abstract public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue;
    @Override
    public String toString() {
        return (super.toString() + "calculation");
    }
}
