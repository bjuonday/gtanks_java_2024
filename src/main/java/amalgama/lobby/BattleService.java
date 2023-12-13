package amalgama.lobby;

import amalgama.Global;
import amalgama.MapLoader;
import amalgama.battle.BattlePlayerController;
import amalgama.battle.Bonus;
import amalgama.battle.SpawnState;
import amalgama.battle.TankKillService;
import amalgama.database.UserItem;
import amalgama.database.UserMount;
import amalgama.database.dao.UserItemDAO;
import amalgama.database.dao.UserMountDAO;
import amalgama.models.HullModificationModel;
import amalgama.models.MapPointModel;
import amalgama.models.SpawnPositionModel;
import amalgama.models.TurretModificationModel;
import amalgama.network.Type;
import amalgama.network.netty.TransferProtocol;
import amalgama.network.secure.Grade;
import amalgama.system.quartz.IQuartzService;
import amalgama.system.quartz.QuartzService;
import amalgama.system.quartz.TimeUnit;
import amalgama.system.timers.TankRespawner;
import amalgama.utils.RandomUtils;
import amalgama.utils.RankUtils;
import amalgama.xml.map.MapModel;
import amalgama.xml.map.Vector3d;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.Destroyable;
import java.util.*;

public class BattleService implements Destroyable {
    public static final String QUARTZ_GROUP = BattleService.class.getName();
    public final String QUARTZ_NAME;
    public final String QUARTZ_RESTART_NAME;
    public TankKillService killService;
    private long endTimePoint;
    public final HashMap<String, BattlePlayerController> players = new HashMap<>();
    public HashMap<String, Bonus> activeBonuses = new HashMap<>();
    public final Battle battle;

    private IQuartzService quartzService = QuartzService.getInstance();

    public BattleService(Battle battle) {
        this.battle = battle;
        QUARTZ_NAME = "BattleTimer/" + hashCode() + "/" + battle.id;
        QUARTZ_RESTART_NAME = "BattleRestarter/" + hashCode() + "/" + battle.id;
        killService = new TankKillService(this);
        if (battle.timeLength > 0)
            startTimer();
    }

    private void startTimer() {
        this.endTimePoint = System.currentTimeMillis() + battle.timeLength * 1000L;
        this.quartzService.addJob(QUARTZ_NAME, QUARTZ_GROUP, e -> {
            System.out.println("End battle");
            battle.startTime = System.currentTimeMillis() / 1000L;
            killService.restartBattle(true);
        }, TimeUnit.SECONDS, battle.timeLength);
    }

    public void broadcast(Type type, String... args) {
        for (var ply : players.values())
            ply.net.send(type, args);
    }

    public void broadcastExcept(BattlePlayerController exclude, Type type, String... args) {
        for (var ply : players.values())
            if (!ply.equals(exclude))
                ply.net.send(type, args);
    }

    public void kickPlayer(String nickname) {
        TransferProtocol ply = players.get(nickname).net;
        players.remove(nickname);
        ply.close();
    }

    //todo spawn bonus
    //todo take bonus

    @Override
    public void destroy() {
        //todo destroy battle
        quartzService.deleteJob(this.QUARTZ_NAME, QUARTZ_GROUP);
    }

    public void activateTank(TransferProtocol net) {
        BattlePlayerController ply = players.get(net.client.userData.getLogin());
        if (ply == null)
            return;
        ply.tank.spawnState = SpawnState.STATE_ACTIVE;
        broadcast(Type.BATTLE, "activate_tank", net.client.userData.getLogin());
    }

    public void battleFinish() {
        activeBonuses.clear();
        //todo bonuses spawn stop
        battle.fund = 0;
        battle.redScore = 0;
        battle.blueScore = 0;
        for (var ply : players.values())
            if (ply != null)
                TankRespawner.cancel(ply);
    }

    public void restart() {
        if (battle.isTeam) {
            broadcast(Type.BATTLE, "change_team_scores", "RED", String.valueOf(battle.redScore));
            broadcast(Type.BATTLE, "change_team_scores", "BLUE", String.valueOf(battle.blueScore));
        }
        for (var ply : players.values()) {
            if (ply != null) {
                statisticClear(ply);
                clearEffects(ply);
                respawn(ply);
            }
        }
        long currTime = System.currentTimeMillis();
        int timeLeft = Long.valueOf((currTime + (this.battle.timeLength * 1000) - currTime) / 1000L).intValue();
        broadcast(Type.BATTLE, "battle_restart", String.valueOf(timeLeft));
        if (battle.timeLength > 0)
            startTimer();
    }

