package unsw.loopmania;

import java.io.File;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ElanMuske extends EnemyProperty{
    private final String type = "ElanMuske";
    private final int FightR = 1;
    private final int SupportR = 1;
    private final int gold = 100;
    private final int speed = 2;
    private int damage = 100;
    //private final boolean cirtical = true;
    private final boolean weak = false;
    private final int hp = 2000;
    private final int exp = 100;
    private int criticalPoss = 10;

    public ElanMuske(PathPosition position) {
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
            // aimed for see the price change with doggie, muske has 50% of chance to jump over character
            Random rand = new Random();
            int result = rand.nextInt(2);
            /*if (result == 1) {
                return false;
            }*/
            if (l.getCycle() <= 5) return false;
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
                recoverEnemies(l.getEnemy());
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
                l.getCharacter().charingSuperPower(40);
                recoverEnemies(l.getEnemy());
            }
        return true;
    }

    @Override
    public ImageView onLoadEnemy() {
        // TODO Auto-generated method stub
        return new ImageView(new Image((new File("src/images/ElanMuske.png")).toURI().toString()));
    }

    @Override
    public boolean isBoss() {
        // TODO Auto-generated method stub
        return true;
    }

    public void recoverEnemies(List<EnemyProperty> enemies) {
        for (EnemyProperty e : enemies) {
            e.setHP((int)(e.getHP() * 1.1));
        }
    }

    @Override
    public void setAllPropertyBack() {
        // TODO Auto-generated method stub
        this.setDamage(150);
        this.setCriticalPoss(10);

    }

}
