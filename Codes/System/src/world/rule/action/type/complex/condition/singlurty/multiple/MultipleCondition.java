package world.rule.action.type.complex.condition.singlurty.multiple;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.action.Action;
import world.rule.action.Execute;
import world.rule.action.type.complex.condition.Condition;
import world.rule.action.type.complex.condition.ConditionType;
import world.rule.action.type.complex.condition.singlurty.single.ComparatorType;
import world.rule.action.type.complex.condition.singlurty.single.SingleCondition;

import java.util.ArrayList;
import java.util.List;

public class MultipleCondition extends Condition implements Execute {
    private  LogicalType logicalType;
    private List<Condition> conditionList;


    public MultipleCondition(ConditionType type, String mainEntityInstance, LogicalType logicalType, EntityDefinition secondryEntity, String count, Condition seconderyCondition) {
        super(type, mainEntityInstance,secondryEntity,count,seconderyCondition);
        this.logicalType=logicalType;
        this.conditionList=new ArrayList<>();
    }
    public boolean isConditionApplied(EntityInstance mainEntityInstance) throws InvalidAction, FormatException {
        for(Condition currCondition: conditionList) {
            if (logicalType.getLogic().equals(LogicalType.AND.getLogic())) {
                if (currCondition.getType().getType().equals(ConditionType.SINGLE.getType())) {

                    SingleCondition currSingle = (SingleCondition) currCondition;
                    if (!currCondition.isThanExecute(mainEntityInstance, currSingle.getPropertyExpression(), ((SingleCondition) currCondition).getComparatorType(), currSingle.getExpression())) {
                        return false;
                    }
                }
                else if (currCondition.getType().getType().equals(ConditionType.MULTIPLE.getType())) {
                    MultipleCondition currMultiple = (MultipleCondition) currCondition;
                    if(!currMultiple.isConditionApplied(mainEntityInstance)){
                        return false;
                    }
                }
                else {
                    throw new FormatException("In the multiple condition on the entity: " + mainEntityInstance.getNameOfEntity() + " there is a condition with a wrong singularity format (" + currCondition.getType().getType() + ")!\nThe valid formats are:(multiple,single).");
                }
            }
            else if (logicalType.getLogic().equals(LogicalType.OR.getLogic())) {
                if (currCondition.getType().getType().equals(ConditionType.SINGLE.getType())) {
                    SingleCondition currSingle = (SingleCondition) currCondition;
                    if (currCondition.isThanExecute(mainEntityInstance, currSingle.getPropertyExpression(), ((SingleCondition) currCondition).getComparatorType(), currSingle.getExpression())) {
                        return true;
                    }
                }
                else if (currCondition.getType().getType().equals(ConditionType.MULTIPLE.getType())) {
                    MultipleCondition currMultiple = (MultipleCondition) currCondition;
                    if(currMultiple.isConditionApplied(mainEntityInstance)){
                        return true;
                    }
                }
                else {
                    throw new FormatException("In the multiple condition on the entity: " + mainEntityInstance.getNameOfEntity() + " there is a condition with a wrong singularity format (" + currCondition.getType().getType() + ")!\nThe valid formats are: multiple,single.");
                }
            }
            else {
                throw new FormatException("In the multiple condition on the entity: " + mainEntityInstance.getNameOfEntity() + " there is a logic condition with a wrong format (" + logicalType.getLogic() + ")!\nThe valid formats are: and,or.");
            }
        }

        return !logicalType.getLogic().equals(LogicalType.OR.getLogic());

    }

    @Override
     public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTick,EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue{
            if(isConditionApplied(mainEntityInstance))
            {
                for (Action currAction:super.getThanActions())
                {
                    currAction.calc(mainEntityInstance,environment,currTick);
                }
            }
            else {
                for (Action currAction:super.getElseActions())
                {
                    currAction.calc(mainEntityInstance,environment,currTick);
                }
            }
    }
    public void addCondition(Condition newCondition){
        conditionList.add(newCondition);
    }
    public String getData() {
        if (super.getSecondryEntity() != null) {
            return "Action Type : Multiple condition\nMain entity : " + super.getMainEntity() + "\nSecondary entity : "+super.getSecondryEntity().getEntityName()+"\nLogical : "+logicalType.getLogic()+"\nAmount of then actions : "+super.getThanActions().size()+"\nAmount of else actions : "+super.getElseActions().size()+"\nAmount of conditions : "+conditionList.size();
        }
        else {
            return "Action Type : Multiple condition\nMain entity : " + super.getMainEntity() + "\nLogical : "+logicalType.getLogic()+"\nAmount of then actions : "+super.getThanActions().size()+"\nAmount of else actions : "+super.getElseActions().size()+"\nAmount of conditions : "+conditionList.size();
        }
    }
}
