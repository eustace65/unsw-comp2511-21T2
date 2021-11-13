package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * a basic form of building in the world
 */
public class VampireCastleBuilding extends BuildingProperty {
    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }


    public boolean checkPathCycle(LoopManiaWorld l) {
        if (this.getPathCycle() % (5 * l.getOrderedPath().size()) == 0) {
            return true;
        }
        return false;
    }

    public Vampire spawnVampire(LoopManiaWorld l) {
        // create a new class of vampire and put it in the global data
        PathPosition position = getNearestPath(l);
        Vampire newVam = new Vampire(position);
        l.getEnemy().add(newVam);
        return newVam;
    }

    @Override
    public void spawnEnemy(LoopManiaWorld l, List<EnemyProperty> spawningEnemies) {
        if (checkPathCycle(l)) {
            Vampire newVam = spawnVampire(l);
            spawningEnemies.add(newVam);
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
        return new ImageView(new Image((new File("src/images/vampire_castle_building_purple_background.png")).toURI().toString()));
    }
}
