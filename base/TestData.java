package base;

public class TestData {

    private final long start;
    private final long goal;
    private final boolean reachable;
    private final String id;

    public TestData(long start, long goal, boolean reachable, String id) {
        this.start = start;
        this.goal = goal;
        this.reachable = reachable;
        this.id = id;
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

    public String getId() {
        return id;
    }

}
