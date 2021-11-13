package unsw.loopmania;

import java.io.File;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Doggie extends EnemyProperty{
    private final String type = "Doggie";
    private final int FightR = 1;
    private final int SupportR = 1;
    private final int gold = 100;
    private final int speed = 2;
    private int damage = 60;
    //private final boolean cirtical = true;
    private final boolean weak = false;
    private final int hp = 1000;
    private final int exp = 100;
    private int criticalPoss = 10;

    public Doggie(PathPosition position) {
        super(position);
        setType(this.type);
        setHP(this.hp);
        setExp(this.exp);
        setDamage(this.damage);
        setFightRadius(this.FightR);
        setSupportRadius(this.SupportR);
        //setIsCritical(this.cirtical);
        setIsWeak(this.weak);
        setGold(this.gold); //TODO can be changed
        setSpeed(this.speed);
        setCriticalPoss(criticalPoss);
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
                hasAttacked = true;
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
                attack_character(l.getCharacter());
            }
        return true;

    }

    @Override
    public ImageView onLoadEnemy() {
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/doggie.png")).toURI().toString()));
    }


    @Override
    public void setAllPropertyBack() {
        setDamage(60);
        setCriticalPoss(10);
    }




    @Override
    public void attack_character(Character c) {
        super.attack_character(c);
        c.charingSuperPower(20);
        Random rand = new Random();
        int result = rand.nextInt(criticalPoss);
        if (result == 0) {
            c.setStunTimes(c.getStuntimes() + 1);
        }
    }



    @Override
    public boolean isBoss() {
        // TODO Auto-generated method stub
        return true;
    }
}