    public void respawn(BattlePlayerController ply) {
        TankRespawner.start(ply, false);
    }

    private void firstSpawn(BattlePlayerController ply) {
        TankRespawner.start(ply, true);
    }

    private void clearEffects(BattlePlayerController ply) {
        //todo
    }

    private void statisticClear(BattlePlayerController ply) {
        BattleUser user = battle.users.get(ply.net.client.userData.getLogin());
        user.kills = 0;
        user.deaths = 0;
        user.battleScore = 0;
    }

    public void addPlayer(TransferProtocol net, String team) {
        BattleUser battleUser = new BattleUser();
        battleUser.nickname = net.client.userData.getLogin();
        battleUser.battleScore = 0;
        battleUser.deaths = 0;
        battleUser.kills = 0;
        battleUser.rank = RankUtils.getRankFromScore(net.client.userData.getScore());
        battleUser.team = team;
        this.players.put(battleUser.nickname, new BattlePlayerController(net, battle));
        battle.users.put(battleUser.nickname, battleUser);
        if (battle.isTeam)
            if (team.equals("RED")) battle.redPeople++;
            else battle.bluePeople++;
        net.client.currentBattleId = battle.id;

        JSONObject json = new JSONObject();
        json.put("invisible_time", 3500);
        json.put("kick_period_ms", 125000);
        json.put("skybox_id", "skybox_default");
        json.put("sound_id", "default_ambient_sound");
        json.put("spectator", false);
        json.put("game_mode", "day");
        json.put("map_id", battle.mapId);

        UserMount mount = UserMountDAO.getMount(net.client.userData);
        assert mount != null : "mount is null";

        JSONArray resources = new JSONArray();
        resources.add(mount.getWeaponId());
        resources.add(mount.getArmorId());
        resources.add(mount.getColorId());

        json.put("resources", resources);

        net.send(Type.BATTLE, "init_turret_sfx_lighting", Global.initSfxData);
        net.send(Type.BATTLE, "init_shots_data", Global.initShotsData);
        net.send(Type.BATTLE, "init_battle_model", json.toJSONString());

        updateCountUsersInBattle();
        addPlayerToBattle(team, battleUser);
    }

    private void updateCountUsersInBattle() {
        if (!battle.isTeam)
            TransferProtocol.broadcast("lobby", Type.LOBBY, "update_count_users_in_dm_battle", battle.id, String.valueOf(battle.users.size()));
        else {
            JSONObject obj = new JSONObject();
            obj.put("battleId", battle.id);
            obj.put("redPeople", battle.redPeople);
            obj.put("bluePeople", battle.bluePeople);

            TransferProtocol.broadcast("lobby", Type.LOBBY, "update_count_users_in_team_battle", obj.toJSONString());
        }
    }

    private void addPlayerToBattle(String team, BattleUser battleUser) {
        JSONObject o = new JSONObject();
        o.put("id", battleUser.nickname);
        o.put("battleId", battle.id);
        o.put("kills", 0);
        o.put("name", battleUser.nickname);
        o.put("rank", battleUser.rank);
        o.put("type", team);
        o.put("isBot", false);

        TransferProtocol.broadcast("lobby", Type.LOBBY, "add_player_to_battle", o.toJSONString());
    }

