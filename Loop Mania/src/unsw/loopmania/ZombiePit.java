package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ZombiePit extends BuildingProperty {
    public ZombiePit(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }


    public boolean checkPathCycle(LoopManiaWorld l) {
        if (this.getPathCycle() % l.getOrderedPath().size() == 0) {
            return true;
        }
        return false;
    }

    public Zombie spawnZombie(LoopManiaWorld l) {
        // create a new class of zombie and put it in the global data
        PathPosition position = getNearestPath(l);
        Zombie newZom = new Zombie(position);
        l.getEnemy().add(newZom);
        return newZom;
    }

    @Override
    public void spawnEnemy(LoopManiaWorld l, List<EnemyProperty> spawningEnemies) {
        if (checkPathCycle(l)) {
            Zombie newZom = spawnZombie(l);
            spawningEnemies.add(newZom);
        }
    }

    @Override
    public void characterStepOn(LoopManiaWorld l) {
        return;
    }

    @Override
    public void enemyStepOn(LoopManiaWorld l, List<BuildingProperty> toRemove) {
        return;
    }


    @Override
    public ImageView onLoadBuilding() {
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/zombie_pit.png")).toURI().toString()));
    }


}
