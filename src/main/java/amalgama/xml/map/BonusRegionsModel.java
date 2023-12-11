package amalgama.xml.map;

import amalgama.xml.map.bonus.BonusRegionModel;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "bonus-regions")
public class BonusRegionsModel {
    private List<BonusRegionModel> bonusRegions;

    public List<BonusRegionModel> getBonusRegions() {
        return bonusRegions;
    }

    @XmlElement(name = "bonus-region")
    public void setBonusRegions(List<BonusRegionModel> bonusRegions) {
        this.bonusRegions = bonusRegions;
    }
}
