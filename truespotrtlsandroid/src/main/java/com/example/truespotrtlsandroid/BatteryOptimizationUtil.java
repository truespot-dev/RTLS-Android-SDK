package com.example.truespotrtlsandroid;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.concurrent.TimeUnit;

public class BatteryOptimizationUtil {

    public static long BEACON_SCANNING_PERIOD_MILLIS = 1000L;
    public static long BEACON_SCANNING_STOP_PERIOD_MILLIS = 1000L;
    public static long PUSH_VISITS_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(30);
  //  public static BeaconManager.BatteryMode BATTERY_MODE = BeaconManager.BatteryMode.CONTINUOUS;

    public static void readBatteryMode(Context context) {

        boolean continuous = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.key_frequency_continuous), true);

        boolean normal = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.key_frequency_normal), false);

        boolean batterySaver = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.key_frequency_battery_saver), false);

        if(continuous){
            BEACON_SCANNING_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(86400);
            BEACON_SCANNING_STOP_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(10);
            PUSH_VISITS_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(30);
         //   BATTERY_MODE = BeaconManager.BatteryMode.CONTINUOUS;
        }else if(normal){
            BEACON_SCANNING_PERIOD_MILLIS = TimeUnit.MINUTES.toMillis(1);
            BEACON_SCANNING_STOP_PERIOD_MILLIS = TimeUnit.MINUTES.toMillis(5);
            PUSH_VISITS_PERIOD_MILLIS = TimeUnit.MINUTES.toMillis(6);
          //  BATTERY_MODE = BeaconManager.BatteryMode.NORMAL;
        }else if(batterySaver){
            BEACON_SCANNING_PERIOD_MILLIS = TimeUnit.MINUTES.toMillis(1);
            BEACON_SCANNING_STOP_PERIOD_MILLIS = TimeUnit.MINUTES.toMillis(30);
            PUSH_VISITS_PERIOD_MILLIS = TimeUnit.MINUTES.toMillis(31);
          //  BATTERY_MODE = BeaconManager.BatteryMode.BATTERY_SAVER;
        }

    }
}
