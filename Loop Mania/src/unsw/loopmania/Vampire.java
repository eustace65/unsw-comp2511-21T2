package unsw.loopmania;
import java.io.File;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class Vampire extends EnemyProperty{
    private final String type = "Vampire";
    private final int FightR = 2;
    private final int SupportR = 5;
    private final int gold = 500;
    private int speed = 3;
    private int damage = 40;
    //private final boolean cirtical = true;
    private final boolean weak = false;
    private final int hp = 1000;
    private final int exp = 300;

    private int criticalPoss = 10;
    public Vampire(PathPosition position) {
        super(position);
        setType(type);
        setHP(this.hp);
        setExp(this.exp);
        setDamage(this.damage);
        setFightRadius(this.FightR);
        setSupportRadius(this.SupportR);
        //setIsCritical(this.cirtical);
        setIsWeak(this.weak);
        setGold(this.gold); //TODO can be changed
        setSpeed(this.speed);
        this.setCriticalPoss(criticalPoss);
    }

    @Override
    //TODO add character object as parameter
    public void attack_ally(Ally ally) {
        Random rand = new Random();
        int int_random = rand.nextInt(5);
        if (int_random == 0) {
            int times = rand.nextInt(10);
            ally.setHp(ally.getHp() - this.getDamage() * times);
            //TODO random additional damage with every vampire attack, for a random number of vampire attacks
            return;
        }
        //TODO deduct hp of ally/Character
        ally.setHp(ally.getHp() - this.getDamage());
        return;
    }

    @Override
    public void attack_character(Character c) {
        Random rand = new Random();
        int int_random = rand.nextInt(getCriticalPoss());
        if (int_random == 0) {
            int times = rand.nextInt(10);
            c.setHp(c.getHp().get() - this.getDamage() * times);
            //TODO random additional damage with every vampire attack, for a random number of vampire attacks
            return;
        }
        //TODO deduct hp of ally/Character
        c.setHp(c.getHp().get() - this.getDamage());
        c.charingSuperPower(20);
        return;
    }


    @Override
    public void setAllPropertyBack() {
        setDamage(40);
        setCriticalPoss(10);
    }

    @Override
    public boolean attack(LoopManiaWorld l, List<Ally> defeatedAllies, List<EnemyProperty> transferZombies,
            boolean inBattle, ItemProperty[] equipments) {
        if (Math.pow((l.getCharacter().getX() - getX()), 2) + Math.pow((l.getCharacter().getY() - getY()), 2) > Math
        .pow(getFightRadius(), 2)) {
            return false;
        }

        boolean hasAttacked = false;
        for (Ally ally : l.getAllies()) {
            if (ally.getHp() <= 0) {
                continue;
            }

            l.getCharacter().setInBattle(true);
            inBattle = true;
            //e.attack_ally(ally);
            hasAttacked = true;
            //if (ally.getHp() <= 0) {

            attack_ally(ally);
            if (ally.getHp() <= 0) {
                defeatedAllies.add(ally);
            }
            break;
        }
        if (!hasAttacked) {
            l.getCharacter().setInBattle(true);
            inBattle = true;

            for (ItemProperty item : equipments) {
                if (item == null) {
                    continue;
                }
                item.useDuringBattle(this, l.getCharacter(), ModeType.STANDARD);
            }
            //for (ItemProperty item : l)
            attack_character(l.getCharacter());
        }
        return true;
    }

    @Override
    public ImageView onLoadEnemy() {
        // TODO Auto-generated method stub
        return new ImageView( new Image((new File("src/images/vampire.png")).toURI().toString()));
    }

    @Override
    public boolean isBoss() {
        // TODO Auto-generated method stub
        return false;
    }

}

