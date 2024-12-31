package system.file.instruction.show.dto;

import system.file.dto.ManageDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntitiesRunAgainDto extends ManageDto {
    Map<String,String> valuesMap;
    Map<String, Map<String, Map<String, String>>> data;
    public EntitiesRunAgainDto(Map<String,String> valuesMap,Map<String, Map<String, Map<String, String>>> allData) {
        super(true);
        this.valuesMap=valuesMap;
        this.data=allData;
    }

    public Map<String, String> getValuesMap() {
        return valuesMap;
    }

    public Map<String, Map<String, Map<String, String>>> getData() {
        return data;
    }
    public List<String> getEntitiesNamesData() {
        List<String> resList = new ArrayList<>(this.valuesMap.keySet());
        return resList;
    }
}
