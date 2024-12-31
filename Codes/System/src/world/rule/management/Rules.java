package world.rule.management;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Rules implements Serializable {
    Map<String, Rule> ruleMap;

    public Rules() {
        this.ruleMap =  new LinkedHashMap<>();
    }

    public Map<String, Rule> getRuleMap() {
        return ruleMap;
    }
    public void addRuleToRulesMap(Rule newRule){
        ruleMap.put(newRule.getName(),newRule);
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Rule rule : ruleMap.values())
        {
            result.append(rule.toString()).append("\n");
        }
        return result.toString();
    }
}
