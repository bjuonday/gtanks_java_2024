package amalgama.xml.map;

import amalgama.xml.map.spawn.SpawnPositionModel;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "spawn-points")
public class SpawnPointModel {

    private List<SpawnPositionModel> spawnPositions;

    public List<SpawnPositionModel> getSpawnPositions() {
        return this.spawnPositions;
    }

    @XmlElement(name = "spawn-point")
    public void setSpawnPositions(List<SpawnPositionModel> spawnPositions) {
        this.spawnPositions = spawnPositions;
    }
}
