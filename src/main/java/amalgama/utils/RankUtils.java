package amalgama.utils;

import amalgama.Global;
import amalgama.models.RankModel;

public class RankUtils {
    public static int getRankFromScore(int score) {
        for (int i = Global.ranks.size() - 1; i > -1; i--) {
            RankModel rank = Global.ranks.get(i);
            if (score >= rank.score)
                return i + 1;
        }
        return 0;
    }

    public static int getScoreFromRank(int rank) {
        if (rank >= Global.ranks.size())
            return 0;

        return Global.ranks.get(rank).score;
    }
}
