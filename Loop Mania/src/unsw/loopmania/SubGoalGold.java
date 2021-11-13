package unsw.loopmania;


public class SubGoalGold implements GoalCompsite{
    private int value;
    private LoopManiaWorld world;

    public SubGoalGold(LoopManiaWorld world, int value) {
        this.world = world;
        this.value = value;
    }

    @Override
    public boolean goalReached() {
        if (world.getGold().get() >= value) return true;
        return false;
    }
}
