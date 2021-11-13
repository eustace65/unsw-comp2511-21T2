package unsw.loopmania;

import java.io.File;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * represents an equipped or unequipped Gold in the backend world
 */
public class Gold extends ItemProperty {
    public static IntegerProperty value = new SimpleIntegerProperty(200);

    public Gold(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.OTHER);
    }

    
    @Override
    public void characterStepOn(LoopManiaWorld l,List<ItemProperty> toRemoveGold, List<ItemProperty> toRemoveHealthPotion) {
        // TODO Auto-generated method stub
        if (l.getCharacter().getX() == getX() && l.getCharacter().getY() == getY()) {
            toRemoveGold.add(this);
        }

    }

    @Override
    public ImageView onLoadItems() {
        return new ImageView(new Image((new File("src/images/gold_pile.png")).toURI().toString()));
    }
    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        // TODO Auto-generated method stub
        
    }
}