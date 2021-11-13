package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * represents an equipped or unequipped HealthPotion in the backend world
 */
public class HealthPotion extends ItemProperty {
    public static IntegerProperty price = new SimpleIntegerProperty(2000);
    private final int health = 200;
    public HealthPotion(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.HEALTHPOTION);
    }

    public int getHealth() {
        return health;
    }
    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        return;

    }
    @Override
    public void characterStepOn(LoopManiaWorld l, List<ItemProperty> toRemoveGold, List<ItemProperty> toRemoveHealthPotion) {
        if (l.getCharacter().getX() == getX() && l.getCharacter().getY() == getY()) {
            toRemoveHealthPotion.add(this);
        }

    }

    @Override
    public ImageView onLoadItems() {
        return new ImageView(new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString()));
    }
    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return false;
    }
}