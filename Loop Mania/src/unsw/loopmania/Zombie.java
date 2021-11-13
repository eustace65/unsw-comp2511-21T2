package unsw.loopmania;
import java.io.File;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class Zombie extends EnemyProperty{
    //TODO can be changed
    private final String type = "Zombie";
    private final int FightR = 2;
    private final int SupportR = 4;
    private final int gold = 300;
    private final int speed = 2;
    private int damage = 30;
    //private final boolean cirtical = true;
    private final boolean weak = false;
    private final int hp = 800;
    private final int exp = 100;
    private int criticalPoss = 10;
    public Zombie(PathPosition position) {
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
    //TODO add character object as parameter
    public void attack_ally(Ally ally) {

        return;
    }

    @Override
    public void setAllPropertyBack() {
        setDamage(30);
        setCriticalPoss(10);
    }

    @Override
    public boolean attack(LoopManiaWorld l, List<Ally> defeatedAllies, List<EnemyProperty> transferZombies,
            boolean inBattle, ItemProperty[] equipments) {
        // TODO Auto-generated method stub

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

            Random rand = new Random();
            int int_random = rand.nextInt(5);
            if (int_random == 0) {
                EnemyProperty newZombie = new Zombie(ally.getPathPosition());
                transferZombies.add(newZombie);
                defeatedAllies.add(ally);
                break;
            }

            ally.setHp(ally.getHp() - getDamage());
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
            l.getCharacter().charingSuperPower(20);
        }
        return true;
    }

    @Override
    public ImageView onLoadEnemy() {
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/zombie.png")).toURI().toString()));
    }

    @Override
    public boolean isBoss() {
        // TODO Auto-generated method stub
        return false;
    }


}
