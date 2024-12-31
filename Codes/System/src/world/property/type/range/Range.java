package world.property.type.range;

import java.io.Serializable;

public abstract class Range implements Serializable {
    protected Object from;
    protected Object to;

    public Range(Object from, Object to){
        this.from = from;
        this.to = to;
    }

}
