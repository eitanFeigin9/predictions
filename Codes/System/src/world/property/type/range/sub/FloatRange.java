package world.property.type.range.sub;

import world.property.type.range.Range;

import java.io.Serializable;

public class FloatRange extends Range implements  Serializable {
    public FloatRange(float from, float to){
        super(from,to);
    }


    public float getFrom() {
        return (float)super.from;
    }


    public float getTo() {
        return (float)super.to;
    }

}
