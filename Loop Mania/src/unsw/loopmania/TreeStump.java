package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TreeStump extends RareItem{
    private final int defense = 20;

    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.SHIELD);
        
    }
    public int getDefense() {
        return this.defense;
    }

    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        // TODO Auto-generated method stub
        if (mode == ModeType.CONFUSING) {
            int damage = 250;
            if (e.isBoss()) {
                c.setDamage(c.getDamage() + 3 * damage);
            } else {
                c.setDamage(c.getDamage());
            }
        }
        if (e.isBoss()) {
            e.setDamage(e.getDamage() - defense * 3);
        } else {
            e.setDamage(e.getDamage() - defense);
        }

        if (e.getDamage() < 0) e.setDamage(0);
    }


    @Override
    public ImageView onLoadItems() {
        return new ImageView(new Image((new File("src/images/tree_stump.png")).toURI().toString()));
    }
    @Override
    public void characterStepOn(LoopManiaWorld l, List<ItemProperty> toRemoveGold,
            List<ItemProperty> toRemoveHealthPotion) {
        // TODO Auto-generated method stub

    }
    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return false;
    }
}
