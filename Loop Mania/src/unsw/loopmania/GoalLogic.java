package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

public abstract class GoalLogic implements GoalCompsite{
    List<GoalCompsite> totalGoal = new ArrayList<>();
    
    public abstract boolean goalReached();

    public void addGoal(GoalCompsite goal) {
        this.totalGoal.add(goal);
    }

    public void removeGoal(GoalCompsite goal) {
        this.totalGoal.remove(goal);
    }
}
