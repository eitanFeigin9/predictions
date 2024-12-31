package world.property.type;

import world.property.Property;
import world.property.random.RandomImp;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class StringType extends Property   implements Cloneable, Serializable {
    private String value;
    private String initValue;
    private boolean isRandomInitialize;

    public StringType(String propertyName, String value) {
        super(propertyName);
        this.value = value;
        this.initValue=value;
        this.isRandomInitialize = NOT_RANDOM;
    }
    public StringType(String propertyName) {
        super(propertyName);
        RandomImp randomValue= new RandomImp();
        this.value= randomValue.getRandomStringValue();
        this.initValue=value;
        this.isRandomInitialize = RANDOM;
    }


    public void setRandomValue(){
        if(!super.isUserEnterValue())
        {
            RandomImp randomValue= new RandomImp();
            this.value= randomValue.getRandomStringValue();

        }
    }

    public String getInitValue() {
        return initValue;
    }

    public boolean isRandomInitialize() {
        return isRandomInitialize;
    }
    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value,int currTick) {
        this.value = (String)value;
        super.increaseChangeCounter(currTick);
        super.setLastValueChangedTick(currTick);
    }

    @Override
    public String toString() {
        String baseString = super.toString() + ", property type= string ,";
        if (isRandomInitialize)
        {
            return baseString +" the value is randomly initialized";
        }
        else {
            return baseString + " the value is not randomly initialized";
        }
    }
    public String getValidValuesData() {
        return(super.toString()+ "\nProperty type: string\nValid values are strings");
    }
    public String getPropertyData() {
        return (super.toString()+"\nProperty's type: string\nProperty's value: "+this.value+"\n");
    }

    @Override
    public Map<String, String> getProperyMap() {
        String isRandom;
        if(this.isRandomInitialize)
        {
            isRandom="Yes";
        }
        else{
            isRandom="No";
        }
        Map<String,String> res= new LinkedHashMap<>();
        res.put("Type: ","String\n");
        res.put("Random initialize?\n", isRandom +"\n");
        if(!this.isRandomInitialize) {
            res.put("Value: ",getValue().toString()+"\n");
        }
        return res;
    }

    @Override
    public Map<String, String> getEnvProperyMap() {
        Map<String,String> res= new LinkedHashMap<>();
        res.put("Type: ","String\n");
        res.put("Value: ",getValue().toString()+"\n");
        return res;
    }

    @Override
    public StringType clone() {
        try {
            StringType clone = (StringType) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
