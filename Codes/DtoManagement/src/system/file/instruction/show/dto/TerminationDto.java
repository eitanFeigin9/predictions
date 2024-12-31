package system.file.instruction.show.dto;

import system.file.dto.ManageDto;

import java.util.List;

public class TerminationDto extends ManageDto {
   private List<String> termination;
    public TerminationDto(List<String> termination) {
        super(true);
        this.termination=termination;
    }
    public  List<String> getTermination() {return termination;}

}
