package convert.action;
import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import schema.ex2.PRDAction;
import schema.ex2.PRDCondition;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.action.Action;
import world.rule.action.ActionType;
import world.rule.action.expression.ManagementExpression;
import world.rule.action.type.complex.calculation.impl.Divide;
import world.rule.action.type.complex.calculation.impl.Multiply;
import world.rule.action.type.complex.condition.Condition;
import world.rule.action.type.complex.condition.ConditionType;
import world.rule.action.type.complex.condition.singlurty.multiple.LogicalType;
import world.rule.action.type.complex.condition.singlurty.multiple.MultipleCondition;
import world.rule.action.type.complex.condition.singlurty.single.ComparatorType;
import world.rule.action.type.complex.condition.singlurty.single.SingleCondition;
import world.rule.action.type.multi.entities.Proxmity;
import world.rule.action.type.multi.entities.Replace;
import world.rule.action.type.simple.Decrease;
import world.rule.action.type.simple.Increase;
import world.rule.action.type.simple.Kill;
import world.rule.action.type.simple.Set;
import java.util.List;
import java.util.Map;

public class ActionConvert {

    private PRDAction prdAction;

    public ActionConvert(PRDAction prdAction)  {
        this.prdAction = prdAction;
    }

    public Action convertPrdActionToAction(Environment environment,EntityDefinition currEntityDefinition, EntityInstance currInstance,EntityDefinition secondaryEntity,EntityInstance[][] gridArray,Map<String,EntityDefinition> entityDefinitionMap,boolean isSubAction) throws ValueNotExists, InvalidAction,InvalidValue, FormatException {

        String count=null;
        Condition secondaryCondition=null;
        EntityInstance secondaryEntityInstance=null;
        if(secondaryEntity!=null)
        {
            secondaryEntityInstance=secondaryEntity.getEntities().get(0);
            if(!isSubAction)
            {
                count=prdAction.getPRDSecondaryEntity().getPRDSelection().getCount();
                try {
                    int countAsInt =Integer.parseInt(count);
                    if(countAsInt<0)
                    {
                        throw new InvalidValue("On the secondary entity: " + secondaryEntityInstance.getNameOfEntity() +" the count is number but not positive one ("+countAsInt+")");
                    }
                } catch (NumberFormatException e) {
                    if (!count.equals("ALL")) {
                        throw new InvalidValue("On the secondary entity: " + secondaryEntityInstance.getNameOfEntity() + " the count isn't a positive number and not the word 'ALL' (" + count + ")");
                    }
                }
                secondaryCondition = createCondition(secondaryEntityInstance,  prdAction.getPRDSecondaryEntity().getPRDSelection().getPRDCondition(), environment, secondaryEntity,false,secondaryEntity,null,null,0,null);
            }

            secondaryEntityInstance=secondaryEntity.getEntities().get(0);
        }
        if (currInstance.getEntityProperties().containsKey(prdAction.getProperty())) {
            if (prdAction.getType().equals(ActionType.INCREASE.getAction())) {
                return new Increase(prdAction.getEntity(), currInstance.getEntityProperties().get(prdAction.getProperty()), ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(), prdAction.getBy(), currInstance,secondaryEntityInstance,0),prdAction.getBy(),secondaryEntity,count,secondaryCondition);
            }
            else if (prdAction.getType().equals(ActionType.DECREASE.getAction())) {
                return new Decrease(prdAction.getEntity(), currInstance.getEntityProperties().get(prdAction.getProperty()), ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(), prdAction.getBy(), currInstance,secondaryEntityInstance,0),prdAction.getBy(),secondaryEntity,count,secondaryCondition);
            }
            else if (prdAction.getType().equals(ActionType.SET.getAction())) {
                return new Set(prdAction.getEntity(), currInstance.getEntityProperties().get(prdAction.getProperty()), ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(), prdAction.getValue(), currInstance,secondaryEntityInstance,0),prdAction.getValue(),secondaryEntity,count,secondaryCondition);
            }

            else {
                throw new FormatException("One of the actions on the entity: " + currInstance.getNameOfEntity() + "is with invalid type of action(" + prdAction.getType() + ")!\nThe valid types are: increase, decrease, set, condition(single or multiple), calculation(multiply or divide) and kill.");
            }
        }
        else if (prdAction.getType().equals(ActionType.CONDITION.getAction())) {
            return createCondition(currInstance,prdAction.getPRDCondition(), environment, currEntityDefinition,false,secondaryEntity,secondaryEntityInstance,count,0,secondaryCondition);
        }
        else if (prdAction.getType().equals(ActionType.KILL.getAction())) {
            return new Kill(currInstance.getNameOfEntity(), currEntityDefinition,secondaryEntity,count,secondaryCondition);
        }
        else if (prdAction.getType().equals(ActionType.CALCULATION.getAction())) {
            if (prdAction.getPRDDivide() != null) {
                return new Divide(environment.getEnvironmentProperties(), currInstance.getEntityProperties().get(prdAction.getResultProp()), prdAction.getPRDDivide().getArg1(), prdAction.getPRDDivide().getArg2(), currInstance,secondaryEntity,secondaryEntityInstance,count,secondaryCondition,0);
            }
            else if (prdAction.getPRDMultiply() != null) {
                return new Multiply(environment.getEnvironmentProperties(), currInstance.getEntityProperties().get(prdAction.getResultProp()), prdAction.getPRDMultiply().getArg1(), prdAction.getPRDMultiply().getArg2(), currInstance,secondaryEntity,secondaryEntityInstance,count,secondaryCondition,0);
            }
            else {
                throw new InvalidAction("The calculation action doesn't contain divide or multiply actions!");
            }
        }
        else if(prdAction.getType().equals(ActionType.REPLACE.getAction())){
            return new Replace(prdAction.getKill(),secondaryEntity,count,secondaryCondition, prdAction.getCreate(), prdAction.getMode(), entityDefinitionMap,gridArray);
        }
        else if(prdAction.getType().equals(ActionType.PROXIMITY.getAction())){
            String stringDepth=prdAction.getPRDEnvDepth().getOf();
            Object depthExpression =ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(), stringDepth, currInstance,secondaryEntityInstance,0);
            if(depthExpression instanceof Integer ||depthExpression instanceof Float )
            {
                int depth = (int) ((Number) depthExpression).doubleValue();
                return new Proxmity(prdAction.getPRDBetween().getSourceEntity(),secondaryEntity,count,secondaryCondition,gridArray,depth, prdAction.getPRDBetween().getTargetEntity(),stringDepth);
            }
            else {
                throw new FormatException("In the proximity action on the source entity: "+prdAction.getPRDBetween().getSourceEntity()+" and on the target entity: "+ prdAction.getPRDBetween().getTargetEntity()+"\nthe depth ("+stringDepth+") isn't a numeric value!");

            }
        }
        else {
            throw new ValueNotExists("The property: " + prdAction.getProperty() + " isn't exists in the entity: " + prdAction.getEntity() + "'s properties!");
        }
    }
        public Condition createCondition(EntityInstance currInstance, PRDCondition prdCondition, Environment environment, EntityDefinition currEntityDefinition, boolean isRecurse,EntityDefinition secondaryEntityDef,EntityInstance secondEntityInstance,String count,int currTick,Condition secondaryCondition) throws ValueNotExists, InvalidAction, FormatException {
            Object conditionExpression,propertyExpression;
            if (prdCondition.getSingularity().equals(ConditionType.SINGLE.getType())) {
                conditionExpression=ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(), prdCondition.getValue(), currInstance,secondEntityInstance,currTick);
                propertyExpression=ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(), prdCondition.getProperty(), currInstance,secondEntityInstance,currTick);
                return new SingleCondition(ConditionType.SINGLE, currInstance.getNameOfEntity(),propertyExpression, ComparatorType.getCompareSymbol(prdCondition.getOperator()), conditionExpression,prdCondition.getValue(),secondaryEntityDef,count,secondaryCondition,prdCondition.getProperty());
            }
            else if (prdCondition.getSingularity().equals(ConditionType.MULTIPLE.getType())) {
                MultipleCondition newAction = new MultipleCondition(ConditionType.MULTIPLE, currInstance.getNameOfEntity(), LogicalType.getCompareLogic(prdCondition.getLogical()),secondaryEntityDef,count,secondaryCondition);
                List<PRDCondition> prdConditionList = prdCondition.getPRDCondition();
                for (PRDCondition currPrdCondition : prdConditionList) {
                    if (currPrdCondition.getSingularity().equals(ConditionType.SINGLE.getType())) {
                        if( currPrdCondition.getEntity().equals(currInstance.getNameOfEntity()))
                        {
                            newAction.addCondition(createCondition(currInstance, currPrdCondition, environment, currEntityDefinition,true,secondaryEntityDef,secondEntityInstance,null,0,null));
                        }
                        else if( currPrdCondition.getEntity().equals(secondEntityInstance.getNameOfEntity()))
                        {
                            newAction.addCondition(createCondition(secondEntityInstance, currPrdCondition, environment, secondaryEntityDef,true,currEntityDefinition,currInstance,null,0,null));
                        }
                        else {
                            throw new InvalidAction("In the sub condition: "+currPrdCondition.getSingularity()+" you tried to work on the entity: "+currPrdCondition.getEntity()+" but is not allowed\nbecause in the 'father' multiple condition the valid entities are: "+currInstance.getNameOfEntity()+" and "+secondEntityInstance.getNameOfEntity()+"!");
                        }
                    }
                  else if(currPrdCondition.getSingularity().equals(ConditionType.MULTIPLE.getType())){
                        newAction.addCondition(createCondition(currInstance, currPrdCondition, environment, currEntityDefinition,true,secondaryEntityDef,secondEntityInstance,null,0,null));
                    }
                    else {
                        throw new FormatException("One of the conditions in the multiple condition's list, on the entity: " + currPrdCondition.getEntity() + "is with invalid type of singularity(" + currPrdCondition.getSingularity() + ")!\nThe valid types are: single, multiple.");
                    }
                }
                return newAction;
            }
            else {
                throw new FormatException("One of the conditions on the entity: " + currInstance.getNameOfEntity() + " is with invalid type of singularity(" +prdAction.getPRDCondition().getSingularity() + ")!\nThe valid types are: single, multiple.");
            }
        }


}

