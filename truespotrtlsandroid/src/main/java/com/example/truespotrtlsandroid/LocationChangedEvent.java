package com.example.truespotrtlsandroid;

import android.location.Location;
import android.os.SystemClock;

import com.example.truespotrtlsandroid.events.BaseEvent;

public class LocationChangedEvent extends BaseEvent {

    private final Location location;
    private final long time;

    public LocationChangedEvent(Location location) {
        this.location = location;
        this.time = SystemClock.elapsedRealtime();
    }

    public Location getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "LocationChangedEvent{" +
                "lat=" + location.getLatitude() +
                "lng=" + location.getLongitude() +
                '}';
    }
}
