package amalgama.xml.map;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "ctf-flags")
public class FlagPositionsModel {

    private Vector3d redFlag;


    private Vector3d blueFlag;

    public Vector3d getRedFlag() {
        return this.redFlag;
    }

    @XmlElement(name = "flag-red")
    public void setRedFlag(Vector3d redFlag) {
        this.redFlag = redFlag;
    }

    public Vector3d getBlueFlag() {
        return this.blueFlag;
    }

    @XmlElement(name = "flag-blue")
    public void setBlueFlag(Vector3d blueFlag) {
        this.blueFlag = blueFlag;
    }

    public String toString() {
        return "red flag: " + this.redFlag + " blue: " + this.blueFlag;
    }
}
