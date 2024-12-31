package convert.rule;

import convert.action.ActionsConvert;
import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import schema.ex2.PRDRule;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.action.Action;
import world.rule.management.Rule;

import java.util.List;
import java.util.Map;

public class RuleConvert {
    PRDRule prdRule;

    public RuleConvert(PRDRule prdRule) {
        this.prdRule = prdRule;
    }
    public Rule convertPrdRuleToRule(Environment environment, Map<String, EntityDefinition> entities, EntityInstance[][] gridArray) throws InvalidValue, InvalidAction, ValueNotExists, FormatException {
        Rule newRule;

        if (prdRule.getPRDActivation() == null) {
            newRule=new Rule(prdRule.getName(),new Rule.Activation());
        }
       else {

            if (prdRule.getPRDActivation().getTicks() != null) {
                Integer ticks = prdRule.getPRDActivation().getTicks();
                if (ticks >= 0) {
                    if (prdRule.getPRDActivation().getProbability() != null) {
                        Double probability = prdRule.getPRDActivation().getProbability();
                        if (probability >= 0.0 && probability <= 1.0) {
                            newRule = new Rule(prdRule.getName(), new Rule.Activation(ticks, probability));
                        }
                        else {
                            throw new InvalidValue("In the rule: " + prdRule.getName() + " the probability isn't between 0 to 1!");
                        }
                    } else {
                        newRule = new Rule(prdRule.getName(), new Rule.Activation(ticks));
                    }
                } else {
                    throw new InvalidValue("In the rule: " + prdRule.getName() + " the ticks is a negative number and should be no-negative!");
                }
            } else if (prdRule.getPRDActivation().getProbability() != null) {
                Double probability = prdRule.getPRDActivation().getProbability();
                if (probability >= 0.0 && probability <= 1.0) {
                    newRule = new Rule(prdRule.getName(), new Rule.Activation(probability));
                } else {
                    throw new InvalidValue("In the rule: " + prdRule.getName() + " the probability isn't between 0 to 1!");
                }
            } else {
                newRule = new Rule(prdRule.getName(), new Rule.Activation());
            }
        }
        ActionsConvert convert = new ActionsConvert(this.prdRule.getPRDActions().getPRDAction());

        List<Action> ruleActions=convert.convertPrdActionsToActionsList(environment,entities,gridArray,null,null,false);
        newRule.getActionList().addAll(ruleActions);
            return newRule;


    }

    }

