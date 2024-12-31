package world.property;

import world.property.type.PropertyDtoData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Property   implements  Serializable, PropertyDtoData {
    final public boolean RANDOM = true;
    final public boolean NOT_RANDOM = false;
    private String propertyName;
    private int lastValueChangedTick;
    private List<Integer> noChangesCount;
    private Object value;
    private boolean isUserEnterValue;

    protected Property(String propertyName) {
        this.propertyName = propertyName;
        this.isUserEnterValue = false;
        this.lastValueChangedTick=0;
        this.noChangesCount=new ArrayList<>();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isUserEnterValue() {
        return isUserEnterValue;
    }

    public void setUserEnterValue(boolean userEnterValue) {
        this.isUserEnterValue = userEnterValue;
    }

    abstract public void setValue(Object value,int currTick);

    abstract public Object getValue();

    public int getLastValueChangedTick() {
        return lastValueChangedTick;
    }

    public List<Integer> getNoChangesCount() {
        return noChangesCount;
    }

    public void increaseChangeCounter(int currTick) {
        if(lastValueChangedTick!=currTick)
        this.noChangesCount.add(currTick-(lastValueChangedTick+1));
    }

    public void setLastValueChangedTick(int lastValueChangedTick) {
        this.lastValueChangedTick = lastValueChangedTick;
    }


    @Override
    public String toString() {
        return " Property name = " + this.propertyName + " ";
    }

    public String simpleToString() {
        return " Property name = " + this.propertyName;
    }

    abstract public String getValidValuesData();

    abstract public String getPropertyData();

    @Override
    abstract public Map<String, String> getProperyMap();
    @Override
    abstract public Map<String, String> getEnvProperyMap();

    abstract public void setRandomValue();
}
