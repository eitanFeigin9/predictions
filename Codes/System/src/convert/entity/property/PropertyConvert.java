package convert.entity.property;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.RangeException;
import schema.ex2.PRDProperty;
import world.property.type.range.sub.FloatRange;
import world.property.type.range.sub.IntRange;
import world.property.Property;
import world.property.type.BooleanType;
import world.property.type.DecimalType;
import world.property.type.FloatType;
import world.property.type.StringType;
import world.rule.action.expression.ManagementExpression;

import static world.property.type.EntityPropertyType.*;
import static world.rule.action.expression.ManagementExpression.isFloat;
import static world.rule.action.expression.ManagementExpression.isInteger;

public class PropertyConvert {
    private PRDProperty newPrdProperty;

    public PropertyConvert(PRDProperty newProperty) {
        this.newPrdProperty = newProperty;
    }
    public Property convertPrdPropertyToProperty() throws FormatException, RangeException, InvalidAction, InvalidValue {
        Object initValue=null;
        if(newPrdProperty.getPRDName().contains(" "))
        {
            throw new InvalidValue("The entity property name must be without spaces!\nTherefore the entity property name: "+newPrdProperty.getPRDName()+" isn't valid!");
        }
        final String PROPERTY_ERROR="In the property: " +newPrdProperty.getPRDName()+" An error occurred! ";
        if (!newPrdProperty.getPRDValue().isRandomInitialize()) {
            initValue = ManagementExpression.convertExpressionToFreeValue(newPrdProperty.getPRDValue().getInit());
        }
        if (BOOLEAN.getTypeName().equals(newPrdProperty.getType())) {
            if (newPrdProperty.getPRDValue().isRandomInitialize()) {
                return new BooleanType(newPrdProperty.getPRDName());
            }
            else {
                assert initValue != null;
                if (initValue.getClass().getSimpleName().toLowerCase().equals(BOOLEAN.getTypeName())) {
                    return new BooleanType(newPrdProperty.getPRDName(), (boolean) initValue);
                }
                else {
                    throw new FormatException(String.format(PROPERTY_ERROR+"Invalid init value! The expected init value type is boolean but the init value is from the type: %s", initValue.getClass().getSimpleName()));
                }
            }

        } else if (STRING.getTypeName().toLowerCase().equals(newPrdProperty.getType())) {
            if (newPrdProperty.getPRDValue().isRandomInitialize()) {
                return new StringType(newPrdProperty.getPRDName());
            }
            else {
                assert initValue != null;
                if (initValue.getClass().getSimpleName().equals(STRING.getTypeName())) {
                    return new StringType(newPrdProperty.getPRDName(), (String) initValue);
                } else {
                    throw new FormatException(String.format(PROPERTY_ERROR+"Invalid init value! the expected init value type is String but the init value is from the type: %s", initValue.getClass().getSimpleName()));
                }
            }
        }
        else if (DECIMAL.getTypeName().equals(newPrdProperty.getType())) {
            double from;
            double to;
            if(newPrdProperty.getPRDRange()==null)
            {
                to =Integer.MAX_VALUE;
                from=Integer.MIN_VALUE;
            }
            else {
                from = newPrdProperty.getPRDRange().getFrom();
                to = newPrdProperty.getPRDRange().getTo();
            }
            if (from==Math.floor(from) && to==Math.floor(to)) {
                if (from <= to) {
                    IntRange range = new IntRange((int) from, (int) to);
                    if (newPrdProperty.getPRDValue().isRandomInitialize()) {
                        return new DecimalType(newPrdProperty.getPRDName(), range);
                    } else if (isInteger(newPrdProperty.getPRDValue().getInit())) {
                        int decimalValue = Integer.parseInt(newPrdProperty.getPRDValue().getInit());
                        if (decimalValue >= (int) from && decimalValue <= (int) to) {
                            return new DecimalType(newPrdProperty.getPRDName(), range, decimalValue);
                        } else {
                            throw new RangeException(PROPERTY_ERROR+"The number " + decimalValue + "isn't in the range of " + from + " to " + to + "!");
                        }
                    } else {
                        throw new FormatException(PROPERTY_ERROR+"Invalid init value! the expected init value type is int but the init value is from the type: " + newPrdProperty.getPRDValue().getInit().getClass().getSimpleName());
                    }
                } else {
                    throw new RangeException(PROPERTY_ERROR+"The from value (" + from + ") is bigger than the to value (" + to + ")!");
                }
            } else {
                throw new FormatException(PROPERTY_ERROR+"The range is not from int type!");
            }
        }
        else if (FLOAT.getTypeName().equals(newPrdProperty.getType())) {
            double from;
            double to;
            if(newPrdProperty.getPRDRange()==null)
            {
                to =Float.MAX_VALUE;
                from=Float.MIN_VALUE;
            }
            else {
                from = newPrdProperty.getPRDRange().getFrom();
                to = newPrdProperty.getPRDRange().getTo();
            }
                if (from <= to) {
                    FloatRange range = new FloatRange((float) from, (float) to);
                    if (newPrdProperty.getPRDValue().isRandomInitialize()) {
                        return new FloatType(newPrdProperty.getPRDName(), range);
                    } else if (isFloat(newPrdProperty.getPRDValue().getInit())) {
                        float floatValue = Float.parseFloat(newPrdProperty.getPRDValue().getInit());
                        if (floatValue >= (float) from && floatValue <= (float) to) {
                            return new FloatType(newPrdProperty.getPRDName(), range, floatValue);
                        } else {
                            throw new RangeException(PROPERTY_ERROR+"The number " + floatValue + "isn't in the range of " + from + " to " + to + "!");
                        }
                    } else {
                        throw new FormatException(PROPERTY_ERROR+"Invalid init value! the expected init value type is float but the init value is from the type: " + newPrdProperty.getPRDValue().getInit().getClass().getSimpleName());
                    }
                } else {
                    throw new RangeException(PROPERTY_ERROR+"The from value (" + from + ") is bigger than the to value (" + to + ")!");
                }
            }

        else {
            throw new FormatException(PROPERTY_ERROR+"The type: " +newPrdProperty.getType()+ " is invalid type, the valid types are: boolean, float, string and integer!");
        }
    }
    }
