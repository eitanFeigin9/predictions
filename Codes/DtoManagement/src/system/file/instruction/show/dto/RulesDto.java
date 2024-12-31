package system.file.instruction.show.dto;

import system.file.dto.ManageDto;

import java.util.List;
import java.util.Map;

public class RulesDto extends ManageDto {
    private Map<String, Map<String, List<String>>> actionsData;
    private Map<String, List<String>> activationData;
    public RulesDto(Map<String, Map<String, List<String>>> actionsData,Map<String, List<String>> activationData) {
        super(true);
     this.activationData=activationData;
     this.actionsData=actionsData;
    }
    public  Map<String, Map<String, List<String>>>   getActionsMap(){

        return this.actionsData;
    }
    public  Map<String, List<String>>  getActivationMap(){

        return this.activationData;
    }
}
