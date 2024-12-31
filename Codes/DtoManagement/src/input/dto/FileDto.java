package input.dto;

public class FileDto extends InstructionsDto{
    private String filePath;
    public FileDto(String instruction, String filePath) {
        super(instruction);
        this.filePath=filePath;
    }

    public String getFilePath() {
        return filePath;
    }

}
