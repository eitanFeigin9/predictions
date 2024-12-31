package system.file.instruction.show.past.simulation.dto;

import system.file.dto.ManageDto;

public class SelectFromListDto extends ManageDto {
    private int numberOfOptions;
    private String options;

    public SelectFromListDto(boolean isInstructionSucceed, int numberOfOptions, String options) {
        super(isInstructionSucceed);
        this.numberOfOptions = numberOfOptions;
        this.options = options;
    }

    public int getNumberOfOptions() {
        return numberOfOptions;
    }

    public String getOptions() {
        return options;
    }
}
