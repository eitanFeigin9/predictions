
package system.file.instruction.show.Manage;
import world.World;
import world.rule.action.Action;
import world.rule.action.type.complex.calculation.impl.Divide;
import world.rule.action.type.complex.calculation.impl.Multiply;
import world.rule.action.type.complex.condition.singlurty.multiple.MultipleCondition;
import world.rule.action.type.complex.condition.singlurty.single.SingleCondition;
import world.rule.action.type.multi.entities.Proxmity;
import world.rule.action.type.multi.entities.Replace;
import world.rule.action.type.simple.Decrease;
import world.rule.action.type.simple.Increase;
import world.rule.action.type.simple.Kill;
import world.rule.action.type.simple.Set;
import world.rule.management.Rule;
import world.rule.management.Rules;

import java.util.*;

public class CreateRulesMap {
    public Map<String, List<String>> getActivationMap(World world) {
        Rules rules = world.getRules();
        Map<String, List<String>> resMap = new LinkedHashMap<>();
        Map<String, Rule> rulesMap = rules.getRuleMap();
        List<String> res;

        for (Map.Entry<String, Rule> currRule : rulesMap.entrySet()) {
            res = new ArrayList<>(currRule.getValue().getActivationData());
            resMap.put(currRule.getKey(), res);
        }
        return resMap;
    }

    public Map<String, Map<String, List<String>>> getActionsMap(World world) {
        Rules rules = world.getRules();
        Map<String, Map<String, List<String>>> resMap = new LinkedHashMap<>();
        Map<String, Rule> rulesMap = rules.getRuleMap();

        for (Map.Entry<String, Rule> currRule : rulesMap.entrySet()) {
            Map<String, List<String>> ruleActions = new LinkedHashMap<>();

            List<Action> actionList = currRule.getValue().getActionList();
            for (Action currAction : actionList) {
                if (currAction instanceof Divide) {
                    addToActionMap(ruleActions, "Divide", ((Divide) currAction).getData());
                } else if (currAction instanceof Multiply) {
                    addToActionMap(ruleActions, "Multiply", ((Multiply) currAction).getData());
                } else if (currAction instanceof MultipleCondition) {
                    addToActionMap(ruleActions, "MultipleCondition", ((MultipleCondition) currAction).getData());
                } else if (currAction instanceof SingleCondition) {
                    addToActionMap(ruleActions, "SingleCondition", ((SingleCondition) currAction).getData());
                } else if (currAction instanceof Proxmity) {
                    addToActionMap(ruleActions, "Proximity", ((Proxmity) currAction).getData());
                } else if (currAction instanceof Replace) {
                    addToActionMap(ruleActions, "Replace", ((Replace) currAction).getData());
                } else if (currAction instanceof Decrease) {
                    addToActionMap(ruleActions, "Decrease", ((Decrease) currAction).getData());
                } else if (currAction instanceof Increase) {
                    addToActionMap(ruleActions, "Increase", ((Increase) currAction).getData());
                } else if (currAction instanceof Kill) {
                    addToActionMap(ruleActions, "Kill", ((Kill) currAction).getData());
                } else if (currAction instanceof Set) {
                    addToActionMap(ruleActions, "Set", ((Set) currAction).getData());
                }
            }

            resMap.put(currRule.getKey(), ruleActions);
        }

        return resMap;
    }

    private void addToActionMap(Map<String, List<String>> actionMap, String actionType, String data) {
        if (!actionMap.containsKey(actionType)) {
            actionMap.put(actionType, new ArrayList<>());
        }
        actionMap.get(actionType).add(data);
    }
}
