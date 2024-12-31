package system.file.instruction.valid;

import convert.world.WorldConvert;
import exception.*;
import system.file.jaxb.unmarshall.UnmarshallFile;
import schema.ex2.PRDWorld;
import world.World;

import javax.xml.bind.JAXBException;

public class IfValidCreateWorld {
    private final String FILE_IS_VALID="The file is valid!";
    private World currWorld;

    public IfValidCreateWorld() {
        this.currWorld = null;
    }

    public World createWorld(String filePath) throws JAXBException, InvalidAction, RangeException, InvalidValue, ValueNotExists, ValueExists, FormatException {
        UnmarshallFile check = new UnmarshallFile(filePath);
        PRDWorld newWorldPrd=check.loadJaxbFormat();
        WorldConvert worldConvert = new WorldConvert(newWorldPrd);
        this.currWorld=worldConvert.convertPrdWorldToWorld();
        return currWorld;
    }
}
