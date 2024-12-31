package system.file.instruction.entity.population;

import system.file.dto.ManageDto;

public class PopulationUpdatesDto extends ManageDto {
    private String status;
    private int currDimension;
    public PopulationUpdatesDto(boolean isInstructionSucceed,String status, int currDimension) {
        super(isInstructionSucceed);
        this.status=status;
        this.currDimension=currDimension;
    }

    public int getCurrDimension() {
        return currDimension;
    }

    public String getStatus() {
        return status;
    }

}
