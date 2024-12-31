package world.rule.action;

public enum ActionType {
    INCREASE("increase"), DECREASE("decrease"), KILL("kill"),SET("set"),CONDITION("condition"),	CALCULATION("calculation"),REPLACE("replace"),PROXIMITY("proximity");
    private final String action;

    private ActionType(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
