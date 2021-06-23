package unsw.blackout;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class Device {
    private String id;
    private String type;
    private double position;
    private List<LocalTime []> activationPeriods;
    private boolean isConnected;
    private int connectTime;

    public Device(String id, String type, double position) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isConnected = false;
        this.activationPeriods = new ArrayList<LocalTime[]>();
        this.connectTime = 0;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public double getPosition() {
        return this.position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean getIsConnected() {
        return this.isConnected;
    }

    public void addActivationPeriods(LocalTime [] activationPeriod) {
        this.activationPeriods.add(activationPeriod);
    }

    // sort based on start time
    public void sortActivationPeriods() {
        Collections.sort(this.activationPeriods, Comparator.comparing(e->e[0]));
    }

    public List<LocalTime []> getActivationPeriods() {
        return this.activationPeriods;
    }

    public int getConnectTime() {
        return this.connectTime;
    }

    public void setConnectTime(int connectTime) {
        this.connectTime = connectTime;
    }
}




