package world.rule.action.type.complex.condition.singlurty.multiple;

import exception.ValueNotExists;
import world.rule.action.type.complex.condition.singlurty.single.ComparatorType;

public enum LogicalType {
    AND("and"),
    OR("or");

    private final String logic;

    private LogicalType(String logic) {
        this.logic = logic;
    }

    public String getLogic() {
        return logic;
    }
    public static LogicalType getCompareLogic(String logic) throws ValueNotExists {
        if(AND.getLogic().equals(logic)){
            return AND;
        } else if (OR.getLogic().equals(logic)) {
            return OR;
        }
        else
        {
            throw new ValueNotExists("The logic type of: "+logic+" isn't one of the valid comparators!\nThe valid logic conditions are:and,or.");
        }
    }
}
