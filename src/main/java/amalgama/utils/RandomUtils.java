package amalgama.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    public static int randomIntBetween(int min, int max) {
        Random rnd = new Random();
        return rnd.nextInt(min, max);
    }

    public static <T> T randomFromList(List<T> list) {
        if (list.isEmpty())
            return null;

        Random rnd = new Random();
        return list.get(rnd.nextInt(0, list.size()));
    }
}
