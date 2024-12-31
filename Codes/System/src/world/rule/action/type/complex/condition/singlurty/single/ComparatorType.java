package world.rule.action.type.complex.condition.singlurty.single;

import exception.ValueNotExists;

public enum ComparatorType {
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN("bt"),
    LESS_THAN("lt");

    private final String symbol;

    private ComparatorType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
    public static ComparatorType getCompareSymbol(String symbol) throws ValueNotExists {
        if(EQUALS.getSymbol().equals(symbol)){
            return EQUALS;
        } else if (NOT_EQUALS.getSymbol().equals(symbol)) {
            return NOT_EQUALS;
        }
     else if (GREATER_THAN.getSymbol().equals(symbol)) {
        return GREATER_THAN;
    }
    else if (LESS_THAN.getSymbol().equals(symbol)) {
        return LESS_THAN;
        }
    else
        {
            throw new ValueNotExists("The comparator type of: "+symbol+" isn't one of the valid comparators!\nThe valid comparators are:=,!=,bt,lt.");
        }
    }
}
