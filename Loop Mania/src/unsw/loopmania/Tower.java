package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tower extends BuildingProperty {
    private final int shootRadius = 500; // TODO: this value may be changed later
    private final int damage = 5000;
    public Tower(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }


    public void addEnemyNearBy(LoopManiaWorld l) {
        // loop through all enemies
        // calculate the distance between tower and enemy
        // if it is in radius
        // add to the list
        super.getEnemies().clear();
        for (EnemyProperty enemy : l.getEnemy()) {
            double distance = getDistance(enemy.getX(), enemy.getY());
            if (distance <= this.shootRadius && enemy.getInBattle()) {
                super.addEnemy(enemy);
            } else {
                for (EnemyProperty e : l.getEnemy()) {
                    if (e.equals(enemy)) super.removeEnemy(e);
                }
            }
        }
        return;
    }

    public void decreaseHp() {
        for (EnemyProperty enemy : super.getEnemies()) {
            enemy.setHP(enemy.getHP() - this.damage);
        }
    }

    public void attack(LoopManiaWorld l) {
        addEnemyNearBy(l);
        decreaseHp();
    }


    public int getShootRadius() {
        return this.shootRadius;
    }

    /*public double getDistance(int destX, int destY) {
        int startX = super.getX();
        int startY = super.getY();
        return Math.sqrt(Math.pow(startX - destX, 2) - Math.pow(startY - destY, 2));
    }*/



    @Override
    public void spawnEnemy(LoopManiaWorld l, List<EnemyProperty> spawningEnemies) {
        return;
    }

    @Override
    public void characterStepOn(LoopManiaWorld l) {
        return;
    }

    @Override
    public void enemyStepOn(LoopManiaWorld l, List<BuildingProperty> toRemove) {
        attack(l);
    }


    @Override
    public ImageView onLoadBuilding() {
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/tower.png")).toURI().toString()));
    }
}
