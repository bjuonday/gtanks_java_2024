package amalgama.battle;

import amalgama.models.MapPointModel;
import amalgama.xml.map.MapModel;

import java.util.ArrayList;

public class StructMap {
    public MapModel model = null;
    public ArrayList<MapPointModel> GREENSpawnPoints = new ArrayList<>();
    public ArrayList<MapPointModel> REDSpawnPoints = new ArrayList<>();
    public ArrayList<MapPointModel> BLUESpawnPoints = new ArrayList<>();
    public ArrayList<BonusRegion> goldRegions = new ArrayList<>();
    public ArrayList<BonusRegion> crystalRegions = new ArrayList<>();
    public ArrayList<BonusRegion> hpRegions = new ArrayList<>();
    public ArrayList<BonusRegion> daRegions = new ArrayList<>();
    public ArrayList<BonusRegion> ddRegions = new ArrayList<>();
    public ArrayList<BonusRegion> nitroRegions = new ArrayList<>();

    public StructMap() { }

    public StructMap(MapModel model, ArrayList<MapPointModel> GREENSpawnPoints, ArrayList<MapPointModel> REDSpawnPoints, ArrayList<MapPointModel> BLUESpawnPoints, ArrayList<BonusRegion> goldRegions, ArrayList<BonusRegion> crystalRegions, ArrayList<BonusRegion> hpRegions, ArrayList<BonusRegion> daRegions, ArrayList<BonusRegion> ddRegions, ArrayList<BonusRegion> nitroRegions) {
        this.model = model;
        this.GREENSpawnPoints = GREENSpawnPoints;
        this.REDSpawnPoints = REDSpawnPoints;
        this.BLUESpawnPoints = BLUESpawnPoints;
        this.goldRegions = goldRegions;
        this.crystalRegions = crystalRegions;
        this.hpRegions = hpRegions;
        this.daRegions = daRegions;
        this.ddRegions = ddRegions;
        this.nitroRegions = nitroRegions;
    }
}
