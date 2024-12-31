package system.file.dto;

import java.io.Serializable;

public class ManageDto implements Serializable {
    private boolean isInstructionSucceed;

    public ManageDto(boolean isInstructionSucceed) {
        this.isInstructionSucceed = isInstructionSucceed;
    }

    public boolean isInstructionSucceed() {
        return isInstructionSucceed;
    }
}
