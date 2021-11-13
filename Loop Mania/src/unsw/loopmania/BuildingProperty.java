package unsw.loopmania;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;

public abstract class BuildingProperty extends Building{

    public BuildingProperty(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        //TODO Auto-generated constructor stub
    }
    abstract public void spawnEnemy(LoopManiaWorld l, List<EnemyProperty> spawningEnemies);
    abstract public void characterStepOn(LoopManiaWorld l);
    abstract public void enemyStepOn(LoopManiaWorld l, List<BuildingProperty> toRemove);
    abstract public ImageView onLoadBuilding();
    
}
