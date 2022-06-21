package com.example.truespotrtlsandroid.models;


import java.util.concurrent.ConcurrentHashMap;

public class BeaconList {
    private static BeaconList ourInstance;
    protected ConcurrentHashMap<String, ConcurrentHashMap<BeaconType, Beacon>> mFoundBeacons = new ConcurrentHashMap();

    public static BeaconList getInstance() {
        if (ourInstance == null) {
            ourInstance = new BeaconList();
        }

        return ourInstance;
    }

    private BeaconList() {
    }

    public Beacon getFoundBeaconIfExists(String address, BeaconType beaconType) {
        if (this.mFoundBeacons.containsKey(address)) {
            ConcurrentHashMap<BeaconType, Beacon> beacons = (ConcurrentHashMap) this.mFoundBeacons.get(address);
            return beacons.containsKey(beaconType) ? (Beacon) beacons.get(beaconType) : null;
        } else {
            return null;
        }
    }

    public void addBeacon(Beacon beacon) {
        ConcurrentHashMap beacons;
        if (this.mFoundBeacons.containsKey(beacon.mDevice.getAddress())) {
            beacons = (ConcurrentHashMap) this.mFoundBeacons.get(beacon.mDevice.getAddress());
            beacons.put(beacon.getBeaconType(), beacon);
        } else {
            beacons = new ConcurrentHashMap();
            beacons.put(beacon.getBeaconType(), beacon);
            this.mFoundBeacons.put(beacon.mDevice.getAddress(), beacons);
        }

    }

    public ConcurrentHashMap<String, ConcurrentHashMap<BeaconType, Beacon>> getFoundBeacons() {
        return this.mFoundBeacons;
    }
}
