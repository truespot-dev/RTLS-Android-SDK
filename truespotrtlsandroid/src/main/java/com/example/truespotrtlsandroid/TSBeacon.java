package com.example.truespotrtlsandroid;

import com.example.truespotrtlsandroid.beacon.TSBeaconSighting;
import com.example.truespotrtlsandroid.models.BaseJSONModel;
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


    TSBeacon(TSBeaconSighting  beacon, CLLocation currentLocation) {
        lat = currentLocation.coordinate.latitude !=null ? Double.parseDouble(currentLocation.coordinate.latitude) : 0 ;
        lat = currentLocation.coordinate.longitude !=null ? Double.parseDouble(currentLocation.coordinate.longitude) : 0 ;

        RSSI = beacon.getRSSI();
        beaconIdentifier = beacon.getBeaconId();
        accuracy = currentLocation.horizontalAccuracy != null ? Double.parseDouble(currentLocation.horizontalAccuracy) : 0;
        uuid =  beacon.getUuid().replace("-","");
        major = String.valueOf(beacon.getMajor()) ;
        minor = String.valueOf(beacon.getMinor());
        timeStamp =  beacon.getTimeMillis();
        proximity = beacon.getProximity();
    }

    TSBeacon(String beaconIdentifier ) {
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

    public boolean alreadyAssigned() {
        if (vin != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return  "Beacon ID='" + beaconIdentifier + '\'' +
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

   public  Dictionary toDictionary()
   {

       Dictionary<String, String> dict = new Hashtable<String, String>();
       dict.put("lat",String.valueOf(lat));
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
