package system.file.instruction.show.past.simulation.dto;

import system.file.dto.ManageDto;

public class ShowSimulationDto extends ManageDto {
    private String result;

    public ShowSimulationDto(boolean isInstructionSucceed, String result) {
        super(isInstructionSucceed);
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}