package unsw.blackout;

import java.time.Duration;
import java.time.LocalTime;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Blackout {
    private List<Satellite> satellites;
    private List<Device> devices;
    private LocalTime currentTime;

    public Blackout() {
        this.devices = new ArrayList<Device>();
        this.satellites = new ArrayList<Satellite>();
        this.currentTime = LocalTime.of(0, 0);
    }

    /**
     *
     * @param id
     * @param type
     * @param position
     */
    public void createDevice(String id, String type, double position) {
        Device newDevice;
        if (type.equals("HandheldDevice")) {
            newDevice = new HandheldDevice(id, type, position);
            newDevice.setConnectTime(1);
        } else if (type.equals("LaptopDevice")) {
            newDevice = new LaptopDevice(id, type, position);
            newDevice.setConnectTime(2);
        } else {
            newDevice = new DesktopDevice(id, type, position);
            newDevice.setConnectTime(5);
        }
        devices.add(newDevice);
        // Update possible connections
        for (Satellite satellite : satellites) {
            satellite.removePossibleConnections();
            for (Device device : devices) {
                satellite.addPossibleConnections(device);
            }
        }
    }

    /**
     *
     * @param id
     * @param type
     * @param height
     * @param position
     */
    public void createSatellite(String id, String type, double height, double position) {
        Satellite newSatellite;
        double velocity;
        if (type.equals("SpaceXSatellite")) {
            velocity = 3330 / 60.0;
            newSatellite = new SpaceXSatellite(id, type, height, position, velocity);
        } else if (type.equals("BlueOriginSatellite")) {
            velocity = 8500 / 60.0;
            newSatellite = new BlueOriginSatellite(id, type, height, position, velocity);
        } else if (type.equals("NasaSatellite")) {
            velocity = 5100 / 60.0;
            newSatellite = new NasaSatellite(id, type, height, position, velocity);
        } else {
            velocity = 6000 / 60.0;
            newSatellite = new SovietSatellite(id, type, height, position, velocity);
        }
        for (Device device : devices) {
            newSatellite.addPossibleConnections(device);
        }
        satellites.add(newSatellite);
    }

    /**
     *
     * @param deviceId
     * @param start
     * @param durationInMinutes
     */
    public void scheduleDeviceActivation(String deviceId, LocalTime start, int durationInMinutes) {
        LocalTime end = start.plusMinutes(durationInMinutes);
        for (Device device : devices) {
            if (device.getId().equals(deviceId)) {
                LocalTime [] activationPeriod = new LocalTime[2];
                activationPeriod[0] = start;
                activationPeriod[1] = end;
                device.addActivationPeriods(activationPeriod);
                break;
            }
        }
    }

    /**
     *
     * @param id
     */
    public void removeSatellite(String id) {
        for (Satellite satellite : satellites) {
            if (satellite.getId().equals(id)) {
                // disconnect devices with this satellite
                for (Connection connection: satellite.getConnections()) {
                    for (Device device : devices) {
                        if (device.getId().equals(connection.getDeviceId())) {
                            if (device.getIsConnected()) {
                                device.setIsConnected(false);
                            }
                            break;
                        }
                    }
                }
                satellites.remove(satellite);
                break;
            }
        }
    }


    /**
     *
     * @param id
     */
    public void removeDevice(String id) {
        for (Device device : devices) {
            if (device.getId().equals(id)) {
                devices.remove(device);
                break;
            }
        }
        for (Satellite satellite : satellites) {
            // update possible connections
            satellite.removePossibleConnections();
            for (Device device : devices) {
                satellite.addPossibleConnections(device);
            }
            // disconnect this device
            for (Connection connection: satellite.getConnections()) {
                if (id.equals(connection.getDeviceId())) {
                    if (connection.getEndTime() == null) {
                        connection.setEndTime(this.currentTime);
                    }
                }
            }
        }
    }

    /**
     *
     * @param id
     * @param newPos
     */
    public void moveDevice(String id, double newPos) {
        for (Device device : devices) {
            if (device.getId().equals(id)) {
                device.setPosition(newPos);
                for (Satellite satellite : satellites) {
                    // update possible connections
                    satellite.removePossibleConnections();
                    for (Device eachDevice : devices) {
                        satellite.addPossibleConnections(eachDevice);
                    }
                    // disconnect device that is not in possible conncetions
                    for (Connection connection: satellite.getConnections()) {
                        if (!satellite.getPossibleConnections().contains(connection.getDeviceId())) {
                            if (connection.getEndTime() == null) {
                                connection.setEndTime(this.currentTime);
                            }
                            if (id.equals(connection.getDeviceId())) {
                                if (device.getIsConnected()) {
                                    device.setIsConnected(false);
                                }
                            }
                        }
                    }
                }
                break;
            }
        }

    }

    /**
     *
     * @return
     */
    public JSONObject showWorldState() {
        JSONObject result = new JSONObject();
        JSONArray devices = new JSONArray();
        JSONArray satellites = new JSONArray();
        // Sort required fields
        Collections.sort(this.devices, Comparator.comparing(e->e.getId()));
        Collections.sort(this.satellites, Comparator.comparing(e->e.getId()));
        for (Satellite satellite : this.satellites) {
            satellite.sortPossibleConnections();
            satellite.sortConnections();
        }
        for (Device device : this.devices) {
            device.sortActivationPeriods();
        }
        // Create satellites array
        for (Satellite realSatellite : this.satellites) {
            JSONObject satellite = new JSONObject();
            satellite.put("id", realSatellite.getId());
            satellite.put("type", realSatellite.getType());
            satellite.put("height", realSatellite.getHeight());
            satellite.put("velocity", realSatellite.getVelocity());
            satellite.put("position", realSatellite.getPosition());
            satellite.put("possibleConnections", realSatellite.getPossibleConnections());
            // Create connections array
            JSONArray conns = new JSONArray();
            for (Connection connection : realSatellite.getConnections()) {
                JSONObject item = new JSONObject();
                item.put("satelliteId", realSatellite.getId());
                item.put("startTime", connection.getStartTime());
                item.put("endTime", connection.getEndTime() == null
                    ? JSONObject.NULL : connection.getEndTime());
                item.put("deviceId", connection.getDeviceId());
                item.put("minutesActive", connection.getMinutesActive());
                conns.put(item);
            }
            satellite.put("connections", conns);
            satellites.put(satellite);
        }
        // Create devices array
        for (Device realDevice : this.devices) {
            // create activaation periods arraay
            JSONArray activationPeriods = new JSONArray();
            for (LocalTime [] activationPeriod : realDevice.getActivationPeriods()) {
                JSONObject time = new JSONObject();
                time.put("startTime", activationPeriod[0].toString());
                time.put("endTime", activationPeriod[1].toString());
                activationPeriods.put(time);
            }
            JSONObject device = new JSONObject();
            device.put("id", realDevice.getId());
            device.put("type", realDevice.getType());
            device.put("position", realDevice.getPosition());
            device.put("isConnected", realDevice.getIsConnected());
            device.put("activationPeriods", activationPeriods);
            devices.put(device);
        }
        result.put("devices", devices);
        result.put("satellites", satellites);
        result.put("currentTime", currentTime);
        return result;
    }

    /**
     *
     * @param tickDurationInMinutes
     */
    public void simulate(int tickDurationInMinutes) {
        boolean antiClockwise = true; // direction of SovietSatellite
        Collections.sort(this.satellites, Comparator.comparing(e->e.getPosition()));
        // each step of the simulation
        for (int i = 0; i < tickDurationInMinutes; i += 1) {
            List<Device> activeDevices = selectActiveDevices();
            Collections.sort(activeDevices, Comparator.comparing(e->e.getId()));
            antiClockwise = changeSatellitePosition(antiClockwise);
            disConnectDevices(activeDevices);
            changeActiveMinutes();
            // connect other divices
            for (Device device : activeDevices) {
                // check if device is already connected
                boolean connected = false;
                for (Satellite satellite : satellites) {
                    for (Connection connection : satellite.getConnections()) {
                        if (connection.getDeviceId().equals(device.getId())
                                && connection.getEndTime() == null) {
                            connected = true;
                        }
                    }
                }
                if (connected) {
                    continue;
                }
                addNewConnection(device);
            }
            this.currentTime = this.currentTime.plusMinutes(1);
        }
    }

    // select active devices
    /**
     *
     * @return
     */
    public List<Device> selectActiveDevices() {
        List<Device> activeDevices = new ArrayList<Device>();
        for (Device device : devices) {
            List<LocalTime []> activationPeriods = device.getActivationPeriods();
            boolean isActive = false;
            // check if current time is in activation peroids
            for (LocalTime [] activationPeriod : activationPeriods) {
                if (this.currentTime.compareTo(activationPeriod[0]) >= 0
                        && this.currentTime.compareTo(activationPeriod[1]) <= 0 ) {
                    isActive = true;
                    break;
                }
            }
            if (isActive) {
                activeDevices.add(device);
            }
        }
        return activeDevices;
    }

    // change satellite position
    /**
     *
     * @param antiClockwise
     * @return
     */
    public boolean changeSatellitePosition(boolean antiClockwise) {
        for (Satellite satellite : satellites) {
            // w = v / r
            double degrees = Math.abs(satellite.getVelocity() * 1.0 / satellite.getHeight());
            // Soviet Satellite
            if (satellite.getType().equals("SovietSatellite")) {
                // Out of range
                if (satellite.getPosition() >= 345 ||
                        (satellite.getPosition() >= 0 && satellite.getPosition() <= 140)) {
                    antiClockwise = true;
                    if (satellite.getVelocity() < 0) {
                        satellite.setVelocity(-satellite.getVelocity());
                    }
                    double newPos = (satellite.getPosition() + degrees) % 360;
                    satellite.setPosition(newPos);
                } else if (satellite.getPosition() < 345 && satellite.getPosition() >= 190) {
                    antiClockwise = false;
                    if (satellite.getVelocity() > 0) {
                        satellite.setVelocity(-satellite.getVelocity());
                    }
                    double newPos = satellite.getPosition() - degrees;
                    satellite.setPosition(newPos);
                } else {
                    // In the range
                    if (antiClockwise) {
                        if (satellite.getPosition() + degrees <= 190) {
                            double newPos = satellite.getPosition() + degrees;
                            satellite.setPosition(newPos);
                        } else {
                            antiClockwise = false;
                            if (satellite.getVelocity() > 0) {
                                satellite.setVelocity(-satellite.getVelocity());
                            }
                            double newPos = 190 -(satellite.getPosition() + degrees - 190);
                            satellite.setPosition(newPos);
                        }
                    } else {
                        if (satellite.getPosition() - degrees >= 140) {
                            double newPos = satellite.getPosition() - degrees;
                            satellite.setPosition(newPos);
                        } else {
                            antiClockwise = true;
                            if (satellite.getVelocity() < 0) {
                                satellite.setVelocity(-satellite.getVelocity());
                            }
                            double newPos = 140 + (140 + degrees - satellite.getPosition());
                            satellite.setPosition(newPos);
                        }
                    }
                }
            } else {
                // other satellites
                double newPos = (satellite.getPosition() + degrees) % 360;
                satellite.setPosition(newPos);
            }
        }
        return antiClockwise;
    }

    // disconnect devices
    /**
     *
     * @param activeDevices
     */
    public void disConnectDevices(List<Device> activeDevices) {
        for (Satellite satellite : satellites) {
            // update possible connections
            satellite.removePossibleConnections();
            for (Device device : devices) {
                satellite.addPossibleConnections(device);
            }
            // disconnect inactive devices or devices without possible conncetions
            for (Connection connection: satellite.getConnections()) {
                if (!satellite.getPossibleConnections().contains(connection.getDeviceId())) {
                    // not in possible connections
                    if (connection.getEndTime() == null) {
                        connection.setEndTime(this.currentTime);
                    }
                    for (Device device : devices) {
                        if (device.getId().equals(connection.getDeviceId())) {
                            if (device.getIsConnected()) {
                                device.setIsConnected(false);
                            }
                            break;
                        }
                    }
                } else {
                    // not active
                    for (Device device : devices) {
                        if (device.getId().equals(connection.getDeviceId())
                                && !activeDevices.contains(device)) {
                            if (connection.getEndTime() == null) {
                                connection.setEndTime(this.currentTime);
                            }
                            if (device.getIsConnected()) {
                                device.setIsConnected(false);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    // add minutes for already connected devices
    public void changeActiveMinutes() {
        for (Satellite satellite : satellites) {
            for (Connection connection: satellite.getConnections()) {
                if (connection.getEndTime() == null) {
                    String deviceId  = connection.getDeviceId();
                    int connectTime = 0;
                    // get connection time
                    for (Device device : devices) {
                        if (device.getId().equals(deviceId)) {
                            connectTime = device.getConnectTime();
                            break;
                        }
                    }
                    // when the connection actually start
                    LocalTime activeTime = connection.getStartTime().plusMinutes(connectTime);
                    if (this.currentTime.equals(activeTime)) {
                        for (Device device : devices) {
                            if (device.getId().equals(deviceId)) {
                                device.setIsConnected(true);
                                break;
                            }
                        }
                    }
                    // set active minutes
                    if (this.currentTime.isAfter(activeTime)) {
                        int minutesActive = (int) Duration.between(activeTime, currentTime).toMinutes();
                        connection.setMinutesActive(minutesActive);
                    }
                }

            }
        }
    }

    // new connection for each device
    /**
     *
     * @param device
     */
    public void addNewConnection(Device device) {
        for (Satellite satellite : satellites) {
            // device not in possible connection
            if (!satellite.getPossibleConnections().contains(device.getId())) {
                continue;
            }

            if (satellite.getType().equals("SpaceXSatellite")) {
                device.setConnectTime(0);
                device.setIsConnected(true);
                Connection newConnection = new Connection(device.getId(),
                    this.currentTime, null, 0);
                satellite.addConnections(newConnection);
                break;
            } else if (satellite.getType().equals("BlueOriginSatellite")) {
                int numActive = 0;
                int numLaptop = 0;
                int numDesktop = 0;
                // check total active connections, active laptop, active desktops
                for (Connection connection : satellite.getConnections()) {
                    if (connection.getEndTime() == null) {
                        numActive += 1;
                        for (Device deviceTmp : devices) {
                            if (deviceTmp.getId().equals(connection.getDeviceId())) {
                                if (deviceTmp.getType().equals("LaptopDevice")) {
                                    numLaptop += 1;
                                } else if (deviceTmp.getType().equals("DesktopDevice")) {
                                    numDesktop += 1;
                                }
                                break;
                            }
                        }
                    }
                }
                if (numActive == 10) {
                    continue;
                }
                if (device.getType().equals("LaptopDevice") && numLaptop == 5) {
                    continue;
                } else if (device.getType().equals("DesktopDevice") && numDesktop == 2) {
                    continue;
                }

                Connection newConnection = new Connection(device.getId(),
                    this.currentTime, null, 0);
                satellite.addConnections(newConnection);
                break;
            } else if (satellite.getType().equals("NasaSatellite")) {
                device.setConnectTime(10);
                int numActive = 0;
                int outRigion = 0;
                // check number of active connection and number of outrigion devices
                for (Connection connection : satellite.getConnections()) {
                    if (connection.getEndTime() == null) {
                        numActive += 1;
                        for (Device deviceTmp : devices) {
                            if (deviceTmp.getId().equals(connection.getDeviceId())) {
                                if (deviceTmp.getPosition() < 30 ||
                                        deviceTmp.getPosition() > 40) {
                                    outRigion += 1;
                                }
                            }
                        }
                    }
                }
                // the connection is full
                if (numActive == 6) {
                    if (device.getPosition() < 30 || device.getPosition() > 40) {
                        continue;
                    } else {
                        if (outRigion == 0) {
                            continue;
                        } else {
                            // disconnect requred connection
                            for (Connection connection : satellite.getConnections()) {
                                if (connection.getEndTime() == null) {
                                    boolean exist = false;
                                    for (Device deviceTmp : devices) {
                                        if (deviceTmp.getId().equals(connection.getDeviceId())) {
                                            if (deviceTmp.getPosition() < 30 || deviceTmp.getPosition() > 40) {
                                                connection.setEndTime(this.currentTime);
                                                deviceTmp.setIsConnected(false);
                                                exist = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (exist) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                Connection newConnection = new Connection(device.getId(), this.currentTime, null, 0);
                satellite.addConnections(newConnection);
                break;
            } else if (satellite.getType().equals("SovietSatellite")) {
                device.setConnectTime(2 * device.getConnectTime());
                int numActive = 0;
                // check active connection
                for (Connection connection : satellite.getConnections()) {
                    if (connection.getEndTime() == null) {
                        numActive += 1;
                    }
                }
                if (numActive == 9) {
                    // drop the oldest connection
                    for (Connection connection : satellite.getConnections()) {
                        if (connection.getEndTime() == null) {
                            connection.setEndTime(this.currentTime);
                            for (Device deviceTmp : devices) {
                                if (deviceTmp.getId().equals(connection.getDeviceId())) {
                                    if (deviceTmp.getIsConnected()) {
                                        deviceTmp.setIsConnected(false);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                Connection newConnection = new Connection(device.getId(), this.currentTime, null, 0);
                satellite.addConnections(newConnection);
                break;
            }
        }
    }

}


