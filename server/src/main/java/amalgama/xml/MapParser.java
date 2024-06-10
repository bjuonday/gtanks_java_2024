package amalgama.xml;

import amalgama.xml.map.MapModel;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.JAXBException;
import java.io.File;

public class MapParser {
    private JAXBContext jc;
    private Unmarshaller unmarshaller;

    public MapParser() throws JAXBException {
        this.jc = JAXBContext.newInstance(MapModel.class);
        this.unmarshaller = jc.createUnmarshaller();
    }

    public MapModel parse(File file) throws JAXBException {
        return (MapModel) unmarshaller.unmarshal(file);
    }
}
