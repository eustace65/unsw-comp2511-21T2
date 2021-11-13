package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Village extends BuildingProperty{
    private final int hpGain = 100; // TODO: the value might be changed
    public Village(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }


    public int getHpGain() {
        return this.hpGain;
    }

    public void increaseHp(Character c) {

        c.setHp(c.getHp().get() + this.getHpGain());
    }

    @Override
    public void spawnEnemy(LoopManiaWorld l, List<EnemyProperty> spawningEnemies) {
        return;
    }

    @Override
    public void characterStepOn(LoopManiaWorld l) {
        increaseHp(l.getCharacter());
    }

    @Override
    public void enemyStepOn(LoopManiaWorld l, List<BuildingProperty> toRemove) {
        return;
    }


    @Override
    public ImageView onLoadBuilding() {
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/village.png")).toURI().toString()));
    }
}
