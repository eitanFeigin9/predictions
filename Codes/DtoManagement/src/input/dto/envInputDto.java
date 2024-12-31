package input.dto;

public class envInputDto extends InstructionsDto{
    private String propName;
    private String propValue;

    public envInputDto(String instruction, String propName,String propValue) {
        super(instruction);
        this.propName=propName;
        this.propValue=propValue;
    }

    public String getPropName() {
        return  this.propName;
    }

    public String getPropValue() {
        return this.propValue;
    }
}