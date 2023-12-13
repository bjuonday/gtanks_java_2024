package amalgama.battle;

import amalgama.lobby.BattleService;
import amalgama.models.MapPointModel;
import amalgama.utils.RandomUtils;

import java.util.Random;

public class BonusAllocator implements Runnable {
    private static final long DELAY = 10000L;
    private final BattleService bfService;
    private int nextGoldFund;
    private int lastFund;
    private boolean isNull;
    private int inc;

    public BonusAllocator(BattleService battleService) {
        bfService = battleService;
        nextGoldFund = getNextGoldFund();
        isNull = bfService == null;
    }

    private static int getNextGoldFund() {
        return RandomUtils.randomIntBetween(200, 300);
    }

    @Override
    public void run() {
        if (bfService.map.crystalRegions.isEmpty() && bfService.map.goldRegions.isEmpty())
            isNull = true;
        while (!isNull) {
            try {
                Thread.sleep(DELAY);
                if (bfService.players.isEmpty())
                    break;
                allocBonus(-1, -1);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void allocBonus(int count, int id) {
        if (bfService.players.isEmpty())
            return;
        if (id < 1)
            id = RandomUtils.randomIntBetween(0, 5);
        if (count < 1)
            count = RandomUtils.randomIntBetween(0, 4);
        BonusType type = null;
        switch (id) {
            case 0, 1 -> type = BonusType.NITRO;
            case 2 -> type = BonusType.ARMOR;
            case 3 -> type = BonusType.HEALTH;
            case 4 -> type = BonusType.DAMAGE;
            case 5 -> type = BonusType.CRYSTAL;
        }
        for (int i = 0; i < count; i++)
            spawnBonus(type);
    }


    private void spawnBonus(BonusType type) {
        if (type == null)
            return;
        int i = 0;
        BonusRegion reg = null;
        Bonus bonus = null;
        switch (type) {
            case HEALTH -> {
                if (bfService.map.hpRegions.isEmpty())
                    break;
                i = RandomUtils.randomIntBetween(0, bfService.map.hpRegions.size());
                reg = bfService.map.hpRegions.get(i);
                bonus = new Bonus(getRandSpawnPos(reg), type);
                bfService.spawnBonus(bonus, inc, 30);
            }
            case ARMOR -> {
                if (bfService.map.daRegions.isEmpty())
                    break;
                i = RandomUtils.randomIntBetween(0, bfService.map.daRegions.size());
                reg = bfService.map.daRegions.get(i);
                bonus = new Bonus(getRandSpawnPos(reg), type);
                bfService.spawnBonus(bonus, inc, 30);
            }
            case DAMAGE -> {
                if (bfService.map.ddRegions.isEmpty())
                    break;
                i = RandomUtils.randomIntBetween(0, bfService.map.ddRegions.size());
                reg = bfService.map.ddRegions.get(i);
                bonus = new Bonus(getRandSpawnPos(reg), type);
                bfService.spawnBonus(bonus, inc, 30);
            }
            case NITRO -> {
                if (bfService.map.nitroRegions.isEmpty())
                    break;
                i = RandomUtils.randomIntBetween(0, bfService.map.nitroRegions.size());
                reg = bfService.map.nitroRegions.get(i);
                bonus = new Bonus(getRandSpawnPos(reg), type);
                bfService.spawnBonus(bonus, inc, 30);
            }
            case CRYSTAL -> {
                if (bfService.map.crystalRegions.isEmpty())
                    break;
                i = RandomUtils.randomIntBetween(0, bfService.map.crystalRegions.size());
                reg = bfService.map.crystalRegions.get(i);
                bonus = new Bonus(getRandSpawnPos(reg), type);
                bfService.spawnBonus(bonus, inc, 300);
            }
            case GOLD_100, GOLD_500 -> {
                if (bfService.map.goldRegions.isEmpty())
                    break;
                i = RandomUtils.randomIntBetween(0, bfService.map.goldRegions.size());
                reg = bfService.map.goldRegions.get(i);
                bonus = new Bonus(getRandSpawnPos(reg), type);
                bfService.spawnBonus(bonus, inc, 300);
            }
        }
        inc++;
        //System.out.println("[BONUS] Index = " + i);
        //System.out.println("[BONUS] Region = " + reg.toString());
        //System.out.println("[BONUS] Position = " + bonus.position.toString());
    }

    private MapPointModel getRandSpawnPos(BonusRegion reg) {
        Random R = new Random();
        MapPointModel p = new MapPointModel();
        p.x = R.nextDouble(reg.min.x, reg.max.x);
        p.y = R.nextDouble(reg.min.y, reg.max.y);
        p.z = R.nextDouble(reg.min.z, reg.max.z);
        return p;
    }

    public void battleFinished() {
        nextGoldFund = getNextGoldFund();
        lastFund = 0;
    }

    public void updateFund() {
        int fund = bfService.battle.fund;
        int delta = Math.abs(fund - lastFund);
        if (delta > 5) {
            allocBonus(delta, 5);
            lastFund = fund;
        }
    }
}
