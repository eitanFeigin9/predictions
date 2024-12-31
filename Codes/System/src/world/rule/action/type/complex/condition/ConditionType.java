package world.rule.action.type.complex.condition;

public enum ConditionType {
    SINGLE("single"),
    MULTIPLE("multiple");

    private final String type;

    private ConditionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}