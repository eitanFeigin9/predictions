package world.rule.action.type.simple;

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
import java.util.List;

abstract public class SimpleAction extends Action implements Execute , Serializable {
    private Property mainEntityProperty;
    private Object expression;
    private String stringExpression;

    public SimpleAction(String mainEntityInstance, Property mainEntityProperty, Object expression,String stringExpression,EntityDefinition secondryEntity, String count, Condition seconderyCondition) {
        super(mainEntityInstance, secondryEntity,  count,  seconderyCondition);
        this.mainEntityProperty = mainEntityProperty;
        this.expression = expression;
        this.stringExpression=stringExpression;
    }

    public String getStringExpression() {
        return stringExpression;
    }

    public Property getMainEntityProperty() {
        return mainEntityProperty;
    }

    public Object getExpression() {
        return expression;
    }

    public void setExpression(Object expression) {
        this.expression = expression;
    }

    @Override
    abstract public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue;

    public String getExpressionType() {
        return expression.getClass().getSimpleName();
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
