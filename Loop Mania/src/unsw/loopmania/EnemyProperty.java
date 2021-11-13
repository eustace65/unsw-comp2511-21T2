package unsw.loopmania;

import java.util.List;

import javafx.scene.image.ImageView;

public abstract class EnemyProperty extends BasicEnemy{
    public EnemyProperty(PathPosition position) {
        super(position);
        //TODO Auto-generated constructor stub
    }


    abstract public boolean attack(LoopManiaWorld l, List<Ally> defeatedAllies, List<EnemyProperty> transferZombies, boolean inBattle, ItemProperty[] equipments);

    abstract public ImageView onLoadEnemy();

    abstract public boolean isBoss();
    abstract public void setAllPropertyBack();
    
    
}
