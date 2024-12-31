package system.file.instruction.show.dto;

import system.file.dto.ManageDto;

import java.util.Map;

public class EnviromentRunAgainDto extends ManageDto {
    Map<String,String>res;
    Map<String, Map<String, String>> data;
    public EnviromentRunAgainDto(Map<String,String> valuesMap,Map<String, Map<String, String>> allData) {
        super(true);
        this.res=valuesMap;
        this.data=allData;
    }

    public Map<String, String> getRes() {
        return res;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }
}
