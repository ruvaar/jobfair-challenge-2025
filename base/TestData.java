package base;

public class TestData {

    private final long start;
    private final long goal;
    private final boolean reachable;

    public TestData(long start, long goal, boolean reachable) {
        this.start = start;
        this.goal = goal;
        this.reachable = reachable;
    }

    public long getStart() {
        return start;
    }

    public long getGoal() {
        return goal;
    }

    public boolean isReachable() {
        return reachable;
    }
}