    public void initTank(TransferProtocol net) throws CloneNotSupportedException {
        List<UserItem> userItemList = UserItemDAO.getUserDrugs(net.client.userData);
        String gui = makeGuiModel();
        String inventoryJson = makeGuiInventory(userItemList);
        String mineJson = makeMineJson();
        String battleMinesJson = makeBattleMinesJson();
        BattleUser battleUser = battle.users.get(net.client.userData.getLogin());

        net.send(Type.BATTLE, "init_gui_model", gui);
        if (!inventoryJson.isEmpty())
            net.send(Type.BATTLE, "init_inventory", inventoryJson);
        net.send(Type.BATTLE, "init_mine_model", mineJson);
        net.send(Type.BATTLE, "init_mines", battleMinesJson);

        for (var ply : players.values()) {
            if (ply.net == net) continue;
            String tankModelJson = makeTankModelJson(ply.net, false, null);
            String statisticJson = makeStatisticJson(ply.net);
            net.send(Type.BATTLE, "init_tank", tankModelJson);
            net.send(Type.BATTLE, "update_player_statistic", statisticJson);
        }

        SpawnPositionModel spawn = makeSpawnPosition(net);
        String localTankModelJson = makeTankModelJson(net, true, spawn);
        String localStatisticJson = makeStatisticJson(net);

        broadcast(Type.BATTLE, "init_tank", localTankModelJson);
        broadcast(Type.BATTLE, "update_player_statistic", localStatisticJson);
        //net.send(Type.BATTLE, "prepare_to_spawn", net.client.userData.getLogin(), spawn.x + "@" + spawn.y + "@" + spawn.z + "@" + spawn.rot);
        //broadcast(Type.BATTLE, "change_health", net.client.userData.getLogin(), "10000");
        BattlePlayerController ply = players.get(spawn.tank_id);
        firstSpawn(ply);
    }

    public SpawnPositionModel makeSpawnPosition(TransferProtocol net) {
        MapPointModel nextSpawnPos = generateNextSpawnPos(net);
        SpawnPositionModel spawn = new SpawnPositionModel();
        spawn.x = nextSpawnPos.x;
        spawn.y = nextSpawnPos.y;
        spawn.z = nextSpawnPos.z;
        spawn.rot = nextSpawnPos.a;
        return spawn;
    }

    public MapPointModel generateNextSpawnPos(TransferProtocol net) {
        MapModel map = MapLoader.maps.get(battle.mapId);
        BattleUser user = battle.users.get(net.client.userData.getLogin());
        assert map != null : "map is null";
        assert user != null : "user is null";

        String team = user.team.equalsIgnoreCase("NONE") ? "dm" : user.team.toLowerCase();
        var list = map.getSpawnPositionsForTeam(team);
        assert !list.isEmpty() : "spawn positions not found";

        int i = RandomUtils.randomIntBetween(0, list.size());
        var position = list.get(i);
        Vector3d pos = position.getPosition();
        Vector3d rot = position.getRotation();

        MapPointModel point = new MapPointModel();
        point.x = pos.getX();
        point.y = pos.getY();
        point.z = pos.getZ();
        point.a = rot.getZ();
        return point;
    }

    public String makeStatisticJson(TransferProtocol net) {
        BattleUser user = battle.users.get(net.client.userData.getLogin());
        assert user != null : "user not found";

        JSONObject json = new JSONObject();
        json.put("id", user.nickname);
        json.put("rank", user.rank);
        json.put("score", user.battleScore);
        json.put("team_type", user.team);
        json.put("kills", user.kills);
        json.put("deaths", user.deaths);
        return json.toJSONString();
    }

