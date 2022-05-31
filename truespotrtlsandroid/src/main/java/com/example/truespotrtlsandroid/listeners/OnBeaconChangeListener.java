package com.example.truespotrtlsandroid.listeners;


import com.example.truespotrtlsandroid.models.Beacon;
import com.example.truespotrtlsandroid.models.Range;

public interface OnBeaconChangeListener {
    void onRssiChanged(Beacon var1, int var2);

    void onRangeChanged(Beacon var1, Range var2);

    void onBeaconExit(Beacon var1);

    void onBeaconEnter(Beacon var1);
}
