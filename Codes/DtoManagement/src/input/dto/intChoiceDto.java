package input.dto;

public class intChoiceDto  extends InstructionsDto{
    int userChoice;
    public intChoiceDto(String instruction, int userChoice) {
        super(instruction);
        this.userChoice= userChoice;
    }

    public int getUserChoice() {
        return userChoice;
    }
}
