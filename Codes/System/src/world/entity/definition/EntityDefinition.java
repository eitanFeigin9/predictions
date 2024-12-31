package world.entity.definition;

import world.entity.execution.EntityInstance;
import world.property.Property;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

 public class EntityDefinition implements Serializable {
    private String entityName;
    private int population;
    private Map<Integer, EntityInstance> entities;
    private boolean isUserInputPopulation ;

    public EntityDefinition(String entityName, int population) {
        this.entityName = entityName;
        this.population = population;
        this.entities= new LinkedHashMap<>();
        this.isUserInputPopulation=false;
    }

     public boolean isUserInputPopulation() {
         return isUserInputPopulation;
     }

     public void setUserInputPopulation(boolean userInputPopulation) {
         isUserInputPopulation = userInputPopulation;
     }

     public String getEntityName() {
        return entityName;
    }

    public int getPopulation() {
        return population;
    }


    public Map<Integer, EntityInstance> getEntities() {
        return entities;
    }
    public void resetMap(){
        this.entities= new LinkedHashMap<>();

    }
    public void addEntity(EntityInstance newEntity) {
        entities.put(newEntity.getId(),newEntity);
    }

     public void setPopulation(int population) {
         this.population = population;
     }

     @Override
     public String toString() {
         String result = "Entity name = " + this.entityName + "\nPopulation = " + this.population + "\n";
         if (!(this.entities.isEmpty()))
         {
             EntityInstance entity = entities.values().iterator().next();
             result += entity.toString();
         }
         return result;
     }
     
}
