package world.property.type;

import world.property.Property;
import world.property.random.RandomImp;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class BooleanType extends Property implements  Serializable {
    private boolean value;
    private boolean initValue;
    private boolean isRandomInitialize;

    public BooleanType(String propertyName, boolean value) {
        super(propertyName);
        this.value = value;
        this.initValue=value;
        this.isRandomInitialize = NOT_RANDOM;
    }
    public BooleanType(String propertyName) {
        super(propertyName);
        RandomImp randomValue= new RandomImp();
        this.value= randomValue.getRandomBooleanValue();
        this.initValue=value;
        this.isRandomInitialize = RANDOM;
    }


    public boolean getInitValue() {
        return initValue;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value,int currTick) {
        this.value = (boolean)value;
        super.increaseChangeCounter(currTick);
        super.setLastValueChangedTick(currTick);

    }

    public boolean isRandomInitialize() {
        return isRandomInitialize;
    }

    @Override
    public String toString() {
        String baseString = super.toString()+ " Property type: boolean";
        if (isRandomInitialize)
        {
            return baseString + ", the value is randomly initialized";
        }
        else {
            return baseString + ", the value is not randomly initialized";
        }
    }
    public String getValidValuesData() {
        return(super.toString()+ "\n Property type: boolean\nValid values are: true or false!") ;
    }
    public String getPropertyData() {
        return (super.toString()+"\nProperty's type: boolean\nProperty's value: "+this.value+"\n");
    }
    public void setRandomValue(){
        if(!super.isUserEnterValue())
        {
            RandomImp randomValue= new RandomImp();
            this.value= randomValue.getRandomBooleanValue();

        }    }


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
        res.put("Type: ","Boolean\n");

        res.put("Random initialize?\n", isRandom +"\n");
        if(!this.isRandomInitialize) {
            res.put("Value: ",getValue().toString()+"\n");
        }
        return res;
    }

    @Override
    public Map<String, String> getEnvProperyMap() {
        Map<String,String> res= new LinkedHashMap<>();
        res.put("Type: ","Boolean\n");
        res.put("Value: ",getValue().toString()+"\n");
        return res;
    }
}
