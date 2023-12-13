package amalgama.battle;

import amalgama.lobby.BattleService;
import amalgama.lobby.BattleUser;
import amalgama.network.Type;
import amalgama.network.managers.LobbyManager;
import amalgama.system.quartz.IQuartzService;
import amalgama.system.quartz.QuartzService;
import amalgama.system.quartz.TimeUnit;
import amalgama.utils.RankUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.security.auth.Destroyable;

public class TankKillService implements Destroyable {
    private static final String QUARTZ_GROUP = TankKillService.class.getName();
    private static final int fundIncrement = 2;
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
            int prize = ply.battleScore / 10;
            JSONObject user = new JSONObject();
            user.put("id", ply.nickname);
            user.put("team_type", ply.team);
            user.put("rank", ply.rank);
            user.put("kills", ply.kills);
            user.put("deaths", ply.deaths);
            user.put("score", ply.battleScore);
            user.put("prize", prize);
            LobbyManager.addCry(bfService.players.get(ply.nickname).net, prize);
            users.add(user);
        }
        json.put("users", users);
        json.put("time_to_restart", 10000);
        bfService.broadcast(Type.BATTLE, "battle_finish", json.toJSONString());
    }

    @Override
    public void destroy() {
        quartzService.deleteJob(this.QUARTZ_NAME, QUARTZ_GROUP);
    }

    public void changeHealth(BattlePlayerController ply, int hp) {
        ply.tank.health = hp;
        bfService.broadcast(Type.BATTLE, "change_health", ply.net.client.userData.getLogin(), String.valueOf(hp));
    }

    public synchronized void killTank(BattlePlayerController ply, BattlePlayerController killer) {
        BattleUser uTarget = bfService.battle.users.get(ply.tank.nickname);
        assert uTarget != null : "target not found in battle";

        if (ply == killer) {
            uTarget.deaths++;
            if (uTarget.battleScore >= 10)
                uTarget.battleScore -= 10;
            bfService.broadcast(Type.BATTLE, "kill_tank", uTarget.nickname, "suicide", uTarget.nickname);
            ply.updateStats();
            ply.tank.spawnState = SpawnState.STATE_DEAD;
            bfService.respawn(ply);
        }
        else {
            BattleUser uAttacker = bfService.battle.users.get(killer.tank.nickname);
            assert uAttacker != null : "attacker not found in battle";

            uAttacker.kills++;
            uAttacker.battleScore += 10;
            LobbyManager.addScore(killer.net, (int) (10 * killer.net.client.scoreBonusPercent));
            uAttacker.rank = RankUtils.getRankFromScore(killer.net.client.userData.getScore());
            if (bfService.battle.isTeam) {
                if (bfService.battle.type.equalsIgnoreCase("tdm")) {
                    if (uAttacker.team.equals("RED")) {
                        bfService.battle.redScore++;
                        bfService.broadcast(Type.BATTLE, "change_team_scores", "RED", String.valueOf(bfService.battle.redScore));
                    }
                    else {
                        bfService.battle.blueScore++;
                        bfService.broadcast(Type.BATTLE, "change_team_scores", "BLUE", String.valueOf(bfService.battle.blueScore));
                    }
                }
            }
            uTarget.deaths++;
            ply.updateStats();
            killer.updateStats();
            bfService.battle.fund += fundIncrement;
            bfService.updateFund();
            bfService.broadcast(Type.BATTLE, "kill_tank", uTarget.nickname, "killed", uAttacker.nickname);
            ply.tank.spawnState = SpawnState.STATE_DEAD;
            bfService.respawn(ply);

            if (bfService.battle.type.equalsIgnoreCase("dm"))
                if (uAttacker.kills >= bfService.battle.maxScore && bfService.battle.maxScore > 0)
                    restartBattle(false);
            if (bfService.battle.type.equalsIgnoreCase("tdm"))
                if ((bfService.battle.redScore >= bfService.battle.maxScore || bfService.battle.blueScore >= bfService.battle.maxScore) && bfService.battle.maxScore > 0)
                    restartBattle(false);
        }
    }

    public synchronized void hitTank(BattlePlayerController attacker, BattlePlayerController target) {
        int damage = attacker.tank.weapon.calculateDamage(target.tank);
        giveDamage(target, damage);
        if (target.tank.health <= 0)
            killTank(target, attacker);
    }

    private synchronized void giveDamage(BattlePlayerController target, int damage) {
        target.tank.health -= damage;
        changeHealth(target, target.tank.health);
    }
}
