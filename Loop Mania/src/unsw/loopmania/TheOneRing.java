package unsw.loopmania;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;

/**
 * represents an equipped or unequipped TheOneRing in the backend world
 */
public class TheOneRing extends RareItem {
    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.OTHER);
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
    }

    @Override
    public void characterStepOn(LoopManiaWorld l, List<ItemProperty> toRemoveGold,
            List<ItemProperty> toRemoveHealthPotion) {
        // TODO Auto-generated method stub
        
    }



    @Override
    public ImageView onLoadItems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canBePurchased() {
        return false;
    }
}