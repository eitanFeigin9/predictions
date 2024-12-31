package input.dto;

public class InstructionsDto {
    private String instruction;

    public InstructionsDto(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
