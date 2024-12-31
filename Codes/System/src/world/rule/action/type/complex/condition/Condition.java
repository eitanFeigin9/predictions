package world.rule.action.type.complex.condition;

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
import world.rule.action.type.complex.condition.singlurty.single.ComparatorType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract public class Condition extends Action implements Execute , Serializable {

    private final boolean THAN_EXECUTE= true;
    private final boolean ELSE_EXECUTE= false;
    private ConditionType type;
    private List<Action> thanActions;
    private List<Action> elseActions;
    public Condition(ConditionType type, String mainEntityInstance, EntityDefinition secondryEntity, String count, Condition seconderyCondition) {
        super(mainEntityInstance,secondryEntity,count,seconderyCondition);
        this.type=type;
        this.elseActions = new ArrayList<>();
        this.thanActions = new ArrayList<>();
    }

    public List<Action> getThanActions() {
        return thanActions;
    }

    public List<Action> getElseActions() {
        return elseActions;
    }

    @Override
    abstract public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue;

    public ConditionType getType() {
        return type;
    }
    public void addToList(List<Action> list, Action newAction) {
        list.add(newAction);
    }

    public boolean isThanExecute(EntityInstance mainEntityInstance, Object currPropertyExpressionValue, ComparatorType comparatorType, Object expression) throws InvalidAction, FormatException {
        if (currPropertyExpressionValue.getClass().getSimpleName().equals(expression.getClass().getSimpleName()))
            if (ComparatorType.EQUALS.getSymbol().equals(comparatorType.getSymbol())) {
                if (currPropertyExpressionValue.equals(expression)) {
                    return THAN_EXECUTE;
                } else {
                    return ELSE_EXECUTE;
                }
            }
            else if (ComparatorType.NOT_EQUALS.getSymbol().equals(comparatorType.getSymbol())) {
                if (currPropertyExpressionValue.equals(expression)) {
                    return ELSE_EXECUTE;
                }
                else {
                    return THAN_EXECUTE;
                }
            }
            else if (ComparatorType.GREATER_THAN.getSymbol().equals(comparatorType.getSymbol())) {
                if (currPropertyExpressionValue.getClass().getSimpleName().equals(Integer.class.getSimpleName())) {
                    if ((int)currPropertyExpressionValue > (int) expression) {
                        return THAN_EXECUTE;
                    } else {
                        return ELSE_EXECUTE;
                    }
                } else if (currPropertyExpressionValue.getClass().getSimpleName().equals(Float.class.getSimpleName())) {
                    if ((float) currPropertyExpressionValue > (float) expression) {
                        return THAN_EXECUTE;
                    } else {
                        return ELSE_EXECUTE;
                    }
                } else {
                    throw new FormatException("The condition action that checks if the property is bigger than the expression isn't possible because they are not numeric!");
                }
            }
            else if (ComparatorType.LESS_THAN.getSymbol().equals(comparatorType.getSymbol())) {
                if (currPropertyExpressionValue.getClass().getSimpleName().equals(Integer.class.getSimpleName())) {
                    if ((int) currPropertyExpressionValue < (int) expression) {
                        return THAN_EXECUTE;
                    }
                    else {
                        return ELSE_EXECUTE;
                    }
                }
                else if (currPropertyExpressionValue.getClass().getSimpleName().equals(Float.class.getSimpleName())) {
                    if ((float) currPropertyExpressionValue < (float) expression) {
                        return THAN_EXECUTE;

                    } else {
                        return ELSE_EXECUTE;
                    }
                }
                else {
                    throw new FormatException("The condition action that checks if the property is lower than the expression isn't possible because they are not numeric!");
                }
            }
            else {
                throw new InvalidAction("The condition action that use the operator: " + comparatorType + " isn't valid!\nThe valid comparators are:=,!=,bt,lt.");
            }
        else if(currPropertyExpressionValue.getClass().getSimpleName().equals(Integer.class.getSimpleName())||currPropertyExpressionValue.getClass().getSimpleName().equals(Float.class.getSimpleName())){
           float propertyExpressionValue;
           float expressionValue;
           if(currPropertyExpressionValue.getClass().getSimpleName().equals(Integer.class.getSimpleName())){
               Integer intPropValue=(Integer) currPropertyExpressionValue;
               propertyExpressionValue=(float)intPropValue;
           }
           else {propertyExpressionValue= (float) currPropertyExpressionValue;
           }


            if(expression.getClass().getSimpleName().equals(Float.class.getSimpleName())||expression.getClass().getSimpleName().equals(Integer.class.getSimpleName())){
                if(expression.getClass().getSimpleName().equals(Integer.class.getSimpleName())){
                    Integer intPropValue=(Integer) expression;
                    expressionValue=(float)intPropValue;
                }
                else {expressionValue= (float) expression;
                }
                if(ComparatorType.EQUALS.getSymbol().equals(comparatorType.getSymbol())){
                    if(propertyExpressionValue==expressionValue){
                        return THAN_EXECUTE;

                    }
                    else {
                        return ELSE_EXECUTE;
                    }
                }
                else if (ComparatorType.NOT_EQUALS.getSymbol().equals(comparatorType.getSymbol())) {
                    if(propertyExpressionValue==expressionValue){
                        return ELSE_EXECUTE;

                    }
                    else {
                        return THAN_EXECUTE;
                    }
                }
                else if (ComparatorType.GREATER_THAN.getSymbol().equals(comparatorType.getSymbol())) {
                    if(propertyExpressionValue>expressionValue){
                        return THAN_EXECUTE;

                    }
                    else {
                        return ELSE_EXECUTE;
                    }
                }
                else if (ComparatorType.LESS_THAN.getSymbol().equals(comparatorType.getSymbol())) {
                    if(propertyExpressionValue<expressionValue){
                        return THAN_EXECUTE;
                    }
                    else {
                        return ELSE_EXECUTE;
                    }
                }
                else { throw new InvalidAction("The condition action that use the operator: " +comparatorType + " isn't valid!\nThe valid comparators are:=,!=,bt,lt.");}
            }
            else {
                throw new FormatException("There condition action that on the: "+mainEntityInstance.getNameOfEntity()+ " that its property's value ("+currPropertyExpressionValue+") is an numeric but the expression isn't numeric!");}
        }
        else {
            throw new InvalidAction("There condition action that on the: "+mainEntityInstance.getNameOfEntity()+ " that its property's value ("+currPropertyExpressionValue+") doesn't comparable to the expression!");
        }
    }
    @Override
    public String toString() {
        return (super.toString() + "condition");
    }

}
