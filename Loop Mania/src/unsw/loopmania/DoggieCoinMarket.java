package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.BooleanProperty;

public class DoggieCoinMarket implements Subject{
    LoopManiaWorld l;
    private List<Observer> observers = new ArrayList<>();
    
    public DoggieCoinMarket(LoopManiaWorld world) {
        l = world;
    }
    @Override
    public void registerObserver(Observer o) {
        // TODO Auto-generated method stub
        observers.add(o);
        
    }

    @Override
    public void removeObserver(Observer o) {
        // TODO Auto-generated method stub
        observers.remove(o);
        
    }

    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        for(Observer o: observers) {
            o.update(l);
        }
        
    }
    
}
