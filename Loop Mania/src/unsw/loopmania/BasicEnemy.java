package unsw.loopmania;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

import java.lang.Math;
/**
 * a basic form of enemy in the world
 */
public class BasicEnemy extends MovingEntity {
    // TODO = modify this, and add additional forms of enemy


    private String type;
    private int hp;
    private int exp;
    private int damage;
    private int fightRadius;
    private int supportRadius;
    //private boolean critical;
    private boolean weak;
    private int gold;
    private int speed;
    private boolean inBattle;
    private String lastMoveDirection;
    private int criticalPoss;
    public BasicEnemy(PathPosition position) {
        super(position);
        this.inBattle = false;
        this.lastMoveDirection = "Up";
    }

    public int getCriticalPoss() {
        return this.criticalPoss;
    }

    public void setCriticalPoss(int poss) {
        this.criticalPoss = poss;
    }

    public String getType() {
        return this.type;
    }
    
    public int getHP() {
        return this.hp;
    }

    public int getExp(){
        return this.exp;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getFightRadius() {
        return this.fightRadius;
    }

    public int getSupportRadius() {
        return this.supportRadius;
    }

    public boolean getIsWeak() {
        return this.weak;
    }

    public int getGold() {
        return this.gold;
    }

    public int getSpeed() {
        return this.speed;
    }

    public Boolean getInBattle() {
        return this.inBattle;
    }

    public String getLastDirec() {
        return this.lastMoveDirection;
    }


    
    
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setHP(int hp) {
        // TODO: set the limit of hp
        this.hp = hp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setFightRadius(int radius) {
        this.fightRadius = radius;
    }

    public void setSupportRadius(int radius) {
        this.supportRadius = radius;
    }

    public void setIsWeak(boolean flag) {
        this.weak = flag;
    }
    
    public void setGold(int g) {
        this.gold = g;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public void setInBattle(boolean flag) {
        this.inBattle = flag;
    }


    public void setLastDirec(String s) {
        this.lastMoveDirection = s;
    }

  
    public void attack_ally(Ally ally) {
        //TODO
        ally.setHp(ally.getHp() - this.getDamage());
    }

    public void attack_character(Character c) {
        c.setHp(c.getHp().get() - this.getDamage());
    }


    /**
     * move the enemy
     */
    public void move(){
        // TODO = modify this, since this implementation doesn't provide the expected enemy behaviour
        // this basic enemy moves in a random direction... 25% chance up or down, 50% chance not at all...
        
        

        int directionChoice = (new Random()).nextInt(2);
            if (directionChoice == 0){
                moveUpPath();
                this.lastMoveDirection = "Down";
            }
            else if (directionChoice == 1){
                moveDownPath();
                this.lastMoveDirection = "Up";
            }
       
    }


    public int getDistance(int destX, int destY) {

        int srcX = this.getX();
        int srcY = this.getY();
        return (int)Math.sqrt(Math.pow(destX - srcX,2) + Math.pow(destY - srcY , 2));
    }

    public void setAllPropertyBack() {
        return;
    }
}
