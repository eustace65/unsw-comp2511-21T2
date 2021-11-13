package unsw.loopmania;

public class Ally extends MovingEntity{
    private String type = "Ally";
    private int hp = 100;
    private int damage = 200;
    private int round = 0;
    private String originalType = "";

    public Ally(PathPosition position) {
        super(position);
    }

    public String getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getOriginalType() {
        return originalType;
    }

    public void setOriginalType(String originalType) {
        this.originalType = originalType;
    }

    public void attack(EnemyProperty e) {
        e.setHP(e.getHP() - damage);
    }

}
