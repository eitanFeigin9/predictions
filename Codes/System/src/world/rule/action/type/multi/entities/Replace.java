package world.rule.action.type.multi.entities;

import exception.FormatException;
import exception.InvalidAction;
import exception.InvalidValue;
import exception.ValueNotExists;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.property.Property;
import world.property.type.BooleanType;
import world.property.type.DecimalType;
import world.property.type.FloatType;
import world.property.type.StringType;
import world.property.type.range.sub.FloatRange;
import world.property.type.range.sub.IntRange;
import world.rule.action.Action;
import world.rule.action.type.complex.condition.Condition;

import java.util.Map;

public class Replace extends Action {
    private String createEntityName;
    private String mode;
    private EntityInstance[][] gridArray;
    private final String STRING_TYPE="String";
    private final String BOOL_TYPE="Boolean";
    private final String INT_TYPE="Decimal";
    private final String FLOAT_TYPE="Float";
    private Map<String,EntityDefinition> entityDefinitionMap;
    public Replace(String killEntityName, EntityDefinition secondryEntity, String count, Condition seconderyCondition,String createEntityName,String mode,Map<String,EntityDefinition> entityDefinitionMap,EntityInstance[][] gridArray) {
        super(killEntityName, secondryEntity, count, seconderyCondition);
        this.createEntityName=createEntityName;
        this.mode=mode;
        this.entityDefinitionMap=entityDefinitionMap;
        this.gridArray=gridArray;
    }

    @Override
    public void executeAction(EntityInstance mainEntityInstance, Environment environment, int currTicks, EntityInstance secondaryInstance) throws FormatException, InvalidAction, ValueNotExists, InvalidValue {
        EntityInstance newInstance = new EntityInstance(createEntityName);
        if(!entityDefinitionMap.containsKey(createEntityName))
        {
            throw new InvalidAction("In the replace action the create entity (" + createEntityName + ") is not one of the entities in the system!");
        }
        else if (mode.equals("scratch")){
            EntityDefinition createEntityDefinition = this.entityDefinitionMap.get(createEntityName);
            EntityInstance template = null;
               for(Map.Entry<Integer,EntityInstance> curr:createEntityDefinition.getEntities().entrySet()){
                    template = curr.getValue();
                   break;
               }
               if (template!=null){
            createPropertiesForInstance(newInstance, template);
            createEntityDefinition.addEntity(newInstance);
            createEntityDefinition.setPopulation(createEntityDefinition.getPopulation()+1);
            this.gridArray[mainEntityInstance.getRow()][mainEntityInstance.getCol()]=newInstance;
                   newInstance.setRow(mainEntityInstance.getRow());
                   newInstance.setCol(mainEntityInstance.getCol());
            mainEntityInstance.setAlive(false);
            }
        }
        else if (mode.equals("derived")) {
            EntityDefinition createEntityDefinition = this.entityDefinitionMap.get(createEntityName);
            EntityInstance template = null;
            for (Map.Entry<Integer, EntityInstance> curr : createEntityDefinition.getEntities().entrySet()) {
                template = curr.getValue();
                break;
            }
            if (template != null) {
                copySamePropertiesFromKillToCreate(newInstance, template, mainEntityInstance);
                createEntityDefinition.addEntity(newInstance);
                createEntityDefinition.setPopulation(createEntityDefinition.getPopulation() + 1);
                this.gridArray[mainEntityInstance.getRow()][mainEntityInstance.getCol()] = newInstance;
                newInstance.setRow(mainEntityInstance.getRow());
                newInstance.setCol(mainEntityInstance.getCol());
                mainEntityInstance.setAlive(false);
            }
        }
        else {
                throw new InvalidAction("In the replace action on the create entity (" + createEntityName + ") and the second entity (" + secondaryInstance.getNameOfEntity() + ") there is an invalid mode ("+mode+")!\nThe valid modes are: derived and scratch!");
            }

    }


