package unsw.loopmania;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;

public class HeroCastle extends BuildingProperty {
    private boolean offerWindow = false;

    public HeroCastle(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public boolean checkPathType() {
        // Cannot be placed expect one spot
        return true;
    }

    public boolean checkPathCycle() {
        if (this.getPathCycle() >= 5) {
            return true;
        }
        return false;
    }

    public boolean getOfferWindow() {
        return this.offerWindow;
    }

    public void openOfferWindow() {
        this.offerWindow = true;
    }

    public void closeOfferWindow() {
        this.offerWindow = false;
    }

    @Override
    public void spawnEnemy(LoopManiaWorld l, List<EnemyProperty> spawningEnemies) {
        return;
    }

    @Override
    public void characterStepOn(LoopManiaWorld l) {
        //TODO add open shop here
        return;
    }

    @Override
    public void enemyStepOn(LoopManiaWorld l, List<BuildingProperty> toRemove) {
        return;
    }

    @Override
    public ImageView onLoadBuilding() {
        // TODO Auto-generated method stub
        return null;
    }
}
