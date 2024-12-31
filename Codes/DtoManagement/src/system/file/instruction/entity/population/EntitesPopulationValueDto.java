package system.file.instruction.entity.population;

import system.file.dto.ManageDto;

import java.util.Map;

public class EntitesPopulationValueDto  extends ManageDto {
    Map<String, Integer> entitesPopulationValuesMap;
    public EntitesPopulationValueDto(Map<String, Integer> entitesPopulationValuesMap) {
        super(true);
        this.entitesPopulationValuesMap=entitesPopulationValuesMap;
    }

    public Map<String, Integer> getEntitesPopulationValuesMap() {
        return entitesPopulationValuesMap;
    }

}
