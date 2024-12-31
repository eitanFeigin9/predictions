package world.rule.action.type.complex.condition.singlurty.single;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.property.Property;
import world.rule.action.Action;
import world.rule.action.expression.ManagementExpression;
import world.rule.action.type.complex.condition.Condition;
import world.rule.action.type.complex.condition.ConditionType;

import java.util.ArrayList;
import java.util.List;

public class SingleCondition extends Condition {

    private Object propertyExpression;
    private ComparatorType comparatorType;
    private Object expression;
    private String stringExpression;
    private String propertyStringExpression;


    public SingleCondition(ConditionType type, String mainEntityInstance, Object propertyExpression, ComparatorType comparatorType, Object expression,String stringExpression,EntityDefinition secondryEntity, String count, Condition seconderyCondition,String propertyStringExpression) {
        super(type, mainEntityInstance,secondryEntity,count,seconderyCondition);
        this.propertyExpression = propertyExpression;
        this.comparatorType = comparatorType;
        this.expression = expression;
        this.stringExpression=stringExpression;
        this.propertyStringExpression=propertyStringExpression;
    }


    public Object getPropertyExpression() {
        return propertyExpression;
    }

    public ComparatorType getComparatorType() {
        return comparatorType;
    }

    public Object getExpression() {
        return expression;
    }


    @Override
    public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance seconderyEntityInstance) throws InvalidAction, ValueNotExists, FormatException, InvalidValue {
       this.expression=(ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(),stringExpression,mainEntityInstance,seconderyEntityInstance,currTick));
        this.propertyExpression=(ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(),propertyStringExpression,mainEntityInstance,seconderyEntityInstance,currTick));
        if(isThanExecute(mainEntityInstance,propertyExpression,comparatorType,expression)){
            executeActions(mainEntityInstance,super.getThanActions(),environment,currTick);
        }
        else {
            executeActions(mainEntityInstance,super.getElseActions(),environment,currTick);
        }

    }

    public void executeActions (EntityInstance mainEntityInstance,List<Action> list,Environment environment,int currTick) throws InvalidAction, ValueNotExists, FormatException, InvalidValue {
        for(Action currAction: list){
            currAction.calc(mainEntityInstance,environment,currTick);
        }
    }
    public String getData() {
        if (super.getSecondryEntity() != null) {
            return "Action Type : Simple condition\nMain entity : " + super.getMainEntity() + "\nSecondary entity : "+super.getSecondryEntity().getEntityName()+"\nProperty : \n"+propertyStringExpression+"\nComparator : "+comparatorType.getSymbol()+"\nValue : \n"+stringExpression+"\nAmount of then actions : "+super.getThanActions().size()+"\nAmount of else actions : "+super.getElseActions().size();
        }
        else {
            return "Action Type : Simple condition\nMain entity : " + super.getMainEntity() +"\nProperty : "+propertyStringExpression+"\nComparator : "+comparatorType.getSymbol()+"\nValue : \n"+stringExpression+"\nAmount of then actions : "+super.getThanActions().size()+"\nAmount of else actions : "+super.getElseActions().size();
        }
    }
    }





