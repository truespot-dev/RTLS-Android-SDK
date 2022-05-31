package com.example.truespotrtlsandroid.events;

import com.example.truespotrtlsandroid.beacon.TSBeaconSighting;


public class BeaconUpdatedEvent extends BaseEvent {
    private final TSBeaconSighting beacon;

    public BeaconUpdatedEvent(TSBeaconSighting beacon) {
        this.beacon = beacon;
    }

    public TSBeaconSighting getBeacon() {
        return beacon;
    }
}
