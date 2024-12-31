package convert.environment.property;

import exception.FormatException;
import exception.InvalidValue;
import exception.RangeException;
import schema.ex2.PRDEnvProperty;
import world.property.type.range.sub.FloatRange;
import world.property.type.range.sub.IntRange;
import world.property.Property;
import world.property.type.BooleanType;
import world.property.type.DecimalType;
import world.property.type.FloatType;
import world.property.type.StringType;

import static world.property.type.EntityPropertyType.*;

public class EnvPropertyConvert {
    private PRDEnvProperty newPrdProperty;

    public EnvPropertyConvert(PRDEnvProperty newProperty) {
        this.newPrdProperty = newProperty;
    }


    public Property convertPrdEnvPropertyToProperty() throws FormatException, RangeException, InvalidValue {
        final String PROPERTY_ERROR="In the property: " +newPrdProperty.getPRDName()+" An error occurred! ";
        if(newPrdProperty.getPRDName().contains(" "))
        {
            throw new InvalidValue("The enviroment property name must be without spaces!\nTherefore the enviorment name: "+newPrdProperty.getPRDName()+" isn't valid!");
        }
        if (BOOLEAN.getTypeName().equals(newPrdProperty.getType())) {
                return new BooleanType(newPrdProperty.getPRDName());
        }
        else if (STRING.getTypeName().toLowerCase().equals(newPrdProperty.getType())) {
                return new StringType(newPrdProperty.getPRDName());
            }
        else if (DECIMAL.getTypeName().equals(newPrdProperty.getType())) {
            double from = newPrdProperty.getPRDRange().getFrom();
            double to = newPrdProperty.getPRDRange().getTo();
            if (from==Math.floor(from) && to==Math.floor(to)) {
                if (from <= to) {
                    IntRange range = new IntRange((int) from, (int) to);
                        return new DecimalType(newPrdProperty.getPRDName(), range);
                    }
                 else {
                    throw new RangeException(PROPERTY_ERROR+"The from value (" + from + ") is bigger than the to value (" + to + ")!");
                }
        }
            else {
                throw new FormatException(PROPERTY_ERROR+"The range is not from int type!");}
        }
        else if (FLOAT.getTypeName().equals(newPrdProperty.getType())) {
            double to,from;
            if(newPrdProperty.getPRDRange()==null)
            {
                to =Integer.MAX_VALUE;
                from=Integer.MIN_VALUE;
            }
            else {
                from = newPrdProperty.getPRDRange().getFrom();
                to = newPrdProperty.getPRDRange().getTo();
            }
            if (from <= to) {
                FloatRange range = new FloatRange((float) from, (float) to);
                    return new FloatType(newPrdProperty.getPRDName(), range);
            } else {
                throw new RangeException(PROPERTY_ERROR+"The from value (" + from + ") is bigger than the to value (" + to + ")!");
            }
        }

        else {
            throw new FormatException(PROPERTY_ERROR+"The type: " +newPrdProperty.getType()+ " is invalid type, the valid types are: boolean, float, string and integer!");
        }
    }
}
