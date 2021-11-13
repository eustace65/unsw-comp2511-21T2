package unsw.loopmania;
// import java.util.ArrayList;
// import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity {
    // TODO = potentially implement relationships between this class and other classes
    private IntegerProperty hp;
    private DoubleProperty hpProgress;
    private int damage = 100;
    private Boolean inBattle;
    private int stunTimes;

    // extension: press R to use
    private IntegerProperty superPower;
    private DoubleProperty superPowerProgress;
    private int superPowerDuration;
    public Character(PathPosition position) {
        super(position);
        hp = new SimpleIntegerProperty(500);
        hpProgress = new SimpleDoubleProperty((double)500 / 500.00);
        setDamage(this.damage);
        this.inBattle = false;
        stunTimes = 0;
        superPower = new SimpleIntegerProperty(0);
        superPowerProgress = new SimpleDoubleProperty((double) 0/ 400.00);
        superPowerDuration = 0;
    }
    public int getStuntimes() {
        return this.stunTimes;
    }

    public void setStunTimes(int newTimes) {
        this.stunTimes = newTimes;
    }

    public IntegerProperty getHp() {
        return this.hp;
    }
    public DoubleProperty getHpProgress() {
        return hpProgress;
    }

    public void setHp(int hp) {
        // TODO: Check if it reaches the highest possible hp
        
        if (hp < 0) {
            this.hp.set(0);
            this.hpProgress.set((double) 0 / 500.00);
        } else if (hp > 500) {
            this.hp.set(500);
            this.hpProgress.set((double) 500 / 500.00);
        }
        else {
            this.hp.set(hp);
            this.hpProgress.set((double) hp / 500.00);
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }



    public void setInBattle(Boolean flag) {
        this.inBattle = flag;
    }

    public Boolean getInBattle() {
        return this.inBattle;
    }



    //TODO : check equipped item and use weapon
    public void attack(EnemyProperty e, ItemProperty[] equipments, ModeType mode) {
        //TODO
        for (ItemProperty item : equipments) {
            if (item == null) {
                continue;
            } 
            item.useDuringBattle(e, this, mode);
        }
        if (getStuntimes() > 0) {
            setStunTimes(getStuntimes() - 1);
            return;
        }
        e.setHP(e.getHP() - this.getDamage());
    }

    public void useHealthPotion(HealthPotion healthPotion) {
        setHp(500);
    }  

    public void useSword(Sword sword, EnemyProperty enemy) {
        enemy.setHP(enemy.getHP() - damage - sword.getDamage());
    }

    public void useStake(Stake stake, EnemyProperty enemy) {
        enemy.setHP(enemy.getHP() - damage - stake.getDamage(enemy));
    }

    public void useStaff(Staff staff, EnemyProperty enemy, LoopManiaWorld world) {
        enemy.setHP(enemy.getHP() - damage - staff.getDamage());
        staff.trance(enemy, world);
    }

    public void useTheOneRing(TheOneRing theOneRing) {
        setHp(500);
    }

    public void setDamageBack() {
        setDamage(100);
    }

    public IntegerProperty getSuperPower() {
        return superPower;
    }

    public DoubleProperty getSuperPowerProgress() {
        return superPowerProgress;
    }

    public void useSuperPower() {
        if (superPower.get() < 400) return;
        superPowerDuration = 10;
        superPower.set(0);
        superPowerProgress.set((double) 0 / 400.00);
    }
    public int getSuperPowerDuration() {
        return this.superPowerDuration;
    }
    public void setSuperPowerDuration(int time) {
        this.superPowerDuration = time;
    } 

    public void charingSuperPower(int value) {
        superPower.set(superPower.get() + value);
        superPowerProgress.set((double)(superPower.get() + value) / 400.00);
        if (superPower.get() > 400) {
            superPower.set(400);
            superPowerProgress.set((double) 400 / 400.00);
        }
    }
}


