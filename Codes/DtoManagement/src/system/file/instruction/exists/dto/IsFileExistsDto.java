package system.file.instruction.exists.dto;

import system.file.dto.ManageDto;

public class IsFileExistsDto extends ManageDto {


    private String status;

    public IsFileExistsDto(String status, Boolean isValid) {
        super(isValid);
        this.status=status;
    }

    public String getStatus() {
        return status;
    }
}
