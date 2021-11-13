package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.GridPane;

public abstract class CardProperty extends Card{

    public CardProperty(String type, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(type, x, y);
        //TODO Auto-generated constructor stub
    }
    abstract void onLoad(LoopManiaWorldController controller, GridPane cards, GridPane squares);
    
}
