package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Sword extends ItemProperty {

    private final int damage = 200;
    public static IntegerProperty price = new SimpleIntegerProperty(1000);

    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.SWORD);
    }

    public int getDamage() {
        return this.damage;
    }



    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        // TODO Auto-generated method stub
        c.setDamage(c.getDamage() + damage);

    }



    @Override
    public void characterStepOn(LoopManiaWorld l, List<ItemProperty> toRemoveGold,
            List<ItemProperty> toRemoveHealthPotion) {
        // TODO Auto-generated method stub

    }

    @Override
    public ImageView onLoadItems() {
        return new ImageView(new Image((new File("src/images/basic_sword.png")).toURI().toString()));
    }

    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return true;
    }
}
