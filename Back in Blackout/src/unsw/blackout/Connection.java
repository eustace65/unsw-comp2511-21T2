package unsw.blackout;
import java.time.LocalTime;

public class Connection {
    private String deviceId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int minutesActive;

    public Connection(String deviceId, LocalTime startTime, LocalTime endTime, int minutesActive) {
        this.deviceId = deviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minutesActive = minutesActive;
    }


    public String getDeviceId() {
        return this.deviceId;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getMinutesActive() {
        return this.minutesActive;
    }

    public void setMinutesActive(int minutesActive) {
        this.minutesActive = minutesActive;
    }

}
