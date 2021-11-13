package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a Card in the world
 * which doesn't move
 */
public abstract class Card extends StaticEntity {
    // TODO = implement other varieties of card than VampireCastleCard
    private String type;
    public Card(String type, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
