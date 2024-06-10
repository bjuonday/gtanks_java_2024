package amalgama.xml.map.bonus;

import amalgama.xml.map.Vector3d;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;

public class BonusRegionModel {

    private Vector3d max;


    private Vector3d min;


    private String type;

    public Vector3d getMax() {
        return max;
    }

    @XmlElement(name = "max")
    public void setMax(Vector3d max) {
        this.max = max;
    }

    public Vector3d getMin() {
        return min;
    }

    @XmlElement(name = "min")
    public void setMin(Vector3d min) {
        this.min = min;
    }

    public String getType() {
        return type;
    }

    @XmlElement(name = "bonus-type")
    public void setType(String type) {
        this.type = type;
    }
}
