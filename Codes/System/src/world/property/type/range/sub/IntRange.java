package world.property.type.range.sub;


import world.property.type.range.Range;

import java.io.Serializable;

public class IntRange extends Range implements Serializable {
    public IntRange(int from, int to){
        super(from,to);
    }


    public int getFrom() {
        return (int)super.from ;
    }


    public int getTo() {
        return (int)super.to ;
    }

}
