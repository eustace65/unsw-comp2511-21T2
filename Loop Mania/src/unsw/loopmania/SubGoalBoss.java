package unsw.loopmania;

public class SubGoalBoss implements GoalCompsite{
    private LoopManiaWorld world;

    public SubGoalBoss(LoopManiaWorld world) {
        this.world = world;
    }

    @Override
    public boolean goalReached() {
        if(world.isBossAlive()) return false;
        return true;
    }
}
