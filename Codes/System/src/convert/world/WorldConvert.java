package convert.world;

import convert.entity.definition.EntityDefinitionConvert;
import convert.environment.EnvironmentConvert;
import convert.rule.RulesConvert;
import exception.*;
import schema.ex2.*;
import world.World;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.management.Rules;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorldConvert {
    private PRDWorld prdWorld;


    public WorldConvert(PRDWorld prdWorld) {
        this.prdWorld = prdWorld;
    }

    public World convertPrdWorldToWorld()throws RangeException, ValueExists, FormatException, InvalidAction, InvalidValue, ValueNotExists  {
        EnvironmentConvert environmentConvert = new EnvironmentConvert(prdWorld.getPRDEnvironment());
        Environment newEnvironment =  environmentConvert.createNewEnvironment();
        Map<String, EntityDefinition> entities = new LinkedHashMap<>();
        for (PRDEntity currEntityDef : prdWorld.getPRDEntities().getPRDEntity())
        {
            EntityDefinitionConvert entityDefinitionConvert = new EntityDefinitionConvert(currEntityDef);
            EntityDefinition newEntityDefinition = entityDefinitionConvert.createEntityDefinition();
            if(entities.containsKey(newEntityDefinition.getEntityName())){
                throw new ValueExists("You tried to add two entities (or more) with the same name: "+newEntityDefinition.getEntityName()+"!");
            }
            else {
                entities.put(newEntityDefinition.getEntityName(),newEntityDefinition);
            }
        }
        int rows,columns;
        rows=prdWorld.getPRDGrid().getRows();
        columns=prdWorld.getPRDGrid().getColumns();
        if(rows<10||rows>100||columns<10||columns>100){
            throw new InvalidValue("The grid's values not in the correct range!\nThe correct range (of the rows and columns) is between 10 to 100.");
        }
         EntityInstance[][] gridArray=new EntityInstance[rows][columns];
        RulesConvert rulesConvert= new RulesConvert(prdWorld.getPRDRules());
        Rules newRules = rulesConvert.convertPrdRulesToRules(newEnvironment,entities,gridArray);
        List<Object> prdList = prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks();
        int threadPoolSize=prdWorld.getPRDThreadCount();
        if(threadPoolSize<=0) {
            throw new InvalidValue("The thread Pool size ("+threadPoolSize +") is not positive one!\nThe correct size must be positive.");
        }
        boolean seconds=false;
        boolean ticks=false;
        PRDBySecond prdBySecond=null;
        PRDByTicks prdByTicks=null;
        if(!prdList.isEmpty()){
            if(prdList.size()>2)
            {
                throw new InvalidValue("There are too much termination conditions!");

            }
            if(prdWorld.getPRDTermination().getPRDByUser()!=null){
                throw new InvalidValue("There is termination condition by user and by tick/seconds!\nIt possible or by tick and (or) by seconds or just by user");

            }
        for (Object obj : prdList) {
            if (obj instanceof PRDBySecond) {
                 prdBySecond = (PRDBySecond) obj;
                if (prdBySecond.getCount() < 0) {
                    throw new InvalidValue("The seconds count of the determination condition is negative!");
                } else {
                    seconds = true;
                }
            }
            else if (obj instanceof PRDByTicks) {
                 prdByTicks = (PRDByTicks) obj;
                if (prdByTicks.getCount() < 0) {
                    throw new InvalidValue("The ticks count of the determination condition is negative!");
                } else {
                    ticks = true;
                }
            }
            else {throw new FormatException("No valid format of the determination condition!");}
        }
        if(seconds&&ticks)
        {
            return (new World(newRules,newEnvironment,prdByTicks.getCount(),prdBySecond.getCount(),entities,rows,columns,threadPoolSize,gridArray));
        }
        else if(seconds)
        {
            return (new World(newRules,newEnvironment,null,prdBySecond.getCount(),entities,rows,columns,threadPoolSize,gridArray));

        }
        else if(ticks)
        {
            return (new World(newRules,newEnvironment,prdByTicks.getCount(),null,entities,rows,columns,threadPoolSize,gridArray));
        }
        else {
            throw new InvalidAction("You didn't insert any determination conditions!\nYou need to insert at least one of ticks count or seconds count.");

        }
        }
        else {
            Object prdByUser;
            prdByUser= prdWorld.getPRDTermination().getPRDByUser();
            if(prdByUser==null){
                throw new InvalidAction("You didn't insert any determination conditions!\nYou need to insert at least one of ticks count or seconds count or by user.");
            }
            else {
                return (new World(newRules,newEnvironment,null,null,entities,rows,columns,threadPoolSize,gridArray));
            }
        }
    }
}

