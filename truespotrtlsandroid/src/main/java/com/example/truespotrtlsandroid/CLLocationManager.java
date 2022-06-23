package com.example.truespotrtlsandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.otto.Bus;

import java.text.DecimalFormat;
import java.util.List;

import timber.log.Timber;

public class CLLocationManager extends BaseLifeCycleObject {

    private final Context context;
    private final Activity activity;
    protected GoogleApiClient googleApiClient;
    private boolean connected;
    private GoogleApiClientConnectionListener listener;
    private Location firstLocation;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private LocationListenerImpl locationUpdateListener;
    private boolean subscribedForLocationUpdates;
    private final DecimalFormat formatter;
    private final float[] distMts;
    private final Runnable delayedRunnable;
    private final Handler handler;
    private final StringBuilder mLogBuilder;

    private Handler handlerLocation;


    public CLLocationManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        listener = new GoogleApiClientConnectionListener();
        locationUpdateListener = new LocationListenerImpl();
        formatter = new DecimalFormat("000.00");
        distMts = new float[1];
        delayedRunnable = new Runnable() {
            @Override
            public void run() {
                stopLocationUpdates();
            }
        };
        handler = new Handler();
        handlerLocation = new Handler();
        mLogBuilder = new StringBuilder();

        if (checkRequiredPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Permission Enable
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }


    public Location getLastKnownLocation() {
        Location location = lastLocation == null ? firstLocation : lastLocation;
        // If we don't have a valid last known location from LocationServices.FusedLocationApi,
        // just pick up the first one from active providers
        if (location == null) {
            if (checkRequiredPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                final LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                final List<String> providers = lm.getProviders(true);
                for (String provider : providers) {
                    location = lm.getLastKnownLocation(provider);
                    if (location != null) {
                        break;
                    }
                }
            }
        }
        return location;
    }


    @Override
    protected void onCreate() {
        Timber.i("onCreate()");

        locationRequest = new LocationRequest();
        locationRequest.setInterval(Constant.Location.LOCATION_REQUEST_INTERVAL_MILLIS);
//        locationRequest.setFastestInterval(Constants.Location.LOCATION_REQUEST_FASTEST_INTERVAL_MILLIS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // No need to check whether the Location services is enabled.
        // Even if it is disabled, when user changes setting, we will start receiving location
        // updates.
        //if (LocationUtil.isLocationEnabled(context)) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode == ConnectionResult.SUCCESS) {
            googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(listener).build();
            googleApiClient.connect();
        } else {

            //GooglePlayServicesUtil.getErrorDialog();
            Timber.e("PlayServices Not Available");
        }
//        } else {
//
//            Timber.e("Location mode is disabled");
//        }


    }

    @Override
    protected void onResume() {
        Timber.i("onResume()");
        handler.removeCallbacks(delayedRunnable);
        if ((googleApiClient != null) && googleApiClient.isConnected() && !subscribedForLocationUpdates) {
            handlerLocation.post(startRunnableLocationUpdates);
        }
    }

    @Override
    protected void onPause() {
        Timber.i("onPause()");
        handler.postDelayed(delayedRunnable, Constant.Location.LOCATION_UPDATE_PAUSE_TIMEOUT_MILLIS);
        handlerLocation.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        Timber.i("onDestroy()");

        stopLocationUpdates();
        if (connected) {
            Timber.i("onDestroy() googleApiClient.disconnect...");
            googleApiClient.disconnect();
        }
        connected = false;
        firstLocation = null;
        lastLocation = null;
        handlerLocation.removeCallbacksAndMessages(null);
    }

    public void startLocationUpdates() {
        if (!subscribedForLocationUpdates) {
            final boolean hasPermission = checkRequiredPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            Timber.i("startLocationUpdates() hasPermission:" + hasPermission);
            if (hasPermission) {
                firstLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationUpdateListener);
                subscribedForLocationUpdates = true;
            } else {
                Timber.i("startLocationUpdates() appending need permission");
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    public void stopLocationUpdates() {
        if (subscribedForLocationUpdates) {
            Timber.i("stopLocationUpdates()");
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationUpdateListener);
            subscribedForLocationUpdates = false;
        }
    }

    public String getLatLongMessage(Location loc) {
        mLogBuilder.delete(0, mLogBuilder.length());
        if (loc != null) {
            mLogBuilder.append(String.format("lat/lng[%.06f, %.06f]", loc.getLatitude(), loc.getLongitude()));
            mLogBuilder.append("acc[" + formatter.format(loc.getAccuracy()) + " mts]");
            if (firstLocation != null) {
                Location.distanceBetween(firstLocation.getLatitude(), firstLocation.getLongitude(), loc.getLatitude(), loc.getLongitude(), distMts);
                mLogBuilder.append("dist[" + formatter.format(distMts[0]) + " mts]");
            }
            if (lastLocation != null) {
                Location.distanceBetween(lastLocation.getLatitude(), lastLocation.getLongitude(), loc.getLatitude(), loc.getLongitude(), distMts);
                mLogBuilder.append("delta[" + formatter.format(distMts[0]) + " mts]");
            }
        }
        return mLogBuilder.toString();
    }


    public Boolean checkRequiredPermission(String permission) {

        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    private class GoogleApiClientConnectionListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnected(Bundle bundle) {
            connected = true;
            Timber.i("onConnected()");
            handlerLocation.post(startRunnableLocationUpdates);
        }

        @Override
        public void onConnectionSuspended(int i) {
            Timber.i("onConnectionSuspended()");
        }

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Timber.i("onConnectionFailed()");
        }
    }

    private class LocationListenerImpl implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
