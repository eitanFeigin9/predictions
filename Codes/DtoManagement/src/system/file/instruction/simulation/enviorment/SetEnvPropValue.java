package system.file.instruction.simulation.enviorment;

import system.file.instruction.valid.dto.IsValidDto;
import world.property.Property;
import world.property.type.DecimalType;
import world.property.type.FloatType;

public class SetEnvPropValue {
    private final String STRING_TYPE="StringType";
    private final String BOOL_TYPE="BooleanType";
    private final String INT_TYPE="DecimalType";
    private final String INSERT_SUCCEED="Value set!";
    private String newValue;
    private Property property;

    public SetEnvPropValue(String newValue, Property property) {
        this.newValue = newValue;
        this.property = property;
    }
    public IsValidDto isSetEnvPropValueSucceed(){
        if(property.getClass().getSimpleName().equals(STRING_TYPE))
        {
            property.setValue(newValue,0);
            property.setUserEnterValue(true);
            return (new IsValidDto(INSERT_SUCCEED,true));
        }
        else if(property.getClass().getSimpleName().equals(BOOL_TYPE))
        {
            if(newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false"))
            {
                if(newValue.equalsIgnoreCase("true")){
                    property.setValue(true,0);
                }
                else {
                    property.setValue(false,0);
                }
                property.setUserEnterValue(true);
                return (new IsValidDto(INSERT_SUCCEED,true));
            }
            return (new IsValidDto("The property is from boolean type but your value ("+newValue+") isn't!",false));
        }
        else if(property.getClass().getSimpleName().equals(INT_TYPE))
        {
            try {
                int intValue = (Integer.parseInt(newValue));
                {
                    if(((DecimalType)property).getRange().getFrom()<=intValue&&((DecimalType)property).getRange().getTo()>=intValue) {
                        property.setValue(intValue,0);
                        property.setUserEnterValue(true);

                        return (new IsValidDto(INSERT_SUCCEED,true));
                    }
                    else {
                        return (new IsValidDto("Your value ("+newValue+") isn't in the correct range!\nThe range is between: "+((DecimalType)property).getRange().getFrom()+" to "+((DecimalType)property).getRange().getTo()+"!",false));
                    }

                }

            } catch (NumberFormatException e) {
                return (new IsValidDto("The property is from integer type but your value ("+newValue+") isn't!",false));

            }
        }
        else
        {
            try {
                float floatValue = (Float.parseFloat(newValue));
                {
                    if(((FloatType)property).getRange().getFrom()<=floatValue&&((FloatType)property).getRange().getTo()>=floatValue) {
                        property.setValue(floatValue,0);
                        property.setUserEnterValue(true);
                        return (new IsValidDto(INSERT_SUCCEED,true));
                    }
                    else {
                        return (new IsValidDto("Your value ("+newValue+") isn't in the correct range!\nThe range is between: "+((FloatType)property).getRange().getFrom()+" to "+((FloatType)property).getRange().getTo()+"!",false));
                    }

                }

            } catch (NumberFormatException e) {
                return (new IsValidDto("The property is from float type but your value ("+newValue+") isn't!",false));

            }
        }
    }
}
