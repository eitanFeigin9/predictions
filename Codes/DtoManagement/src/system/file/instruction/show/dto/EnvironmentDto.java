package system.file.instruction.show.dto;

import system.file.dto.ManageDto;

import java.util.Map;

public class EnvironmentDto  extends ManageDto {
    private  Map<String, Map<String, String>> result;
    public EnvironmentDto(Map<String, Map<String, String>> environmentMap) {
        super(true);
        this.result=environmentMap;
    }
    public Map<String, Map<String, String>>  getenvironmentMapDto(){
        return this.result;
    }
}