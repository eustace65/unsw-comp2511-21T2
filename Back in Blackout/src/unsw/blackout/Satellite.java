package unsw.blackout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Satellite {
    private String id;
    private String type;
    private double height;
    private double position;
    private List<Connection> connections;
    private List<String> possibleConnections;
    private double velocity;

    public Satellite(String id, String type, double height, double position, double velocity) {
        this.id = id;
        this.type = type;
        this.height = height;
        this.position = position;
        this.connections = new ArrayList<Connection>();
        this.possibleConnections = new ArrayList<String>();
        this.velocity = velocity;
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

    public double getHeight() {
        return this.height;
    }

    public double getVelocity() {
        return this.velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public List<String> getPossibleConnections() {
        return this.possibleConnections;
    }

    public List<Connection> getConnections() {
        return this.connections;
    }

    /**
     *
     * @param device
     */
    public void addPossibleConnections(Device device) {
        String deviceId = device.getId();
        // check range
        if (MathsHelper.satelliteIsVisibleFromDevice(position, height, device.getPosition())) {
            // check type
            if (type.equals("SpaceXSatellite")) {
                if (device.getType().equals("HandheldDevice")) {
                    possibleConnections.add(deviceId);
                }
            } else if (type.equals("SovietSatellite")) {
                if (device.getType().equals("LaptopDevice") || device.getType().equals("DesktopDevice")) {
                    possibleConnections.add(deviceId);
                }
            } else {
                possibleConnections.add(deviceId);
            }
        }
    }

    public void sortPossibleConnections() {
        Collections.sort(possibleConnections);
    }

    public void removePossibleConnections() {
        this.possibleConnections.clear();
    }

    public void addConnections(Connection connection) {
        this.connections.add(connection);
    }

    // sort based on start time and id
    public void sortConnections() {
        Collections.sort(connections,
            Comparator.comparing(Connection::getStartTime).thenComparing(Connection::getDeviceId));
    }


}


