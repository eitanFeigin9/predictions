package world.rule.action.type.complex.calculation.impl;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.property.Property;
import world.rule.action.Execute;
import world.rule.action.type.complex.calculation.manage.Calculation;
import world.rule.action.expression.ManagementExpression;
import world.rule.action.type.complex.condition.Condition;

import java.io.Serializable;
import java.util.Map;

public class Divide extends Calculation implements Execute , Serializable {


    public Divide(Map<String, Property> environmentProperties, Property resultProp, String arg1, String arg2, EntityInstance mainEntityInstance, EntityDefinition secondaryEntityDef , EntityInstance secondryEntity, String count, Condition seconderyCondition, int currTick) throws FormatException, InvalidAction, ValueNotExists {
        super(environmentProperties,resultProp,ManagementExpression.getExpressionValue(environmentProperties,arg1,mainEntityInstance,secondryEntity,currTick ),ManagementExpression.getExpressionValue(environmentProperties,arg2,mainEntityInstance,secondryEntity,currTick),mainEntityInstance.getNameOfEntity(),arg1,arg2,secondaryEntityDef,count,seconderyCondition);
        if ((!(super.getArg1().getClass().getSimpleName().equals(Float.class.getSimpleName()) || super.getArg1().getClass().getSimpleName().equals(Integer.class.getSimpleName())))||!(super.getArg2().getClass().getSimpleName().equals(Float.class.getSimpleName()) || super.getArg2().getClass().getSimpleName().equals(Integer.class.getSimpleName()))) {
            throw new FormatException("In the divide action one of the arguments isn't numeric!");
        }
    }

    @Override
    public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue {
        Object argument1=ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(),getStringExpression1(),mainEntityInstance,secondaryInstance,currTick);
        Object argument2=ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(),getStringExpression2(),mainEntityInstance,secondaryInstance,currTick);
        Property currProperty=mainEntityInstance.getEntityProperties().get(getResultProp().getPropertyName());
        if(currProperty.getValue().getClass().getSimpleName().equals("Integer")) {
            if (argument1.getClass().getSimpleName().equals("Integer") && argument2.getClass().getSimpleName().equals("Integer")) {
                if(((int) argument2)!=0){
                    currProperty.setValue((int) argument1 / (int) argument2,currTick);
            }
                else {
                    throw new InvalidAction("In the divide action on the entity: "+mainEntityInstance.getNameOfEntity()+" on the property: "+currProperty.getPropertyName()+" the second argument is equal to 0!");
                }
            }
            else {
                throw new FormatException("In the divide action on the entity: "+mainEntityInstance.getNameOfEntity()+" on the property: "+currProperty.getPropertyName()+" the arguments format should be as integer!");
            }
        }
        else if(currProperty.getValue().getClass().getSimpleName().equals("Float"))
        {
            if ((argument1.getClass().getSimpleName().equals("Integer")||argument1.getClass().getSimpleName().equals("Float")) && (argument2.getClass().getSimpleName().equals("Integer")||argument2.getClass().getSimpleName().equals("Float"))) {
                float arg1,arg2;
                if(argument1.getClass().getSimpleName().equals("Integer")){
                    int intArgument = (int)argument1;
                    arg1=(float) intArgument;
                }
                else{
                    arg1=(float) argument1;
                }
                if(argument2.getClass().getSimpleName().equals("Integer")){
                    int intArgument2 = (int)argument2;
                    arg2=(float) intArgument2;
                }
                else{
                    arg2=(float) argument1;
                }
                if (arg2 != 0.0) {
                    currProperty.setValue(arg1 / arg2,currTick);
                } else {
                    throw new InvalidAction("In the divide action on the entity: " + mainEntityInstance.getNameOfEntity() + " on the property: " +currProperty.getPropertyName() + " the second argument is equal to 0!");
                }
            }
            else {
                throw new FormatException("In the divide action on the entity: "+mainEntityInstance.getNameOfEntity()+" on the property: "+currProperty.getPropertyName()+" the arguments format should be as float!");
            }
        }
        else {
            throw new FormatException("The divide action on the entity: "+mainEntityInstance.getNameOfEntity()+" on the property: "+currProperty.getPropertyName()+" can't occur because the property isn't a integer or a float one.");
        }

    }
    public String getData() {
        if (super.getSecondryEntity() != null) {
            return "Action Type : Divide\nMain entity : " + super.getMainEntity() + "\nSecondary entity : "+super.getSecondryEntity().getEntityName()+"\nProperty : "+super.getResultProp().getPropertyName()+"\nArg1 : \n"+super.getStringExpression1()+"\nArg2 : \n"+super.getStringExpression2();
        }
        else {
            return "Action Type : Divide\nMain entity : \n" + super.getMainEntity() + "\nProperty : \n"+super.getResultProp().getPropertyName()+"\nArg1 : \n"+super.getStringExpression1()+"\nArg2 : \n"+super.getStringExpression2();
        }
    }
}
