package world.rule.action.expression;

import exception.FormatException;
import exception.InvalidAction;
import exception.ValueNotExists;
import world.entity.execution.EntityInstance;
import world.property.Property;
import world.rule.action.function.functions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagementExpression  implements  Serializable {
    final static Pattern ENVIRONMENT_PATTERN = Pattern.compile("environment\\((.*?)\\)");
    final static Pattern RANDOM_PATTERN = Pattern.compile("random\\((.*?)\\)");
    final static Pattern EVALUATE_PATTERN = Pattern.compile("evaluate\\((.*?)\\.(.*?)\\)");
    private static final Pattern PERCENT_PATTERN = Pattern.compile("percent\\((.+?),(.+?)\\)");
    final static Pattern TICKS_PATTERN = Pattern.compile("ticks\\(([^.]+)\\.([^)]+)\\)");

    public static Object getExpressionValue(Map<String, Property > environmentProperties, String expression, EntityInstance mainEntityInstance,EntityInstance secondEntityInstance,int currTick) throws InvalidAction, ValueNotExists, FormatException {
        Object value ;

        if (isPattern(expression,ENVIRONMENT_PATTERN)) {
             value = functions.environment(environmentProperties, extractArgumentFromExpression(expression,ENVIRONMENT_PATTERN));
        }
        else if (isPattern(expression,RANDOM_PATTERN)) {
            String argument = extractArgumentFromExpression(expression, RANDOM_PATTERN);
            if (argument != null) {
                if (isInteger(argument)) {
                    value = functions.random(Integer.parseInt(argument));
                } else {
                    throw new FormatException("The random expression expected to receive an integer but received the value: " + argument + " that isn't an integer!");
                }
            } else {
                throw new InvalidAction("The random expression didn't receive a value!");
            }
        }
        else if (isPattern(expression,EVALUATE_PATTERN)) {
            String[] parts =extractTwoArgumentFromExpression(expression, EVALUATE_PATTERN);

            if (parts == null) {
                throw new FormatException("The evaluate expression expected to receive an <Entity name.Property name> but received: " + expression + "!");
            }
            else {
                String entityName =parts[0];
                if (!mainEntityInstance.getNameOfEntity().equals(entityName)) {
                    if (secondEntityInstance != null) {
                        if (!secondEntityInstance.getNameOfEntity().equals(entityName)) {
                            throw new FormatException("The evaluate expression expected to receive an <Entity name.Property name> but the string before the <.> (" + entityName + ") isn't the entity that the action is on!");
                        } else {
                            String propertyName = parts[1];
                            if (!secondEntityInstance.getEntityProperties().containsKey(propertyName)) {
                                throw new FormatException("The evaluate expression expected to receive an <Entity name.Property name> but the string after the <.> (" + propertyName + ") isn't a property of this entity (" + entityName + ")!");
                            } else {
                                value = secondEntityInstance.getEntityProperties().get(propertyName).getValue();

                            }
                        }
                    } else {
                        throw new FormatException("The evaluate expression expected to receive an <Entity name.Property name> but the string before the <.> (" + entityName + ") isn't the entity that the action is on!");
                    }
                } else {
                    String propertyName = parts[1];
                    if (!mainEntityInstance.getEntityProperties().containsKey(propertyName)) {
                        throw new FormatException("The evaluate expression expected to receive an <Entity name.Property name> but the string after the <.> (" + propertyName + ") isn't a property of this entity (" + entityName + ")!");
                    }
                    value = mainEntityInstance.getEntityProperties().get(propertyName).getValue();
                }
            }



        }

        else if (isPattern(expression,PERCENT_PATTERN)) {
            String[] parts =extractTwoArgumentFromExpression(expression, PERCENT_PATTERN);

            if (parts == null) {
                throw new FormatException("The percent expression expected to receive an (expression1,expression2) but received: " + expression + "!");
            }
            else {
                    String arg1 = parts[0];
                Object valArg1,valArg2;
                try {
                     valArg1 = getExpressionValue(environmentProperties, arg1, mainEntityInstance, secondEntityInstance,currTick);
                } catch (InvalidAction |ValueNotExists |FormatException e) {
                    throw new FormatException("In the percent function, the first argument ("+arg1+") had an error!"+e.getMessage());
                }
                String arg2 = parts[1];
                try {
                     valArg2 = getExpressionValue(environmentProperties, arg2, mainEntityInstance, secondEntityInstance,currTick);
                } catch (InvalidAction |ValueNotExists |FormatException e) {
                    throw new FormatException("In the percent function, the second argument ("+arg2+") had an error!"+e.getMessage());
                }
                float floatValue1,floatValue2;
                if (valArg1 instanceof Integer ) {
                    int intValue = (Integer) valArg1;
                  floatValue1 = (float) intValue;
                }
                else if(valArg1 instanceof Float){
                    floatValue1 = (float) valArg1;

                }
                else {
                    throw new FormatException("In the percent function, the first argument ("+arg1+") value isn't a number!");
                }
                if (valArg2 instanceof Integer ) {
                    int intValue2 = (Integer) valArg2;
                    floatValue2 = (float) intValue2;
                }
                else if(valArg2 instanceof Float){
                    floatValue2 = (float) valArg2;

                }
                else {
                    throw new FormatException("In the percent function, the second argument ("+arg2+") value isn't a number!");
                }
                if(floatValue2<=0){
                    throw new InvalidAction("In the percent function, the second argument ("+arg2+") value "+floatValue2+" isn't a positive number!\nThe percentage needs to be a positive number.");
                }
                value =(float)((floatValue2 / 100.0) * floatValue1);
            }
        }
        else if (isPattern(expression,TICKS_PATTERN)) {
            String[] parts;
            if (expression.split("\\.").length >= 2) {
               parts = Pattern.compile("\\.").split(expression.substring(6, expression.length() - 1));
            } else {
                throw new FormatException("The ticks expression expected to receive an <Entity name.Property name> but received: " + expression + "!");
            }
            List<String> entityPropertyList=new ArrayList<>();
            entityPropertyList.add(parts[0]);
            entityPropertyList.add(parts[1]);
            String entityName=entityPropertyList.get(0);
            if(!mainEntityInstance.getNameOfEntity().equals(entityName))
            {
                if(secondEntityInstance!=null)
                {
                    if(!secondEntityInstance.getNameOfEntity().equals(entityName))
                    {
                        throw new FormatException("The ticks expression expected to receive an <Entity name.Property name> but the string before the <.> ("+entityName+ ") isn't the entity that the action is on!");
                    }
                    else {
                        String propertyName=entityPropertyList.get(1);
                        if(!secondEntityInstance.getEntityProperties().containsKey(propertyName)) {
                            throw new FormatException("The ticks expression expected to receive an <Entity name.Property name> but the string after the <.> (" + propertyName + ") isn't a property of this entity (" + entityName + ")!");
                        }
                        else {
                                value = currTick - secondEntityInstance.getEntityProperties().get(propertyName).getLastValueChangedTick();
                        }
                    }
                }
                else {
                    throw new FormatException("The ticks expression expected to receive an <Entity name.Property name> but the string before the <.> (" + entityName + ") isn't the entity that the action is on!");
                }
            }
            else {
                String propertyName = entityPropertyList.get(1);
                if (!mainEntityInstance.getEntityProperties().containsKey(propertyName)) {
                    throw new FormatException("The ticks expression expected to receive an <Entity name.Property name> but the string after the <.> (" + propertyName + ") isn't a property of this entity (" + entityName + ")!");
                }
                value = currTick - mainEntityInstance.getEntityProperties().get(propertyName).getLastValueChangedTick();
            }



        }
        else if (getEntityProperty(mainEntityInstance, expression) != null) {
            value = getEntityProperty(mainEntityInstance, expression);
        }
        else {
            value = convertExpressionToFreeValue(expression);
        }
        return value;
    }
    public static Object convertExpressionToFreeValue(String expression) throws InvalidAction {
        if(expression==null)
        {
            throw new InvalidAction("There is an empty expression!\nExpressions must contain value.");
        }
        else if (isBoolean(expression)) {
            return Boolean.parseBoolean(expression);
        }
        else if (isInteger(expression)) {
            return Integer.parseInt(expression);
        }
        else if (isFloat(expression)) {
            return Float.parseFloat(expression);
        }
        else {
            return expression;
        }
    }
    public static boolean isBoolean(String str) {
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static Object getEntityProperty(EntityInstance mainEntityInstance, String expression) {

        if(mainEntityInstance.getEntityProperties().containsKey(expression)) {
            return mainEntityInstance.getEntityProperties().get(expression).getValue();
        }
        else {
            return null;
        }
    }
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isPattern(String input, Pattern pattern) {

        boolean patternYes;
        try {
            Matcher matcher = pattern.matcher(input);
            patternYes = matcher.matches();
            return patternYes;
        }
        catch (Exception e) {
            return false;
        }
    }
    public static String extractArgumentFromExpression(String expression, Pattern pattern) {
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) {

            return matcher.group(1);
        }
        else {
            return null;
        }
    }
    public static String[] extractTwoArgumentFromExpression(String str,Pattern pattern) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return new String[]{matcher.group(1), matcher.group(2)};
        } else {
            return null;
        }
    }
    }




