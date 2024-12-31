package world.rule.action.type.multi.entities;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.action.Action;
import world.rule.action.expression.ManagementExpression;
import world.rule.action.type.complex.condition.Condition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Proxmity extends Action {
    private int depth;
    private String secondaryEntity;
    private EntityInstance[][] gridArray;
   private List<Action> thanActions;
   private String stringDepth;


    public Proxmity(String mainEntityName, EntityDefinition secondryEntity, String count, Condition seconderyCondition,EntityInstance[][] gridArray,int depth,String secondaryEntity,String stringDepth) {
        super(mainEntityName, secondryEntity, count, seconderyCondition);
        this.depth=depth;
        this.secondaryEntity=secondaryEntity;
       this.gridArray=gridArray;
        this.thanActions = new ArrayList<>();
        this.stringDepth=stringDepth;
    }

    public List<Action> getThanActions() {
        return thanActions;
    }
    @Override
    public void executeAction(EntityInstance mainEntityInstance, Environment environment, int currTicks, EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue {
        Set<EntityInstance> uniqueSet = new HashSet<>();
        Object depthExpression = ManagementExpression.getExpressionValue(environment.getEnvironmentProperties(), stringDepth, mainEntityInstance,secondaryInstance,currTicks);
        if(depthExpression instanceof Integer ||depthExpression instanceof Float ) {
            depth = (int) ((Number) depthExpression).doubleValue();
        }
        else {
            throw new FormatException("In the proximity action on the source entity: "+mainEntityInstance.getNameOfEntity()+" and on the target entity: "+ secondaryInstance.getNameOfEntity()+"\nin the tick : "+currTicks+"\nthe depth ("+stringDepth+") isn't a numeric value!");

        }
        recurse(mainEntityInstance.getRow(),mainEntityInstance.getCol(),depth,uniqueSet);
        for (EntityInstance entity : uniqueSet) {
            for(Action currAction: this.thanActions) {
                currAction.executeAction(mainEntityInstance, environment, currTicks,entity);
            }
        }

    }
    public void recurse( int entityRow,int entityCol, int depth,Set<EntityInstance> uniqueSet){
        if(depth<1){
            return;
        }
        else {
            int maxCol=gridArray[0].length;
            int maxRow=gridArray.length;
            int updateRow,updateCol;
            if(entityRow+1==maxRow) {
                updateRow = 0;
            }
            else {
                updateRow=entityRow+1;
            }
                if(gridArray[updateRow][entityCol]!=null)
                {
                    String entityName=gridArray[updateRow][entityCol].getNameOfEntity();
                    if(entityName.equals(secondaryEntity)) {
                        uniqueSet.add(gridArray[updateRow][entityCol]);
                    }
                }
            recurse(updateRow,entityCol,depth-1,uniqueSet);
                if(entityCol+1==maxCol) {
                    updateCol = 0;
                }
                else {
                    updateCol=entityCol+1;
                }
                    if(gridArray[updateRow][updateCol]!=null)
                    {
                        String entityName=gridArray[updateRow][updateCol].getNameOfEntity();
                        if(entityName.equals(secondaryEntity)) {
                            uniqueSet.add(gridArray[updateRow][updateCol]);
                        }
                    }
            recurse(updateRow,updateCol,depth-1,uniqueSet);

            if(entityCol==0){
                    updateCol=maxCol-1;
                }
                else {
                    updateCol=entityCol-1;
                }
                    if(gridArray[updateRow][updateCol]!=null) {
                        String entityName=gridArray[updateRow][updateCol].getNameOfEntity();
                        if(entityName.equals(secondaryEntity)) {
                            uniqueSet.add(gridArray[updateRow][updateCol]);
                        }
                    }
            recurse(updateRow,updateCol,depth-1,uniqueSet);
            if(entityRow==0) {
                updateRow = maxRow-1;
            }
            else {
                updateRow=entityRow-1;
            }
            if(gridArray[updateRow][entityCol]!=null)
            {
                String entityName=gridArray[updateRow][entityCol].getNameOfEntity();
                if(entityName.equals(secondaryEntity)) {
                    uniqueSet.add(gridArray[updateRow][entityCol]);
                }
            }
            recurse(updateRow,entityCol,depth-1,uniqueSet);
            if(entityCol+1==maxCol) {
                updateCol = 0;
            }
            else {
                updateCol=entityCol+1;
            }
            if(gridArray[updateRow][updateCol]!=null)
            {
                String entityName=gridArray[updateRow][updateCol].getNameOfEntity();
                if(entityName.equals(secondaryEntity)) {
                    uniqueSet.add(gridArray[updateRow][updateCol]);
                }
            }
            recurse(updateRow,updateCol,depth-1,uniqueSet);
            if(entityCol==0){
                updateCol=maxCol-1;
            }
            else {
                updateCol=entityCol-1;
            }
            if(gridArray[updateRow][updateCol]!=null) {

                String entityName=gridArray[updateRow][updateCol].getNameOfEntity();
                if(entityName.equals(secondaryEntity)) {
                    uniqueSet.add(gridArray[updateRow][updateCol]);
                }
            }
            recurse(updateRow,updateCol,depth-1,uniqueSet);
            updateRow=entityRow;
            if(entityCol+1==maxCol) {
                updateCol = 0;
            }
            else {
                updateCol=entityCol+1;
            }
            if(gridArray[updateRow][updateCol]!=null)
            {
                String entityName=gridArray[updateRow][updateCol].getNameOfEntity();
                if(entityName.equals(secondaryEntity)) {
                    uniqueSet.add(gridArray[updateRow][updateCol]);
                }
            }
            recurse(updateRow,updateCol,depth-1,uniqueSet);

            if(entityCol==0){
                updateCol=maxCol-1;
            }
            else {
                updateCol=entityCol-1;
            }
            if(gridArray[updateRow][updateCol]!=null) {
                String entityName=gridArray[updateRow][updateCol].getNameOfEntity();
                if(entityName.equals(secondaryEntity)) {
                    uniqueSet.add(gridArray[updateRow][updateCol]);
                }
            }
            recurse(updateRow,updateCol,depth-1,uniqueSet);
        }

    }
    public String getData() {
        return "Action Type : Proximity\nSource entity : " + super.getMainEntity() +"\nTarget Entity : "+secondaryEntity+"\nDepth : \n"+stringDepth+"\nAmount of actions : "+ this.thanActions.size();
    }
}

