package world.rule.action.type.simple;

import exception.FormatException;
import exception.InvalidAction;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.property.Property;
import world.rule.action.Execute;
import world.rule.action.expression.ManagementExpression;
import world.rule.action.type.complex.condition.Condition;

import java.io.Serializable;

public class Decrease extends SimpleAction implements Execute , Serializable {
    public Decrease(String mainEntityInstance, Property mainEntityProperty, Object expression,String stringExpression,EntityDefinition secondryEntity, String count, Condition condition) throws FormatException {
        super(mainEntityInstance, mainEntityProperty, expression,stringExpression, secondryEntity,  count,  condition);
        if (!(super.getExpressionType().equals(Float.class.getSimpleName())) && !(super.getExpressionType().equals(Integer.class.getSimpleName())) && !(super.getExpressionType().equals(Double.class.getSimpleName()))) {
            throw new FormatException("In the decrease action, on the main entity:"+super.getMainEntity()+" the expression: "+super.getExpression().toString()+" isn't numeric!");
        }
    }

    @Override
    public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance seconderyEntityInstance) throws InvalidAction, ValueNotExists, FormatException {
        Property entityProperty = getMainEntityProperty();
        super.setExpression(ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(),getStringExpression(),mainEntityInstance,seconderyEntityInstance,currTick));
        entityProperty= mainEntityInstance.getEntityProperties().get(entityProperty.getPropertyName());

        if (entityProperty.getValue().getClass().getSimpleName().equals(Integer.class.getSimpleName())) {
            if (super.getExpressionType().equals(Integer.class.getSimpleName())) {
                entityProperty.setValue((int) entityProperty.getValue() - (int) getExpression(),currTick);
            } else {
                throw new FormatException("In the decrease action the property: " + entityProperty.getPropertyName() + " is an integer but the expression isn't!");
            }
        } else if (entityProperty.getValue().getClass().getSimpleName().equals(Float.class.getSimpleName())) {
            if (super.getExpressionType().equals(Float.class.getSimpleName()) || super.getExpressionType().equals(Integer.class.getSimpleName())) {
                float floatExpression;
                float propertyFloatValue= (Float)entityProperty.getValue();
                if(super.getExpressionType().equals(Integer.class.getSimpleName())){
                    Integer intValue = (Integer) getExpression();
                    floatExpression = (float) intValue;
                }
                else {
                    floatExpression= (Float)getExpression();
                }
                float res=propertyFloatValue - floatExpression;
                entityProperty.setValue(res,currTick);
            } else {
                throw new FormatException("In the decrease action the property: " + entityProperty.getPropertyName() + "is a float one but the expression is not numeric!");
            }
        } else {
            throw new FormatException("In the decrease action the property: " + entityProperty.getPropertyName() + " isn't numeric!");
        }
    }
    @Override
    public String toString() {
        return (super.toString() + "decrease");
    }
    public String getData() {
        if (super.getSecondryEntity() != null) {
            return "Action Type : Decrease\nMain entity : " + super.getMainEntity() + "\nSecondary entity : "+super.getSecondryEntity().getEntityName()+"\nProperty : "+super.getMainEntityProperty().getPropertyName()+"\nBy : \n"+super.getStringExpression();
        }
        else {
            return "Action Type : Decrease\nMain entity : " + super.getMainEntity() + "\nProperty : "+super.getMainEntityProperty().getPropertyName()+"\nBy : \n"+super.getStringExpression();
        }
    }
}


