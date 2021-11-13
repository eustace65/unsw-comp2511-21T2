package unsw.loopmania;


import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;

abstract public class Building extends StaticEntity {
    private int pathCycle = 0;
    private ArrayList<EnemyProperty> enemies = new ArrayList<EnemyProperty>();

    public Building (SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public int getPathCycle() {
        return this.pathCycle;
    }

    public ArrayList<EnemyProperty> getEnemies() {
        return this.enemies;
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    public void setPathCycle(int newPathCycle) {
        this.pathCycle = newPathCycle;
    }


    //public abstract boolean checkPathCycle();

    public void addEnemy(EnemyProperty stepOn) {
        this.enemies.add(stepOn);
    }

    public void removeEnemy(EnemyProperty stepOn) {
        this.enemies.remove(stepOn);
    }


    public void addEnemiesWorld(LoopManiaWorld l) {
        this.getEnemies().clear();
        for (EnemyProperty enemy : l.getEnemy()) {
            if (enemy.getPathPosition().getX().get() == super.getX() && enemy.getPathPosition().getY().get() == super.getY()) {
                addEnemy(enemy);
            }
        }
    }

    public PathPosition getNearestPath(LoopManiaWorld l) {
        PathPosition position = null;

        int i = 0;
        for (Pair<Integer, Integer> p : l.getOrderedPath()) {
            if (p.equals(new Pair<>(super.getX(), super.getY())) ||
            p.equals(new Pair<>(super.getX() - 1, super.getY())) ||
            p.equals(new Pair<>(super.getX() + 1, super.getY())) ||
            p.equals(new Pair<>(super.getX(), super.getY() + 1)) ||
            p.equals(new Pair<>(super.getX(), super.getY() - 1)) ||
            p.equals(new Pair<>(super.getX() - 1, super.getY() - 1)) ||
            p.equals(new Pair<>(super.getX() + 1, super.getY() + 1)) ||
            p.equals(new Pair<>(super.getX() - 1, super.getY() + 1)) ||
            p.equals(new Pair<>(super.getX() + 1, super.getY() - 1))) {
                
                position = new PathPosition(i, l.getOrderedPath());
                
            }

            
            i++;
        }
        return position; // non-pathtile if return value is null
    }

    public double getDistance(int destX, int destY) {
        int startX = super.getX();
        int startY = super.getY();
        return Math.sqrt(Math.pow(startX - destX, 2) - Math.pow(startY - destY, 2));
    }
}
