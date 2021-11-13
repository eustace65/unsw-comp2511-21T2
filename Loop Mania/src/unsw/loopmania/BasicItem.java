package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class BasicItem extends Item {
    ItemType type;
    public BasicItem(SimpleIntegerProperty x, SimpleIntegerProperty y, ItemType type) {
        super(x, y, type);
    }

}
