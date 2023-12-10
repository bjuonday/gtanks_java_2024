package amalgama.battle;

import amalgama.lobby.BattleService;
import amalgama.network.Type;
import amalgama.system.quartz.IQuartzService;
import amalgama.system.quartz.QuartzService;
import amalgama.system.quartz.TimeUnit;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

public class TankKillService implements Destroyable {
    private static final String QUARTZ_GROUP = TankKillService.class.getName();
    private final String QUARTZ_NAME;
    private final BattleService bfService;
    private IQuartzService quartzService = QuartzService.getInstance();

    public TankKillService(BattleService bfService) {
        this.bfService = bfService;
        QUARTZ_NAME = "TankKillService/" + hashCode() + "/" + this.bfService.battle.id;
    }

    public void restartBattle(boolean byTimeout) {
        if (!byTimeout && bfService.battle.timeLength > 0)
            this.quartzService.deleteJob(bfService.QUARTZ_NAME, BattleService.QUARTZ_GROUP);
        //calculatePrizes();
        //bfService.battleFinish();
        bfService.broadcast(Type.BATTLE, "battle_finish"); //todo battle finish
        //quartzService.addJob(this.QUARTZ_NAME, QUARTZ_GROUP, e -> this.bfService.restart(), TimeUnit.MILLISECONDS, 10000L);
    }

    //todo set battle fund
    //todo get battle fund
    //todo add fund
    //todo calc prizes
    //todo synch kill tank
    //todo change hp
    //todo heal hp
    //todo synch damage tank


    @Override
    public void destroy() throws DestroyFailedException {
        quartzService.deleteJob(this.QUARTZ_NAME, QUARTZ_GROUP);
    }
}
