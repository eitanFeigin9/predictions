package convert.rule;


import exception.*;
import schema.ex2.PRDRule;
import schema.ex2.PRDRules;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.management.Rule;
import world.rule.management.Rules;

import java.util.Map;

public class RulesConvert {
    PRDRules prdRules;

    public RulesConvert(PRDRules prdRules) {
        this.prdRules = prdRules;
    }

    public PRDRules getPrdRules() {
        return prdRules;
    }
    public Rules convertPrdRulesToRules(Environment environment, Map<String, EntityDefinition> entities, EntityInstance[][] gridArray) throws InvalidAction, InvalidValue, ValueNotExists, FormatException, ValueExists {
      Rules newRules = new Rules();
        RuleConvert ruleConvert;
       for (PRDRule currRule:prdRules.getPRDRule()) {
            ruleConvert = new RuleConvert(currRule);

           if(!(newRules.getRuleMap().containsKey(currRule.getName()))){
               newRules.addRuleToRulesMap(ruleConvert.convertPrdRuleToRule(environment,entities,gridArray));
           }
           else {
               throw new ValueExists("You tried to add two rules with the same name: "+currRule.getName()+"!");

           }

       }
       return newRules;
    }


}
