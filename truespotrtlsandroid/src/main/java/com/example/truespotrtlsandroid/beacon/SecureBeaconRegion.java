package com.example.truespotrtlsandroid.beacon;

import java.util.UUID;

public class SecureBeaconRegion extends BeaconRegion {
    public SecureBeaconRegion(String identifier, UUID proximityUUID, Integer major, Integer minor) {
        super(identifier, proximityUUID, major, minor);
    }
}
