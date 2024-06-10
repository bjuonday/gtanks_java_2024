package amalgama.system.quartz;

public enum TimeUnit {
    MILLISECONDS(1),
    SECONDS(1000),
    MINUTES(60000),
    HOURS(3600000);

    private final int mn;

    TimeUnit(int mn) {
        this.mn = mn;
    }

    public long time(long t) {
        return this.mn * t;
    }
}
