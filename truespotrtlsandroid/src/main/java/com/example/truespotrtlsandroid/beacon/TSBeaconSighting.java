package com.example.truespotrtlsandroid.beacon;

import com.example.truespotrtlsandroid.Proximity;
import com.example.truespotrtlsandroid.models.BaseJSONModel;
import com.example.truespotrtlsandroid.models.Beacon;
import com.example.truespotrtlsandroid.models.IBeacon;
import com.example.truespotrtlsandroid.models.Range;

import java.util.Objects;

public class TSBeaconSighting extends BaseJSONModel {

    public enum BatteryLevel {
        LOW,
        MED_LOW,
        MED,
        MED_HIGH,
        HIGH
    }

    private String beaconId;
    private int rssi;
    private long timeMillis;
    private int temperature;
    private BatteryLevel batteryLevel;
    private String uuid;
    private int minor;
    private int major;
    private String deviceAddress;
    private String beaconIdentifier;
    private String id;
    public Proximity proximity;

    public String getBeaconIdentifier() {
        return beaconIdentifier;
    }

    public void setBeaconIdentifier(String beaconIdentifier) {
        this.beaconIdentifier = beaconIdentifier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TSBeaconSighting(String beaconId, int rssi, String deviceAddress, String uuid, int minor, int major) {
        this.beaconId = beaconId;
        this.rssi = rssi;
        //this.timeMillis = other.timeMillis;
        // this.temperature = other.temperature;
        // this.batteryLevel = other.batteryLevel;
        this.uuid = uuid;
        this.minor = minor;
        this.major = major;
        this.deviceAddress = deviceAddress;
        this.beaconIdentifier = beaconId;
        // this.id = other.id;
    }


    public TSBeaconSighting(TSBeaconSighting other) {
        this.beaconId = other.beaconId;
        this.rssi = other.rssi;
        this.timeMillis = other.timeMillis;
        this.temperature = other.temperature;
        this.batteryLevel = other.batteryLevel;
        this.uuid = other.uuid;
        this.minor = other.minor;
        this.major = other.major;
        this.deviceAddress = other.deviceAddress;
        this.beaconIdentifier = other.beaconIdentifier;
        this.id = other.id;
    }

    public TSBeaconSighting(IBeacon b) {
        this.rssi = b.getRssi();
        this.timeMillis = b.getLastSeen();
        this.temperature = (int) b.getTemperature();
        this.batteryLevel = BatteryLevel.HIGH;
        this.uuid = b.getUuid();
        this.minor = b.getMinor();
        this.major = b.getMajor();
        this.deviceAddress = b.getDevice().getAddress();
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = BatteryLevel.HIGH;
    }

    public int getRSSI() {
        return rssi;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getUuid() {
        return uuid;
    }

    public int getMinor() {
        return minor;
    }

    public int getMajor() {
        return major;
    }

    public void update(TSBeaconSighting other) {
        this.rssi = other.rssi;
        this.timeMillis = other.timeMillis;
        this.temperature = other.temperature;
        this.batteryLevel = other.batteryLevel;
    }

    public void update(Beacon other) {
        this.rssi = other.getRssi();
        this.timeMillis = other.getLastSeen();
        this.temperature = (int) other.getTemperature();
    }

    public BatteryLevel getBatteryLevel() {
        return batteryLevel;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSBeaconSighting that = (TSBeaconSighting) o;
        return minor == that.minor &&
                Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, minor);
    }

    @Override
    public String toString() {
        return "MoLoCarBeaconSighting{" +
                "beaconId='" + beaconId + '\'' +
                ", rssi=" + rssi +
                ", timeMillis=" + timeMillis +
                ", temperature=" + temperature +
                ", uuid='" + uuid + '\'' +
                ", minor=" + minor +
                ", batteryLevel=" + batteryLevel +
                ", deviceAddress=" + deviceAddress +
                '}';
    }

    public static BatteryLevel getBatteryLevel(String level) {
        for (BatteryLevel bl : BatteryLevel.values()) {
            if (bl.name().equalsIgnoreCase(level)) {
                return bl;
            }
        }
        return BatteryLevel.HIGH;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public Proximity getProximity() {
        return this.proximity == null ? Proximity.UNKNOWN : this.proximity;
    }

    public void setProximity(Proximity proximity) {
        this.proximity = proximity;
    }
}
