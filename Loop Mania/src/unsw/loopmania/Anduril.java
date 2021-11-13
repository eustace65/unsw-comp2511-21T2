package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * represents an equipped or unequipped TheOneRing in the backend world
 */
public class Anduril extends RareItem {
    private final int damage = 250;

    private Image andurilImage;
    public Anduril(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.SWORD);
        //andurilImage = new Image((new File("src/images/anduril.png")).toURI().toString());

        //TODO Auto-generated constructor stub
    }

    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        // TODO Auto-generated method stub
        if (mode == ModeType.CONFUSING) {
            int defense = 20; 
            if (e.isBoss()) {
                e.setDamage(e.getDamage() - defense * 3);
            } else {
                e.setDamage(e.getDamage() - defense);
            }
    
            if (e.getDamage() < 0) e.setDamage(0);
        }
        if (e.isBoss()) {
            c.setDamage(c.getDamage() + 3 * damage);
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
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/anduril.png")).toURI().toString()));
    }

    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return false;
    }


}