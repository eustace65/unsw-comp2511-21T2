package unsw.loopmania;

public class SubGoalCycle implements GoalCompsite{
    private int value;
    private LoopManiaWorld world;

    public SubGoalCycle(LoopManiaWorld world, int value) {
        this.value = value;
        this.world = world;
    }

    @Override
    public boolean goalReached() {
        if(world.getCycle() >= value) return true;
        return false;
    }
}
