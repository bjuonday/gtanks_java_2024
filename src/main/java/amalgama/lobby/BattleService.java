package amalgama.lobby;

import amalgama.battle.TankKillService;
import amalgama.network.Type;
import amalgama.network.netty.TransferProtocol;
import amalgama.system.quartz.IQuartzService;
import amalgama.system.quartz.QuartzService;
import amalgama.system.quartz.TimeUnit;

import javax.security.auth.Destroyable;
import java.util.HashMap;

public class BattleService implements Destroyable {
    public static final String QUARTZ_GROUP = BattleService.class.getName();
    public final String QUARTZ_NAME;
    public final String QUARTZ_RESTART_NAME;
    private TankKillService killService = new TankKillService(this);
    private long endTimePoint;
    private HashMap<String, TransferProtocol> players = new HashMap<>();
    public final Battle battle;

    private IQuartzService quartzService = QuartzService.getInstance();

    public BattleService(Battle battle) {
        this.battle = battle;
        QUARTZ_NAME = "BattleTimer/" + hashCode() + "/" + battle.id;
        QUARTZ_RESTART_NAME = "BattleRestarter/" + hashCode() + "/" + battle.id;
        if (battle.timeLength > 0)
            startTimer(((Long) battle.timeLength).intValue());
    }

    private void startTimer(int seconds) {
        System.out.println("Timer start " + seconds);
        this.endTimePoint = System.currentTimeMillis() + seconds * 1000L;
        this.quartzService.addJob(QUARTZ_NAME, QUARTZ_GROUP, e -> {
            System.out.println("End battle");
            battle.startTime = System.currentTimeMillis() / 1000L;
            killService.restartBattle(true);
        }, TimeUnit.SECONDS, battle.timeLength);
    }

    public void broadcast(Type type, String... args) {
        TransferProtocol.broadcast(this.battle.id, type, args);
    }

    public void kickPlayer(String nickname) {
        TransferProtocol ply = players.get(nickname);
        ply.close();
    }

    //todo activate tank
    //todo init tank
    //todo random spawn pos
    //todo remove user
    //todo add user
    //todo spawn
    //todo move
    //todo respawn
    //todo spawn bonus
    //todo take bonus
    //todo fire
    //todo battle finish
    //todo battle restart

    @Override
    public void destroy() {
        //todo destroy battle
        quartzService.deleteJob(this.QUARTZ_NAME, QUARTZ_GROUP);
    }
}
