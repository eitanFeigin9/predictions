package convert.action;


import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import schema.ex2.PRDAction;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.action.Action;
import world.rule.action.type.complex.condition.Condition;
import world.rule.action.type.multi.entities.Proxmity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionsConvert {
   private List<PRDAction>  prdActions;
   public ActionsConvert(List<PRDAction> prdActions) {
       this.prdActions = prdActions;
   }

    public  List<Action> convertPrdActionsToActionsList(Environment environment, Map<String, EntityDefinition> entities,EntityInstance[][] gridArray,EntityDefinition mainEntityDef,EntityDefinition SecondaryEntityDef,boolean isSubAction) throws ValueNotExists, InvalidAction, FormatException, InvalidValue {
        List<Action> resList = new ArrayList<>();
        EntityDefinition secondaryEntity=null;
        EntityDefinition mainEntity=null;
        for (PRDAction currAction : prdActions) {
            if(SecondaryEntityDef!=null)
            {
                secondaryEntity=SecondaryEntityDef;
                mainEntity=mainEntityDef;
            }
            else if (currAction.getPRDSecondaryEntity()!=null) {
                if (entities.containsKey(currAction.getPRDSecondaryEntity().getEntity())) {
                    secondaryEntity=entities.get(currAction.getPRDSecondaryEntity().getEntity());
                }
                else {
                    throw new ValueNotExists("There is an action from type of: " + currAction.getType() + " that contains second entity(" + currAction.getPRDSecondaryEntity().getEntity() + ") that doesn't exists!");

                }
            }
                if (entities.containsKey(currAction.getEntity())) {
                    ActionConvert currConvert = new ActionConvert(currAction);
                    Action newAction;
                    if(mainEntityDef!=null)
                    {
                        if(mainEntityDef.getEntityName().equals(currAction.getEntity())){
                             newAction = currConvert.convertPrdActionToAction(environment, entities.get(currAction.getEntity()), entities.get(currAction.getEntity()).getEntities().get(0), secondaryEntity, gridArray, entities,isSubAction);
                        } else {
                            assert secondaryEntity != null;
                            if (secondaryEntity.getEntityName().equals(currAction.getEntity())) {
                                 newAction = currConvert.convertPrdActionToAction(environment, entities.get(currAction.getEntity()), entities.get(currAction.getEntity()).getEntities().get(0), mainEntityDef, gridArray, entities,isSubAction);
                            }
                            else {
                                throw new InvalidAction("In the sub action: "+currAction.getType()+" you tried to work on the entity: "+currAction.getEntity()+" but is not allowed\nbecause in the main action the valid entities are: "+mainEntityDef.getEntityName()+" and "+secondaryEntity.getEntityName()+"!");
                            }
                        }
                    }
                    else {
                        mainEntity=entities.get(currAction.getEntity());
                     newAction = currConvert.convertPrdActionToAction(environment, entities.get(currAction.getEntity()), entities.get(currAction.getEntity()).getEntities().get(0), secondaryEntity, gridArray, entities,isSubAction);
                    }
                    if (newAction.getClass().getSimpleName().equals("MultipleCondition") || newAction.getClass().getSimpleName().equals("SingleCondition")) {
                        ((Condition) newAction).getThanActions().addAll(convertPrdActionListToActionList(currAction.getPRDThen().getPRDAction(), entities, environment, gridArray,secondaryEntity, mainEntity));
                        if (currAction.getPRDElse() != null) {
                            ((Condition) newAction).getElseActions().addAll(convertPrdActionListToActionList(currAction.getPRDElse().getPRDAction(), entities, environment, gridArray,secondaryEntity,mainEntity));
                        }
                    }
                    resList.add(newAction);
                }
                else if (currAction.getType().equals("replace")) {
                    if (entities.containsKey(currAction.getKill()) && entities.containsKey(currAction.getCreate())) {
                        ActionConvert currConvert = new ActionConvert(currAction);
                        Action newAction = currConvert.convertPrdActionToAction(environment, entities.get(currAction.getKill()), entities.get(currAction.getKill()).getEntities().get(0), secondaryEntity, gridArray, entities, isSubAction);
                        resList.add(newAction);

                    } else {
                        if (entities.containsKey(currAction.getKill()))
                        {
                            throw new ValueNotExists("There is a replace action that contains an entity(" + currAction.getCreate() + ") that doesn't exists!");
                        }
                        else {
                            if(!entities.containsKey(currAction.getCreate()))
                            {
                                throw new ValueNotExists("There is a replace action that contains two entities: the kill one(" + currAction.getKill() + ") and the create one ("+currAction.getCreate()+")that doesn't exists!");

                            }
                            else {
                                throw new ValueNotExists("There is a replace action that contains a kill entity(" + currAction.getKill() + ") that doesn't exists!");
                            }
                        }

                    }
                }
                 else if (currAction.getType().equals("proximity")) {
                    if (entities.containsKey(currAction.getPRDBetween().getSourceEntity()) && entities.containsKey(currAction.getPRDBetween().getTargetEntity())) {
                        ActionConvert currConvert = new ActionConvert(currAction);
                        Action newAction = currConvert.convertPrdActionToAction(environment, entities.get(currAction.getPRDBetween().getSourceEntity()), entities.get(currAction.getPRDBetween().getSourceEntity()).getEntities().get(0), secondaryEntity, gridArray, entities,isSubAction);
                         ((Proxmity) newAction).getThanActions().addAll(convertPrdActionListToActionList(currAction.getPRDActions().getPRDAction(), entities, environment, gridArray,entities.get(currAction.getPRDBetween().getTargetEntity()),entities.get(currAction.getPRDBetween().getSourceEntity())));
                        resList.add(newAction);

                    } else {
                        if (entities.containsKey(currAction.getPRDBetween().getSourceEntity())){
                            throw new ValueNotExists("There is a proximity action that contains a target entity(" + currAction.getPRDBetween().getTargetEntity() +") that doesn't exists!");

                        }
                        else {
                            throw new ValueNotExists("There is a proximity action that contains a source entity(" + currAction.getPRDBetween().getSourceEntity() +") that doesn't exists!");
                        }
                    }
                }
                 else{
                    throw new ValueNotExists("There is an action from type of: " + currAction.getType() + " that contains entity(" + currAction.getEntity() + ") that doesn't exists!");
                }
            }
            return resList;
        }
    public List<Action> convertPrdActionListToActionList(List<PRDAction> prdActionsList,Map<String, EntityDefinition> entities,Environment environment,EntityInstance[][] gridArray,EntityDefinition secondaryEntity,EntityDefinition mainEntity) throws ValueNotExists, InvalidAction, FormatException, InvalidValue {
        ActionsConvert listConvert =new ActionsConvert(prdActionsList);
        return listConvert.convertPrdActionsToActionsList(environment,entities,gridArray,mainEntity,secondaryEntity,true);
    }
}