    private String makeTankModelJson(TransferProtocol net, boolean stateNull, SpawnPositionModel spawn) throws CloneNotSupportedException {
        BattleUser user = battle.users.get(net.client.userData.getLogin());
        UserMount mount = UserMountDAO.getMount(net.client.userData);
        assert user != null : "user not found in battle";
        assert mount != null : "user mount not found in battle";

        String turretId = mount.getWeaponId();
        String turretMod = turretId.substring(turretId.lastIndexOf('_') + 1);
        String hullId = mount.getArmorId();
        String hullMod = hullId.substring(hullId.lastIndexOf('_') + 1);

        TurretModificationModel turretModel = null;
        HullModificationModel hullModel = null;

        for ( var t : Global.turrets_config.get(turretId.substring(0, turretId.length() - 3)) ) {
            if (t.modification.equals(turretMod)) {
                turretModel = t.clone();
                break;
            }
        }

        for ( var h : Global.hulls_config.get(hullId.substring(0, hullId.length() - 3)) ) {
            if (h.modification.equals(hullMod)) {
                hullModel = h.clone();
                break;
            }
        }

        assert turretModel != null : "turretModel is null";
        assert hullModel != null : "hullModel is null";

        JSONObject json = new JSONObject();
        json.put("battleId", battle.id);
        json.put("turret_id", turretId);
        json.put("hull_id", hullId);
        json.put("colormap_id", mount.getColorId());
        json.put("nickname", user.nickname);
        if (spawn == null)
            json.put("position", "0.0@0.0@100.0@0.0");
        else
            json.put("position", spawn.x + "@" + spawn.y + "@" + spawn.z + "@" + spawn.rot);
        json.put("state", "active");
        json.put("team_type", user.team);
        json.put("tank_id", user.nickname);
        json.put("speed", hullModel.speed);
        json.put("turn_speed", hullModel.turn_speed);
        json.put("turret_turn_speed", turretModel.turretRotationSpeed);
        json.put("mass", hullModel.mass);
        json.put("power", hullModel.power);
        json.put("impact_force", turretModel.impactCoeff);
        json.put("kickback", turretModel.kickback);
        json.put("turret_rotation_accel", turretModel.turretRotationAccel);
        json.put("state_null", stateNull);
        json.put("health", 10000);
        json.put("rank", user.rank);
        json.put("icration", battle.users.size() + 1);
        if (spawn != null) {
            spawn.health = 10000;
            spawn.tank_id = user.nickname;
            spawn.incration_id = 1;
            spawn.turn_speed = hullModel.turn_speed;
            spawn.speed = hullModel.speed;
            spawn.team_type = user.team;
            spawn.turret_rotation_speed = turretModel.turretRotationSpeed;
            BattlePlayerController ply = players.get(user.nickname);
            ply.lastSpawnModel = spawn;
            ply.setupTank(spawn.tank_id, turretModel, hullModel, mount);
        }
        return json.toJSONString();
    }

    private String makeBattleMinesJson() {
        JSONObject json = new JSONObject();
        JSONArray mines = new JSONArray();
        for (var mine : battle.mines) {
            JSONObject m = new JSONObject();
            m.put("mineId", mine.mineId);
            m.put("ownerId", mine.ownerId);
            m.put("x", mine.x);
            m.put("y", mine.y);
            m.put("z", mine.z);
            mines.add(m);
        }
        json.put("mines", mines);
        return json.toJSONString();
    }

    private String makeMineJson() {
        JSONObject json = new JSONObject();
        json.put("activationTimeMsec", 2000);
        json.put("farVisibilityRadius", 15);
        json.put("impactForce", 2);
        json.put("minDistanceFromBase", 6);
        json.put("nearVisibilityRadius", 5);
        json.put("radius", 1);
        return json.toJSONString();
    }

    private String makeGuiModel() {
        long currTime = System.currentTimeMillis() / 1000;
        assert battle != null : "battle is null";

        int totalScore = 0;
        JSONArray users = new JSONArray();
        for (var user : battle.users.values()) {
            totalScore += user.battleScore;
            JSONObject u = new JSONObject();
            u.put("nickname", user.nickname);
            u.put("rank", user.rank);
            u.put("teamType", user.team);
            users.add(u);
        }

        JSONObject json = new JSONObject();
        json.put("name", battle.name);
        json.put("timeLimit", battle.timeLength);
        json.put("currTime", (battle.startTime + battle.timeLength) - currTime);
        json.put("fund", (int) (totalScore / 10));
        json.put("score_blue", battle.blueScore);
        json.put("score_red", battle.redScore);
        json.put("scoreLimit", battle.maxScore);
        json.put("team", battle.isTeam);
        json.put("users", users);

        return json.toJSONString();
    }

    private String makeGuiInventory(List<UserItem> userItemList) {
        if (userItemList == null || userItemList.isEmpty())
            return "";

        JSONObject json = new JSONObject();
        JSONArray items = new JSONArray();
        for (var i : userItemList) {
            String iid = i.getItemId();
            int slot;
            if (iid.startsWith("health")) slot = 1;
            else if (iid.startsWith("armor")) slot = 2;
            else if (iid.startsWith("double_damage")) slot = 3;
            else if (iid.startsWith("n2o")) slot = 4;
            else slot = 5;

            JSONObject item = new JSONObject();
            item.put("id", iid);
            item.put("count", i.getCount());
            item.put("itemEffectTime", 45);
            item.put("itemRestSec", 15);
            item.put("slotId", slot);
            items.add(item);
        }
        json.put("items", items);
        return json.toJSONString();
    }

