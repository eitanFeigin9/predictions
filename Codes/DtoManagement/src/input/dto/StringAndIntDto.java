package input.dto;

public class StringAndIntDto  extends InstructionsDto{
    private int number;
    private String string;

    public StringAndIntDto(String instruction,int number, String string) {
        super(instruction);
        this.number = number;
        this.string = string;
    }

    public int getNumberData() {
        return number;
    }

    public String getStringData() {
        return string;
    }
}