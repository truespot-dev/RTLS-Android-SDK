package com.example.truespotrtlsandroid;

import com.example.truespotrtlsandroid.beacon.TSBeaconSighting;
import com.example.truespotrtlsandroid.models.BaseJSONModel;
import com.example.truespotrtlsandroid.models.Beacon;
import com.example.truespotrtlsandroid.models.TSDevice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

import java.util.Dictionary;
import java.util.Hashtable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TSBeacon extends BaseJSONModel {

    @SerializedName("uuid")
    public String uuid;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("beaconIdentifier")
    public String beaconIdentifier;
    @SerializedName("isActive")
    public boolean isActive;
    @SerializedName("vin")
    public String vin;
    @SerializedName("lat")
    public double lat;
    @SerializedName("lng")
    public double lng;
    @SerializedName("timeStamp")
    public long timeStamp;
    @SerializedName("RSSI")
    public int RSSI;
    @SerializedName("accuracy")
    public double accuracy;
    @SerializedName("major")
    public String major;
    @SerializedName("minor")
    public String minor;
    @SerializedName("proximity")
    public Proximity proximity = Proximity.UNKNOWN;
    @SerializedName("assetIdentifier")
    public String assetIdentifier;
    @SerializedName("assetType")
    public String assetType;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeaconIdentifier() {
        return beaconIdentifier;
    }

    public void setBeaconIdentifier(String beaconIdentifier) {
        this.beaconIdentifier = beaconIdentifier;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getRSSI() {
        return RSSI;
    }

    public void setRSSI(int RSSI) {
        this.RSSI = RSSI;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public Proximity getProximity() {
        return proximity;
    }

    public void setProximity(Proximity proximity) {
        this.proximity = proximity;
    }

    public String getAssetIdentifier() {
        return assetIdentifier;
    }

    public void setAssetIdentifier(String assetIdentifier) {
        this.assetIdentifier = assetIdentifier;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    TSBeacon(TSBeaconSighting beacon) {
        lat = beacon.getLat();
        lng = beacon.getLng();
        RSSI = beacon.getRSSI();
        beaconIdentifier = beacon.getBeaconId();
        accuracy = beacon.accuracy;
        uuid = beacon.getUuid().replace("-", "");
        major = String.valueOf(beacon.getMajor());
        minor = String.valueOf(beacon.getMinor());
        timeStamp = beacon.getTimeMillis();
        proximity = beacon.getProximity();
    }

    TSBeacon(String beaconIdentifier) {
        beaconIdentifier = beaconIdentifier;
    }

    TSBeacon(TSDevice device) {
        beaconIdentifier = device.tagIdentifier;
        //timeStamp = data["timeStamp"] as? TimeInterval
        lat = device.latitude;
        lng = device.longitude;
        //RSSI = data["rssi"] as? Int
        accuracy = device.accuracy;
        uuid = device.uuid;
        major = device.major;
        minor = device.minor;
        assetIdentifier = device.assetIdentifier;
        assetType = device.assetType;
    }

    public void update(TSBeacon other) {
        this.RSSI = other.getRSSI();
        this.timeStamp = other.getTimeStamp();
    }
    public void update(Beacon other) {
        this.RSSI = other.getRssi();
    }

    public boolean alreadyAssigned() {
        if (vin != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Beacon ID='" + beaconIdentifier + '\'' +
                ", TimeStamp=" + timeStamp +
                ", Lat=" + lat +
                ", Long=" + lng +
                ", RSSI='" + RSSI + '\'' +
                ", Acuracy=" + accuracy +
                ", UUID=" + uuid +
                ", Minor=" + minor +
                ", Major=" + major +
                '}';
    }

    public Dictionary toDictionary() {

        Dictionary<String, String> dict = new Hashtable<String, String>();
        dict.put("lat", String.valueOf(lat));
        dict.put("lng", String.valueOf(lng));
        dict.put("rssi", String.valueOf(RSSI));
        dict.put("beaconIdentifier", beaconIdentifier);
        dict.put("accuracy", String.valueOf(accuracy));
        dict.put("batteryLevel", beaconIdentifier);
        dict.put("uuid", major);
        dict.put("major", minor);
        dict.put("timeStamp", String.valueOf(timeStamp));
        return dict;
    }


}
