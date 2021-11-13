package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * represents an equipped or unequipped Stake in the backend world
 */
public class Stake extends ItemProperty {
    private final int damage = 150;
    public static IntegerProperty price = new SimpleIntegerProperty(1500);
    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.STAKE);
    }

    public int getDamage(EnemyProperty enemy) {
        if (enemy.getType().equals("Vampire")) {
            return 2 * damage;
        } else {
            return damage;
        }

    }

    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        // TODO Auto-generated method stub
        if (e.getType().equals("Vampire")) {
            c.setDamage(c.getDamage() + 2 * damage);
        } else {
            c.setDamage(c.getDamage() + damage);
        }

    }

    @Override
    public void characterStepOn(LoopManiaWorld l, List<ItemProperty> toRemoveGold,
            List<ItemProperty> toRemoveHealthPotion) {
        // TODO Auto-generated method stub

    }

    @Override
    public ImageView onLoadItems() {
        return new ImageView(new Image((new File("src/images/stake.png")).toURI().toString()));
    }

    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return true;
    }
}