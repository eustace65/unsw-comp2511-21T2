package unsw.loopmania;

public class GoalAndLogic extends GoalLogic{
    public boolean goalReached() {
        boolean first = totalGoal.get(0).goalReached();
        for(GoalCompsite each: totalGoal) {
            first &= each.goalReached();
        }
        return first;
    }
}
