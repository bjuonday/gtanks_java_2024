package amalgama.network.secure;

public class Limits {
    //maximum number of active battles
    public static final int MAX_BATTLES = 10;

    //The maximum number of battles that can be created in an interval
    public static final int MAX_BATTLES_IN_INTERVAL = 3;

    //The interval for creating battles (in seconds)
    public static final int BATTLES_INTERVAL = 300;

    private Limits() {}
}
