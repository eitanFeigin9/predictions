package system.file.instruction.show.dto;

import system.file.dto.ManageDto;

import java.util.Map;

public class EntitiesDto extends ManageDto {
    private Map<String, Map<String, Map<String, String>>> result;
    public EntitiesDto(Map<String, Map<String, Map<String, String>>> entitesMap) {
        super(true);
        this.result=entitesMap;
    }
    public Map<String, Map<String, Map<String, String>>>  getEntitiesMapDto(){

        return this.result;
    }
}


