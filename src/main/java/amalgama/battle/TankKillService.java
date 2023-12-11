package amalgama.battle;

import amalgama.lobby.BattleService;
import amalgama.network.Type;
import amalgama.system.quartz.IQuartzService;
import amalgama.system.quartz.QuartzService;
import amalgama.system.quartz.TimeUnit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

public class TankKillService implements Destroyable {
    private static final String QUARTZ_GROUP = TankKillService.class.getName();
    private final String QUARTZ_NAME;
    private static final long DELAY_BEFORE_RESTART = 10000L;
    private final BattleService bfService;
    private IQuartzService quartzService = QuartzService.getInstance();

    public TankKillService(BattleService bfService) {
        this.bfService = bfService;
        QUARTZ_NAME = "TankKillService/" + hashCode() + "/" + this.bfService.battle.id;
    }

    public void restartBattle(boolean byTimeout) {
        if (!byTimeout && bfService.battle.timeLength > 0)
            this.quartzService.deleteJob(bfService.QUARTZ_NAME, BattleService.QUARTZ_GROUP);
        finishAndPrizes();
        bfService.battleFinish();
        quartzService.addJob(this.QUARTZ_NAME, QUARTZ_GROUP, e -> this.bfService.restart(), TimeUnit.MILLISECONDS, DELAY_BEFORE_RESTART);
    }

    private void finishAndPrizes() {
        if (bfService == null || bfService.players.isEmpty())
            return;
        JSONObject json = new JSONObject();
        JSONArray users = new JSONArray();
        for (var ply : bfService.battle.users.values()) {
            JSONObject user = new JSONObject();
            user.put("id", ply.nickname);
            user.put("team_type", ply.team);
            user.put("rank", ply.rank);
            user.put("kills", ply.kills);
            user.put("deaths", ply.deaths);
            user.put("score", ply.battleScore);
            user.put("prize", (int) (ply.battleScore / 10));
            users.add(user);
        }
        json.put("users", users);
        json.put("time_to_restart", 10000);
        bfService.broadcast(Type.BATTLE, "battle_finish", json.toJSONString());
    }

    //todo synch kill tank
    //todo change hp
    //todo heal hp
    //todo synch damage tank


    @Override
    public void destroy() throws DestroyFailedException {
        quartzService.deleteJob(this.QUARTZ_NAME, QUARTZ_GROUP);
    }

    public void changeHealth(BattlePlayerController ply, int hp) {
        bfService.broadcast(Type.BATTLE, "change_health", ply.net.client.userData.getLogin(), String.valueOf(hp));
    }
}