    public void createPropertiesForInstance(EntityInstance newInstance,EntityInstance propertiesTemplate){
        Map<String, Property> propertyMap=propertiesTemplate.getEntityProperties();
        for(Map.Entry<String, Property> currProp :propertyMap.entrySet() ){
            insertProperty(newInstance,currProp.getValue());
        }
    }
    public void insertProperty(EntityInstance newInstance,Property currProp)
    {

        if(currProp.getValue().getClass().getSimpleName().equals(STRING_TYPE)){
            if(((StringType)(currProp)).isRandomInitialize())
            {
                StringType newProp = new StringType(currProp.getPropertyName());
                newInstance.addProperty(newProp);
            }
            else {
                StringType newProp = new StringType(currProp.getPropertyName(), ((StringType)(currProp)).getInitValue());
                newInstance.addProperty(newProp);
            }
        }
        else if(currProp.getValue().getClass().getSimpleName().equals(INT_TYPE)){
            if(((DecimalType)(currProp)).isRandomInitialize())
            {
                DecimalType newProp = new DecimalType(currProp.getPropertyName(),((DecimalType)(currProp)).getRange());
                newInstance.addProperty(newProp);
            }
            else {
                DecimalType newProp = new DecimalType(currProp.getPropertyName(),((DecimalType)(currProp)).getRange(), ((DecimalType)(currProp)).getInitValue());
                newInstance.addProperty(newProp);
            }
        }
        else if(currProp.getValue().getClass().getSimpleName().equals(BOOL_TYPE)) {
            if (((BooleanType) (currProp)).isRandomInitialize()) {
                BooleanType newProp = new BooleanType(currProp.getPropertyName());
                newInstance.addProperty(newProp);
            } else {
                BooleanType newProp = new BooleanType(currProp.getPropertyName(), ((BooleanType) (currProp)).getInitValue());
                newInstance.addProperty(newProp);
            }
        }
        else {
            if(((FloatType)(currProp)).isRandomInitialize())
            {
                FloatType newProp = new FloatType(currProp.getPropertyName(),((FloatType)(currProp)).getRange());
                newInstance.addProperty(newProp);
            }
            else {
                FloatType newProp = new FloatType(currProp.getPropertyName(),((FloatType)(currProp)).getRange(), ((FloatType)(currProp)).getInitValue());
                newInstance.addProperty(newProp);
            }
        }
    }
    public void copySamePropertiesFromKillToCreate(EntityInstance newInstance,EntityInstance propertiesTemplate,EntityInstance killInstance) {
        Map<String, Property> createPropertyMap = propertiesTemplate.getEntityProperties();
        Map<String, Property> killPropertyMap = killInstance.getEntityProperties();

        for (Map.Entry<String, Property> currProp : createPropertyMap.entrySet()) {
            if (killPropertyMap.containsKey(currProp.getKey())) {
                Property killCurrProp = killPropertyMap.get(currProp.getKey());

                if (currProp.getValue().getClass().getSimpleName().equals(killCurrProp.getClass().getSimpleName())) {
                    if (currProp.getValue().getClass().getSimpleName().equals("StringType")) {
                        StringType newProp = new StringType(currProp.getValue().getPropertyName(), (String) ((StringType) killCurrProp).getValue());
                        newInstance.addProperty(newProp);
                    } else if (currProp.getValue().getClass().getSimpleName().equals(INT_TYPE)) {
                        DecimalType newProp = new DecimalType(currProp.getValue().getPropertyName(), ((DecimalType) (killCurrProp)).getRange(), (Integer) ((DecimalType) (killCurrProp)).getValue());
                        newInstance.addProperty(newProp);
                    } else if (currProp.getValue().getClass().getSimpleName().equals("BooleanType")) {
                        BooleanType newProp = new BooleanType(currProp.getValue().getPropertyName(), (Boolean) ((BooleanType) (killCurrProp)).getValue());
                        newInstance.addProperty(newProp);
                    } else {
                        FloatType newProp = new FloatType(currProp.getValue().getPropertyName(), ((FloatType) (killCurrProp)).getRange(), (Float) ((FloatType) (killCurrProp)).getValue());
                        newInstance.addProperty(newProp);
                    }
                }
                else if(currProp.getValue().getClass().getSimpleName().equals(FLOAT_TYPE)&&killCurrProp.getClass().getSimpleName().equals(INT_TYPE)){
                    float from=((DecimalType) (killCurrProp)).getRange().getFrom(),to=((DecimalType) (killCurrProp)).getRange().getTo();
                    FloatRange newRange=new FloatRange(from,to);
                    FloatType newProp = new FloatType(currProp.getValue().getPropertyName(),newRange, (Float) ((DecimalType) (killCurrProp)).getValue());
                    newInstance.addProperty(newProp);
                }
                else {
                    insertProperty(newInstance,currProp.getValue());
                }
            }
            else{
                insertProperty(newInstance,currProp.getValue());
            }
        }
    }
    public String getData() {
        return "Action Type : Replace\nKill entity : " + super.getMainEntity() +"\nCreate Entity : "+super.getSecondryEntity().getEntityName()+"\nMode : "+mode;
    }
}

