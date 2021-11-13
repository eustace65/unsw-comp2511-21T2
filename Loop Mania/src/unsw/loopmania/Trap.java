package unsw.loopmania;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Trap extends BuildingProperty {
    private final int damage = 4; // TODO: this value can be changed later
    public Trap (SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }


    public void destroyTrap(LoopManiaWorld l) {
        //fetch from the global buildings and remove this object
        for (BuildingProperty b : l.getBuildings()) {
            if (b.equals(this)) {
                l.getBuildings().remove(b);
                b.destroy();
            }
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public void exertDamage(LoopManiaWorld l, List<BuildingProperty> toRemove) {
        // TODO: Deduct corresponding hp from the enemy
        super.addEnemiesWorld(l);
        for (EnemyProperty enemy : super.getEnemies()) {
            enemy.setHP(enemy.getHP() - this.damage);
        }
        if (super.getEnemies().size() > 0) {
            toRemove.add(this);
        }
    }


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
        exertDamage(l, toRemove);

    }


    @Override
    public ImageView onLoadBuilding() {
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/trap.png")).toURI().toString()));
    }
}
