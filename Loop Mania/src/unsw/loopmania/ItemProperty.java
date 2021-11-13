package unsw.loopmania;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;

public abstract class ItemProperty extends BasicItem {

    public ItemProperty(SimpleIntegerProperty x, SimpleIntegerProperty y, ItemType type) {
        super(x, y, type);
        //TODO Auto-generated constructor stub
    }

    abstract public void useDuringBattle(EnemyProperty e, Character c, ModeType mode);
    abstract public void characterStepOn(LoopManiaWorld l,List<ItemProperty> toRemoveGold, List<ItemProperty> toRemoveHealthPotion);
    abstract public ImageView onLoadItems();
    abstract public boolean canBePurchased();
    
}
