package unsw.loopmania;

import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DoggieCoinPrice implements Observer {
    public static IntegerProperty price = new SimpleIntegerProperty(200);
    /*public DoggieCoinPrice() {
        price = new SimpleIntegerProperty(200);
    }*/
    
    @Override
    public void update(LoopManiaWorld l) {
        // TODO Auto-generated method stub
        Random rand = new Random();
        if (!l.hasSpawnMuske.get()) {
            int result = rand.nextInt(2);
            if (result == 0) {
                price.set(price.get() + rand.nextInt(30));
            } else {
                price.set(price.get() - rand.nextInt(30));
                if (price.get() < 0) price.set(0);
            }
        } else {
            if (!l.hasKilledMuske.get()) {
                price.set(price.get() + rand.nextInt(50));
            } else {
                price.set(300);
                l.hasSpawnMuske.set(false);
            }
        }
        
    }
}
