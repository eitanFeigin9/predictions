package system.file.instruction.entity;

import system.file.dto.ManageDto;
import world.entity.definition.EntityDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntitesListDto extends ManageDto {
    private Map<String, EntityDefinition> entitiesMap;
    public EntitesListDto( Map<String, EntityDefinition> entities) {
        super(true);
        this.entitiesMap=entities;
    }
    public List<String> getEntitiesNames(){
        List<String> res = new ArrayList<>();
        for(Map.Entry<String, EntityDefinition> currEntity: entitiesMap.entrySet()){
            res.add(currEntity.getKey());
        }
        return res;
    }
}
