package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Campfire extends BuildingProperty {
    private final int campRadius = 3; // TODO; the value may change later;
    public Campfire(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public int getcampRadius() {
        return this.campRadius;
    }


    public void doubleDamage(Character c) {
        // get the character's damage and double it
        // deduct corresponding points from the enemy
        for (EnemyProperty enemies : super.getEnemies()) {
            enemies.setHP(enemies.getHP() - 2 * c.getDamage());
        }
    }

    @Override
    public void spawnEnemy(LoopManiaWorld l, List<EnemyProperty> spawningEnemies) {
        return;
    }

    @Override
    public void characterStepOn(LoopManiaWorld l) {
        int destX = getX();
        int destY = getY();
        int srcX = l.getCharacter().getPathPosition().getX().get();
        int srcY = l.getCharacter().getPathPosition().getY().get();
        int distance = (int)Math.sqrt(Math.pow(destX - srcX,2) + Math.pow(destY - srcY , 2));
            if (distance <= getcampRadius()) {
                l.getCharacter().setDamage(l.getCharacter().getDamage() * 2);
            } else {
                l.getCharacter().setDamage(100);
            }
    }

    @Override
    public void enemyStepOn(LoopManiaWorld l, List<BuildingProperty> toRemove) {
        return;
    }



    @Override
    public ImageView onLoadBuilding() {
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/campfire.png")).toURI().toString()));
    }


}
