package world.rule.action.type.simple;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.property.Property;
import world.rule.action.Execute;
import world.rule.action.expression.ManagementExpression;
import world.rule.action.type.complex.condition.Condition;
import world.rule.action.type.complex.condition.ConditionType;
import world.rule.action.type.complex.condition.singlurty.multiple.MultipleCondition;
import world.rule.action.type.complex.condition.singlurty.single.SingleCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Set extends SimpleAction implements Execute , Serializable {
    public Set(String mainEntityInstance, Property mainEntityProperty, Object expression,String stringExpression,EntityDefinition secondryEntity, String count, Condition condition) {
        super(mainEntityInstance, mainEntityProperty, expression,stringExpression, secondryEntity,  count,  condition);
    }


    @Override
    public String toString() {
        return (super.toString() + "set");
    }
    @Override
    public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance seconderyEntityInstance) throws InvalidAction, ValueNotExists, FormatException {
        Property entityProperty = getMainEntityProperty();
        super.setExpression(ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(),getStringExpression(),mainEntityInstance,seconderyEntityInstance,currTick));
        entityProperty= mainEntityInstance.getEntityProperties().get(entityProperty.getPropertyName());
        if (entityProperty.getValue().getClass().getSimpleName().equals(Float.class.getSimpleName())) {
            if (super.getExpressionType().equals(Float.class.getSimpleName()) || super.getExpressionType().equals(Integer.class.getSimpleName())) {
                float floatExpression;
                if(super.getExpressionType().equals(Integer.class.getSimpleName())){
                    Integer intValue = (Integer) getExpression();
                    floatExpression = (float) intValue;
                }
                else {
                    floatExpression= (Float)getExpression();
                }
                entityProperty.setValue(floatExpression,currTick);
            } else {
                throw new FormatException("In the set action the property: " + entityProperty.getPropertyName() + " is a float one but the expression " + super.getExpression().toString() + "  isn't numeric");
            }
        } else if (entityProperty.getValue().getClass().getSimpleName().equals(Integer.class.getSimpleName())) {
            if (super.getExpressionType().equals(Integer.class.getSimpleName())) {
                entityProperty.setValue((int) getExpression(),currTick);
            } else {
                throw new FormatException("In the set action the property: " + entityProperty.getPropertyName() + " is an integer one but the expression " + super.getExpression().toString() + "  isn't an integer");
            }
        } else if (super.getExpressionType().equals(entityProperty.getValue().getClass().getSimpleName())) {
            entityProperty.setValue(getExpression(),currTick);
        } else {
            throw new FormatException("In the set action the property: " + entityProperty.getPropertyName() + " isn't the same type as the expression");
        }
    }
    public String getData() {
        if (super.getSecondryEntity() != null) {
            return "Action Type : Set\nMain entity : " + super.getMainEntity() + "\nSecondary entity : "+super.getSecondryEntity().getEntityName()+"\nProperty : "+super.getMainEntityProperty().getPropertyName()+"\nValue : \n"+super.getStringExpression();
        }
        else {
            return "Action Type : Set\nMain entity : " + super.getMainEntity() + "\nProperty : "+super.getMainEntityProperty().getPropertyName()+"\nValue : \n"+super.getStringExpression();
        }
    }
}