    public void move(TransferProtocol net, List<Double> params, int ctrlBits, double aDouble) {
        if (params.size() != 12){
            net.vrs.registerAct("invalid_move", Grade.DETRIMENTAL);
            return;
        }

        JSONObject json = new JSONObject();
        JSONObject orient = new JSONObject();
        orient.put("x", params.get(3));
        orient.put("y", params.get(4));
        orient.put("z", params.get(5));
        JSONObject position = new JSONObject();
        position.put("x", params.get(0));
        position.put("y", params.get(1));
        position.put("z", params.get(2));
        JSONObject line = new JSONObject();
        line.put("x", params.get(6));
        line.put("y", params.get(7));
        line.put("z", params.get(8));
        JSONObject angle = new JSONObject();
        angle.put("x", params.get(9));
        angle.put("y", params.get(10));
        angle.put("z", params.get(11));

        json.put("position", position);
        json.put("orient", orient);
        json.put("line", line);
        json.put("angle", angle);
        json.put("turretDir", aDouble);
        json.put("ctrlBits", ctrlBits);
        json.put("tank_id", net.client.userData.getLogin());

        broadcast(Type.BATTLE, "move", json.toJSONString());
    }

    public void fire(TransferProtocol net, String fireJson) throws ParseException {
        BattlePlayerController ply = players.get(net.client.userData.getLogin());
        if (ply == null || ply.tank == null || ply.tank.weapon == null)
            return;

        JSONObject fireData = (JSONObject) new JSONParser().parse(fireJson);
        broadcast(Type.BATTLE, "fire", ply.tank.nickname, fireJson);

        String targetId = (String) fireData.get("victimId");
        if (targetId == null || targetId.equalsIgnoreCase("null"))
            return;
        if (!players.containsKey(targetId))
            return;

        BattlePlayerController target = players.get(targetId);
        if (ply.tank.spawnState != SpawnState.STATE_ACTIVE) {
            net.vrs.registerAct("shooting_inactive_state", Grade.DANGEROUS);
            return;
        }
        if (target.tank.spawnState != SpawnState.STATE_ACTIVE)
            return;
        if (battle.isTeam && !battle.ff) {
            BattleUser uTarget = battle.users.get(target.tank.nickname);
            BattleUser uAttacker = battle.users.get(ply.tank.nickname);
            assert uTarget != null && uAttacker != null : "Users not found in team";
            if (uTarget.team.equals(uAttacker.team))
                return;
        }

        killService.hitTank(ply, target);
    }

    public void updateFund() {
        broadcast(Type.BATTLE, "change_fund", String.valueOf(battle.fund));
    }

    public void suicide(TransferProtocol net) {
        BattlePlayerController ply = players.get(net.client.userData.getLogin());
        if (ply == null || ply.tank == null || ply.tank.spawnState != SpawnState.STATE_ACTIVE)
            return;

        killService.killTank(ply, ply);
    }

    public void removeUser(TransferProtocol net) throws RuntimeException {
        BattlePlayerController ply = players.remove(net.client.userData.getLogin());
        if (ply == null)
            throw new RuntimeException("user not found");

        //todo clear mines
        BattleUser user =  battle.users.remove(ply.tank.nickname);
        if (battle.isTeam) {
            if (user.team.equalsIgnoreCase("RED"))
                battle.redPeople--;
            else
                battle.bluePeople--;
            //todo drop flag
        }
        broadcast(Type.BATTLE, "remove_user", ply.tank.nickname);

        removePlayerFromBattle(ply);
        updateCountUsersInBattle();

        ply.net.client.currentBattleId = null;
        ply.tank = null;
    }

    private void removePlayerFromBattle(BattlePlayerController ply) {
        JSONObject obj = new JSONObject();
        obj.put("battleId", battle.id);
        obj.put("id", ply.tank.nickname);
        TransferProtocol.broadcast("lobby", Type.LOBBY, "remove_player_from_battle", obj.toJSONString());
    }
}
