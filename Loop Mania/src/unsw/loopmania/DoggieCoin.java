package unsw.loopmania;

import java.io.File;
import java.util.List;
import java.util.Random;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class DoggieCoin extends ItemProperty{
    public static IntegerProperty price;

    BooleanProperty muskeSpawned;
    BooleanProperty muskeAlive;

    public DoggieCoin(SimpleIntegerProperty x, SimpleIntegerProperty y, ItemType type) {
        super(x, y, type);
        price = new SimpleIntegerProperty(800);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        return;

    }

    @Override
    public void characterStepOn(LoopManiaWorld l, List<ItemProperty> toRemoveGold,
            List<ItemProperty> toRemoveHealthPotion) {
        return;

    }


    /*public void setPrice(int value) {
        this.price.set(value);
    }*/

    @Override
    public ImageView onLoadItems() {
        return new ImageView(new Image((new File("src/images/doggiecoin.png")).toURI().toString()));
    }

    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return true;
    }


}
