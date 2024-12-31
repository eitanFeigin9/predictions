package world.property.type;

public enum EntityPropertyType {
    DECIMAL("decimal"),
    FLOAT("float"),
    BOOLEAN("boolean"),
    STRING("String");

    private final String typeName;

    EntityPropertyType(String typeName) {
        this.typeName = typeName;
    }
    public String getTypeName() {
        return typeName;
    }
}
