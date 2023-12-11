package amalgama.xml.map.spawn;

import amalgama.xml.map.Vector3d;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "spawn-point")
public class SpawnPositionModel {

    private Vector3d position;


    private Vector3d rotation;


    private String type;

    public Vector3d getPosition() {
        return position;
    }

    @XmlElement(name = "position")
    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3d getRotation() {
        return rotation;
    }

    @XmlElement(name = "rotation")
    public void setRotation(Vector3d rotation) {
        this.rotation = rotation;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }
}
