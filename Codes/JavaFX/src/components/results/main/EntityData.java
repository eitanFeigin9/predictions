package components.results.main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EntityData {
    private final SimpleStringProperty entityName;
    private final SimpleIntegerProperty entityAmount;

    public EntityData(String entityName, int entityAmount) {
        this.entityName = new SimpleStringProperty(entityName);
        this.entityAmount = new SimpleIntegerProperty(entityAmount);
    }

    public String getEntityName() {
        return entityName.get();
    }

    public SimpleStringProperty entityNameProperty() {
        return entityName;
    }

    public int getEntityAmount() {
        return entityAmount.get();
    }
}
