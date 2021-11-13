package unsw.loopmania;

public class SubGoalExp implements GoalCompsite{
    private int value;
    private LoopManiaWorld world;

    public SubGoalExp(LoopManiaWorld world, int value) {
        this.world = world;
        this.value = value;
    }

    @Override
    public boolean goalReached() {
        if(world.getExpInt().get() >= value) return true;
        return false;
    }
}
