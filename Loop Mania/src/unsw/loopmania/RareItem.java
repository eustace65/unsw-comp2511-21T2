package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class RareItem extends ItemProperty  {
    ItemType type;
    public RareItem(SimpleIntegerProperty x, SimpleIntegerProperty y, ItemType type) {
        super(x, y, type);
        //this.type = type;
    }
}