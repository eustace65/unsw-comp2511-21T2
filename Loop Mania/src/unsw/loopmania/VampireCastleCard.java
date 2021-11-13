package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a vampire castle card in the backend game world
 */
public class VampireCastleCard extends Card {
    // TODO = add more types of card
    public VampireCastleCard(String type, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(type, x, y);
    }    
}
