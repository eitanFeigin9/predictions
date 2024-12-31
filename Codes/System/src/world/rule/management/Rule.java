package world.rule.management;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.action.Action;
import world.rule.action.type.multi.entities.Replace;
import world.rule.action.type.simple.Kill;

import java.io.Serializable;
import java.util.*;

public class Rule implements  Serializable {
    private String name;
    Activation activation;
    private List<Action> actionList;
    private Set<Action> endTickActionSet;

    public Rule(String name,Activation activation) {
        this.name = name;
        this.activation= activation;
        this.actionList = new ArrayList<>();
        this.endTickActionSet =new LinkedHashSet<>();
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public String getName() {
        return name;
    }
    public void activeRule(int simulationTicks, Map<String, EntityDefinition> entities, Environment environment,EntityInstance currInstance) throws InvalidAction, ValueNotExists, FormatException, InvalidValue {
        Set<Action>killReplaceList=new LinkedHashSet<>();
                for (Action currAction : actionList) {
                    if(currAction instanceof Replace || currAction instanceof Kill)
                    {
                        killReplaceList.add(currAction);
                    }
                    else {


                        String mainEntityName = currAction.getMainEntity();
                        if (currInstance.getNameOfEntity().equals(mainEntityName)) {

                            if (currInstance.isEntityAlive())
                                currAction.calc(currInstance, environment, simulationTicks);
                        }
                    }


        }
                this.endTickActionSet =killReplaceList;
    }
    public void activeEndActions(int simulationTicks, Map<String, EntityDefinition> entities, Environment environment,EntityInstance currInstance) throws InvalidAction, ValueNotExists, FormatException, InvalidValue {
        for (Action currAction : endTickActionSet) {
                String mainEntityName = currAction.getMainEntity();
                if (currInstance.getNameOfEntity().equals(mainEntityName)) {
                    if (currInstance.isEntityAlive())
                        currAction.calc(currInstance, environment, simulationTicks);
                }

                }


    }
    public List<String> getActivationData(){
        List<String>res=new ArrayList<>();
        String ticks=("Num of ticks : "+this.activation.ticks);
        String probability=("probability : "+this.activation.probability);
        res.add(ticks);
        res.add(probability);
        return res;
    }
public boolean isRuleActivate(int simulationTicks) {
    if (simulationTicks % this.activation.getTicks() == 0) {
        Random random = new Random();
        float randomFloat = random.nextFloat() * 1.0f;
        if (this.activation.getProbability() > randomFloat) {
            return true;
        }
    }
    return false;
}
    public void addActionToList(Action newAction) {
        actionList.add(newAction);
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Rule name = " + this.name + " , Activation: [ probability = " + activation.probability + " , ticks = " + activation.ticks + " ] ,Number of actions = " + actionList.size() + " \n  Rule actions details: \n");
        for (Action action : actionList)
        {
            result.append(action.toString()).append("\n");
        }
        return result.toString();
    }
    public static class Activation implements  Serializable {
        private final int DEFAULT_TICKS_VALUE=1;
        private final double DEFAULT_PROBABILITY_VALUE=1.0;
        private int ticks = DEFAULT_TICKS_VALUE;
        private double probability=DEFAULT_PROBABILITY_VALUE;

        public Activation() {
        }
        public Activation(int ticks, double probability) {
            this.ticks = ticks;
            this.probability = probability;
        }
        public Activation(int ticks) {
            this.ticks = ticks;
        }
        public Activation(double probability) {
            this.probability = probability;
        }

        public int getTicks() {
            return ticks;
        }

        public double getProbability() {
            return probability;
        }


    }

}