//            if (!loc.hasAccuracy() || (loc.getAccuracy() > Constants.Location.LOCATION_ACCURACY_TOLERANCE_METERS)) {
//                // Don't show user a message
//                //bus.post(new MoLoCarError(ErrorType.GENERAL, "Insufficient location accuracy"));
//                Timber.e("onLocationChanged() hasAccuracy:" + loc.hasAccuracy() + ", accuracy:" + loc.getAccuracy());
//                // don't return
//                //return;
//            }
            // Check if the distance between the new location reported and last known location has
            // exceeded location changed tolerance, if yes process...
//            distMts[0] = Integer.MAX_VALUE;
//            if (lastLocation != null) {
//                Location.distanceBetween(lastLocation.getLatitude(), lastLocation.getLongitude(), loc.getLatitude(), loc.getLongitude(), distMts);
//            }
//            if (distMts[0] >= Constants.Location.LOCATION_CHANGED_TOLERANCE_METERS) {
            Timber.d("onLocationChanged() " + getLatLongMessage(loc));
            lastLocation = loc;

//            }
        }
    }

    private Runnable stopRunnableLocationUpdates = new Runnable() {
        @Override
        public void run() {
            BatteryOptimizationUtil.readBatteryMode(context);
            Log.e("stopLocationUpdates", "Stopped for: " + BatteryOptimizationUtil.BEACON_SCANNING_STOP_PERIOD_MILLIS);
            stopLocationUpdates();
            handlerLocation.postDelayed(startRunnableLocationUpdates, BatteryOptimizationUtil.BEACON_SCANNING_STOP_PERIOD_MILLIS);
        }
    };

    private Runnable startRunnableLocationUpdates = new Runnable() {
        @Override
        public void run() {
            BatteryOptimizationUtil.readBatteryMode(context);
            Log.e("startLocationUpdates", "Started for: " + BatteryOptimizationUtil.BEACON_SCANNING_PERIOD_MILLIS);
            startLocationUpdates();
            handlerLocation.postDelayed(stopRunnableLocationUpdates, BatteryOptimizationUtil.BEACON_SCANNING_PERIOD_MILLIS);
        }
    };

}
