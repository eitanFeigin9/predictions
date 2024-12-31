package system.file.jaxb.unmarshall;


import schema.ex2.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class UnmarshallFile {
    private final String SCHEMA_PATH = "schema.ex2";
    private String filePath;

    public UnmarshallFile(String filePath) {
        this.filePath = filePath;
    }
    public PRDWorld loadJaxbFormat() throws JAXBException {
        try {
            JAXBContext context = JAXBContext.newInstance(SCHEMA_PATH);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (PRDWorld) unmarshaller.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            throw new JAXBException("The file's format doesn't match the requested format!");
        }
    }
}
