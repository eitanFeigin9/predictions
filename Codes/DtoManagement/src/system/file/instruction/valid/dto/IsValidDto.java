package system.file.instruction.valid.dto;

import system.file.dto.ManageDto;

import java.io.Serializable;

public class IsValidDto extends ManageDto implements Serializable {
    private Boolean isValid;
    private String status;

    public IsValidDto(String status, Boolean isValid) {
        super(isValid);
        this.isValid = isValid;
        this.status=status;
    }

    public Boolean getValid() {
        return isValid;
    }

    public String getStatus() {
        return status;
    }
}
