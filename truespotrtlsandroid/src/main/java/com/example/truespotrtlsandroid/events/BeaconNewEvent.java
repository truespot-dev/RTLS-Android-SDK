package com.example.truespotrtlsandroid.events;


import com.example.truespotrtlsandroid.beacon.TSBeaconSighting;

public class BeaconNewEvent extends BaseEvent {

    private final TSBeaconSighting beacon;

    public BeaconNewEvent(TSBeaconSighting beacon) {
        this.beacon = beacon;
    }

    public TSBeaconSighting getBeacon() {
        return beacon;
    }
}
