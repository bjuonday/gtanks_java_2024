package amalgama.xml.map;

import amalgama.xml.map.bonus.BonusRegionModel;

import amalgama.xml.map.spawn.SpawnPositionModel;
import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "map")
public class MapModel {

    private SpawnPointModel spawnPoints;


    private BonusRegionsModel bonusRegions;


    private FlagPositionsModel flagPositions;

    public SpawnPointModel getSpawnPoints() {
        return this.spawnPoints;
    }

    @XmlElement(name = "spawn-points")
    public void setSpawnPoints(SpawnPointModel spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public BonusRegionsModel getBonusRegions() {
        return this.bonusRegions;
    }

    @XmlElement(name = "bonus-regions")
    public void setBonusRegions(BonusRegionsModel bonusRegions) {
        this.bonusRegions = bonusRegions;
    }

    public FlagPositionsModel getFlagPositions() {
        return this.flagPositions;
    }

    @XmlElement(name = "ctf-flags")
    public void setFlagPositions(FlagPositionsModel flagPositions) {
        this.flagPositions = flagPositions;
    }

    public Vector3d getPositionBlueFlag() {
        return (getFlagPositions() != null) ? getFlagPositions().getBlueFlag() : null;
    }

    public Vector3d getPositionRedFlag() {
        return (getFlagPositions() != null) ? getFlagPositions().getRedFlag() : null;
    }

    public List<SpawnPositionModel> getSpawnPositions() {
        return this.spawnPoints.getSpawnPositions();
    }

    public List<SpawnPositionModel> getSpawnPositionsForTeam(String team) {
        List<SpawnPositionModel> list = new ArrayList<>();
        for (var point : getSpawnPositions()) {
            if (point.getType().equalsIgnoreCase(team))
                list.add(point);
        }
        return list;
    }

    public List<BonusRegionModel> getBonusesRegion() {
        return this.bonusRegions.getBonusRegions();
    }
}
