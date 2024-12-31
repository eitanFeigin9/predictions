package world.property.type;

import world.property.type.range.sub.IntRange;
import world.property.Property;
import world.property.random.RandomImp;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


public class DecimalType extends Property  implements  Serializable {
    private IntRange range;
    private int value;
    private int initValue;
    private boolean isRandomInitialize;


    public DecimalType(String propertyName, IntRange range, int value) {
        super(propertyName);
        this.range = range;
        this.value = value;
        this.initValue=value;
        this.isRandomInitialize = NOT_RANDOM;
    }
    public DecimalType(String propertyName, IntRange range) {
        super(propertyName);
        this.range = range;
        RandomImp randomValue= new RandomImp();
        this.value= randomValue.getRandomDecimalValue(this.range);
        this.initValue=value;
        this.isRandomInitialize = RANDOM;
    }

    public boolean isRandomInitialize() {
        return isRandomInitialize;
    }

    public int getInitValue() {
        return initValue;
    }

    public IntRange getRange() {
        return this.range;
    }
    public void setRandomValue(){
        if(!super.isUserEnterValue())
        {
            RandomImp randomValue= new RandomImp();
            this.value= randomValue.getRandomDecimalValue(this.range);

        }    }


    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value,int currTick) {
        super.increaseChangeCounter(currTick);
        super.setLastValueChangedTick(currTick);
        if((int)value>this.range.getTo()){
            this.value=this.range.getTo();
        }
        else if ((int)value<this.range.getFrom()) {
            this.value=this.range.getFrom();
        }
        else
            this.value = (int)value;
    }

    @Override
    public String toString() {
        String baseString = super.toString()+ " Property type: decimal, Property range is from: " + this.range.getFrom() + " to: " + this.range.getTo();
        if (isRandomInitialize)
        {
            return baseString + ", the value is randomly initialized";
        }
        else {
            return baseString + ", the value is not randomly initialized";
        }
    }
    public String getValidValuesData() {
       return(super.toString()+ "\nProperty type: decimal\nProperty range: From: " + this.range.getFrom() + "\nTo: " + this.range.getTo()+"\nValid values are decimals numbers in the range") ;
    }
    public String getPropertyData() {
        return (super.toString()+"\nProperty's type: int\nProperty's value: "+this.value+"\n");
    }

    @Override
    public Map<String,String> getProperyMap() {
        String isRandom;
        if(this.isRandomInitialize)
        {
            isRandom="Yes";
        }
        else{
            isRandom="No";
        }
        Map<String,String> res= new LinkedHashMap<>();
        res.put("Type: ","Number\n");
        res.put("Range:\nFrom: ", range.getFrom() +"\n");
        res.put("To: ", range.getTo() +"\n");
        res.put("Random initialize?\n", isRandom +"\n");
        if(!this.isRandomInitialize) {
            res.put("Value: ",getValue().toString()+"\n");
        }
        return res;
    }

    @Override
    public Map<String, String> getEnvProperyMap() {
        Map<String,String> res= new LinkedHashMap<>();
        res.put("Type: ","Number\n");
        res.put("Range:\nFrom: ", range.getFrom() +"\n");
        res.put("To: ", range.getTo() +"\n");
        res.put("Value: ",getValue().toString()+"\n");
        return res;
    }
}

