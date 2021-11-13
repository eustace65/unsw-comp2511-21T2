package unsw.loopmania;

import java.io.File;
import java.util.List;
import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * represents an equipped or unequipped Staff in the backend world
 */
public class Staff extends ItemProperty {
    private final int damage = 100;
    public static IntegerProperty price = new SimpleIntegerProperty(2000);
    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, ItemType.STAFF);
    }

    public int getDamage() {
        return damage;
    }


    public void trance(EnemyProperty enemy, LoopManiaWorld world) {
        Random rand = new Random();
        int random = rand.nextInt(5);
        if (random == 0) {
            PathPosition position = enemy.getPathPosition();
            world.killEnemy(enemy);
            Ally ally = new Ally(position);
            ally.setRound(5);
            ally.setOriginalType(enemy.getType());
            world.addAlly(ally);
        }
    }

    @Override
    public void useDuringBattle(EnemyProperty e, Character c, ModeType mode) {
        // TODO Auto-generated method stub
        c.setDamage(c.getDamage() + damage);

    }



    @Override
    public void characterStepOn(LoopManiaWorld l, List<ItemProperty> toRemoveGold,
            List<ItemProperty> toRemoveHealthPotion) {
        // TODO Auto-generated method stub

    }

    @Override
    public ImageView onLoadItems() {
        return new ImageView(new Image((new File("src/images/staff.png")).toURI().toString()));
    }

    @Override
    public boolean canBePurchased() {
        // TODO Auto-generated method stub
        return true;
    }
}