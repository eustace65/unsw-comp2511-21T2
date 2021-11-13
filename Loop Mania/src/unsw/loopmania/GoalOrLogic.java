package unsw.loopmania;

public class GoalOrLogic extends GoalLogic{
    public boolean goalReached() {
        boolean first = totalGoal.get(0).goalReached();
        for(GoalCompsite each: totalGoal) {
            first |= each.goalReached();
        }
        return first;
    }
}
