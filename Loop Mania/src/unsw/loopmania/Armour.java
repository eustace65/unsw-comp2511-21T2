package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * represents an equipped or unequipped Armour in the backend world
 */
public class Armour extends ItemProperty {
    public static IntegerProperty price = new SimpleIntegerProperty(1000);
    // TODO = add more weapon/item types
    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.ARMOUR);
    }
    public int getDefense(EnemyProperty enemy) {
        return enemy.getDamage()/2;
    }

    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        e.setDamage(e.getDamage()/ 2);

    }
    @Override
    public void characterStepOn(LoopManiaWorld l, List<ItemProperty> toRemoveGold,
            List<ItemProperty> toRemoveHealthPotion) {
        // TODO Auto-generated method stub
        return;
    }

    @Override
    public ImageView onLoadItems() {
        return new ImageView(new Image((new File("src/images/armour.png")).toURI().toString()));
    }
    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return true;
    }



}