package com.example.truespotrtlsandroid;

public class Constant {

    public static class Location {
        public static final int GPS_REQUEST = 5467;
        public static final long LOCATION_REQUEST_INTERVAL_MILLIS = 5 * 1000;
        public static final long LOCATION_UPDATE_PAUSE_TIMEOUT_MILLIS = 2000;
    }

    public class BeaconManager {
        //public static final int MIN_RSSI_DELTA_THRESHOLD = 5;
        public static final int MIN_RSSI_DELTA_THRESHOLD = 0; // For now ignore threshold
        public static final long BEACON_LISTENER_PAUSE_WAIT_MILLIS = 5000;
        public static final long BEACON_LISTING_WAIT_MILLIS = 5000;

        public static final long BEACON_SCANNING_PERIOD_MILLIS = 10000L;
        public static final long BEACON_SCANNING_STOP_PERIOD_MILLIS = 10000L;
    }
}
