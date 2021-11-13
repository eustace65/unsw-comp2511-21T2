package unsw.loopmania;
import javafx.beans.property.SimpleIntegerProperty;

public class Item extends StaticEntity {
    ItemType type;
    public Item(SimpleIntegerProperty x, SimpleIntegerProperty y, ItemType type) {
        super(x, y);
        this.type = type;
    }

    public ItemType getType() {
        return this.type;
    }

    public int getSlot() {
        return this.type.getIndex();
    }

}
