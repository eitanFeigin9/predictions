package world.rule.action;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.action.type.complex.condition.Condition;
import world.rule.action.type.complex.condition.ConditionType;
import world.rule.action.type.complex.condition.singlurty.multiple.MultipleCondition;
import world.rule.action.type.complex.condition.singlurty.single.SingleCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

abstract public class Action implements Execute , Serializable {
    private String mainEntityName;
    private EntityDefinition secondryEntity;
    private String count;
    private Condition seconderyCondition;

    public Action(String mainEntityName, EntityDefinition secondryEntity, String count, Condition seconderyCondition) {
        this.mainEntityName = mainEntityName;
        this.secondryEntity=secondryEntity;
        this.count = count;
        this.seconderyCondition=seconderyCondition;
    }

    public String getMainEntity() {
        return mainEntityName;
    }

    @Override
    abstract public void executeAction(EntityInstance mainEntityInstance, Environment environment,int currTicks,EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue;
    @Override
    public String toString() {
        return "    Type: ";
    }

    public EntityDefinition getSecondryEntity() {
        return secondryEntity;
    }
    public void calc(EntityInstance mainEntityInstance, Environment environment,int currTick) throws FormatException, InvalidAction, ValueNotExists, InvalidValue {
        EntityDefinition seconderyEntityDef= getSecondryEntity();
        if(seconderyEntityDef!=null) {
            Map<Integer, EntityInstance> seconderyEntitesMap = seconderyEntityDef.getEntities();
            List<EntityInstance> executableEntitesList = new ArrayList<>();
            Condition seconderyCondition = getSeconderyCondition();
            if(seconderyCondition!=null){
            for (Map.Entry<Integer, EntityInstance> currSecondaryInstance : seconderyEntitesMap.entrySet()) {
                if (seconderyCondition.getType().getType().equals(ConditionType.SINGLE.getType())) {
                    assert seconderyCondition instanceof SingleCondition;
                    SingleCondition currCondition = (SingleCondition) seconderyCondition;
                    if (seconderyCondition.isThanExecute(currSecondaryInstance.getValue(), currCondition.getPropertyExpression(), currCondition.getComparatorType(), currCondition.getExpression())) {
                        executableEntitesList.add(currSecondaryInstance.getValue());
                    }
                } else if (seconderyCondition.getType().getType().equals(ConditionType.MULTIPLE.getType())) {
                    assert seconderyCondition instanceof MultipleCondition;
                    MultipleCondition currCondition = (MultipleCondition) seconderyCondition;
                    if (currCondition.isConditionApplied(currSecondaryInstance.getValue())) {
                        executableEntitesList.add(currSecondaryInstance.getValue());
                    }

                } else {
                    throw new FormatException("The type of the condition on the secondary entity: " + currSecondaryInstance.getValue().getNameOfEntity() + " isn't in the type of single or multiple condition!");
                }

            }
        }
            else {
                executableEntitesList = new ArrayList<>(seconderyEntitesMap.values());
            }
            List<EntityInstance> resExecutableEntitesList = new ArrayList<>();
            if(getCount().equals("ALL")){
                resExecutableEntitesList = new ArrayList<>(executableEntitesList);
            }
            else{
                try {
                    int intValue = Integer.parseInt(getCount()); // Try to parse the string to int
                    if(intValue<=0){
                        throw new InvalidValue("On the secondary entity: " + seconderyEntityDef.getEntityName() +" the count isn't a positive number ("+intValue+")");
                    }
                    else if(intValue>executableEntitesList.size()) {
                        resExecutableEntitesList = new ArrayList<>(executableEntitesList);
                    }
                    else {
                        for (int i=0;i<intValue;i++){
                            Random random = new Random();
                            int randomIndex = random.nextInt(executableEntitesList.size());
                            resExecutableEntitesList.add(executableEntitesList.get(randomIndex)) ;
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new FormatException("On the secondary entity: " + seconderyEntityDef.getEntityName() +" the count ("+getCount()+") isn't a number or the word 'ALL'!");
                }

            }
            for (EntityInstance currEntityInstance : resExecutableEntitesList) {
                executeAction(mainEntityInstance,environment,currTick,currEntityInstance);
            }
        }
        else {
            executeAction(mainEntityInstance,environment,currTick,null);
        }


    }
    public String getCount() {
        return count;
    }

    public Condition getSeconderyCondition() {
        return seconderyCondition;
    }

}
