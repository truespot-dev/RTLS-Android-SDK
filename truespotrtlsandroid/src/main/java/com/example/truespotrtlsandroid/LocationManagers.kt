package com.example.truespotrtlsandroid

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.lang.StringBuilder
import java.text.DecimalFormat

class LocationManagers(context: Context, activity: Activity) : BaseLifeCycleObject() {


    var context: Context? = null
    var activity: Activity? = null
    var googleApiClient: GoogleApiClient? = null
    var connected: Boolean = false
    var listener: GoogleApiClientConnectionListener? = null
    var firstLocation: Location? = null
    var lastLocation: Location? = null
    var locationRequest: LocationRequest? = null
    var locationUpdateListener: LocationListenerImpl? = null
    var subscribedForLocationUpdates = false
    var formatter: DecimalFormat? = null
    var distMts: FloatArray? = null
    var delayedRunnable: Runnable? = null
    var handler: Handler? = null
    var mLogBuilder: StringBuilder? = null

    var handlerLocation: Handler? = null

    init {
        checkForLocationPermission(context, activity)
    }


    private fun checkForLocationPermission(context: Context, activity: Activity) {

        this.context = context
        this.activity = activity
        listener = GoogleApiClientConnectionListener()
        locationUpdateListener = LocationListenerImpl()
        formatter = DecimalFormat("000.00")
        distMts = FloatArray(1)
        delayedRunnable = Runnable { stopLocationUpdates() }
        handler = Handler()
        handlerLocation = Handler()
        mLogBuilder = StringBuilder()


        locationRequest = LocationRequest()
        locationRequest?.interval = Constant.Location.LOCATION_REQUEST_INTERVAL_MILLIS
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)
        if (resultCode == ConnectionResult.SUCCESS) {
            googleApiClient = GoogleApiClient.Builder(context).addApi(LocationServices.API)
                .addConnectionCallbacks(
                    listener!!
                ).build()
            googleApiClient?.connect()
        } else {

            //GooglePlayServicesUtil.getErrorDialog();
            Timber.e("PlayServices Not Available")
        }


        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (checkRequiredPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                context
            ) == true
        ) {
            // Permission Enable
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }


    }

    private fun checkRequiredPermission(permission: String, context: Context): Boolean? {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    /* var startRunnableLocationUpdates = Runnable {
         BatteryOptimizationUtil.readBatteryMode(context)
         Log.e(
             "startLocationUpdates",
             "Started for: " + BatteryOptimizationUtil.BEACON_SCANNING_PERIOD_MILLIS
         )
         startLocationUpdates()
         handlerLocation!!.postDelayed(stopRunnableLocationUpdates,BatteryOptimizationUtil.BEACON_SCANNING_PERIOD_MILLIS)
     }*/

    /* var stopRunnableLocationUpdates = Runnable {
         BatteryOptimizationUtil.readBatteryMode(context)
         Log.e(
             "stopLocationUpdates",
             "Stopped for: " + BatteryOptimizationUtil.BEACON_SCANNING_STOP_PERIOD_MILLIS
         )
         stopLocationUpdates()
         handlerLocation!!.postDelayed(startRunnableLocationUpdates,BatteryOptimizationUtil.BEACON_SCANNING_STOP_PERIOD_MILLIS)
     }
 */


    fun startLocationUpdates() {
        if (!subscribedForLocationUpdates) {
            val hasPermission = context?.let {
                checkRequiredPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    it
                )
            }!!
            Timber.i("startLocationUpdates() hasPermission:$hasPermission")
            if (hasPermission) {
                firstLocation = googleApiClient?.let {
                    LocationServices.FusedLocationApi.getLastLocation(
                        it
                    )
                }
                googleApiClient?.let {
                    locationRequest?.let { it1 ->
                        locationUpdateListener?.let { it2 ->
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                it,
                                it1, it2
                            )
                        }
                    }
                }
                subscribedForLocationUpdates = true
            } else {
                Timber.i("startLocationUpdates() appending need permission")
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }
            }
        }
    }

    fun stopLocationUpdates() {
        if (subscribedForLocationUpdates) {
            Timber.i("stopLocationUpdates()")
            googleApiClient?.let {
                locationUpdateListener?.let { it1 ->
                    LocationServices.FusedLocationApi.removeLocationUpdates(
                        it,
                        it1
                    )
                }
            }
            subscribedForLocationUpdates = false
        }
    }

    fun getLatLongMessage(loc: Location?): String? {
        mLogBuilder?.length?.let { mLogBuilder?.delete(0, it) }
        if (loc != null) {
            mLogBuilder?.append(
                String.format(
                    "lat/lng[%.06f, %.06f]",
                    loc.latitude,
                    loc.longitude
                )
            )
            mLogBuilder?.append("acc[" + formatter?.format(loc.accuracy.toDouble()) + " mts]")
            if (firstLocation != null) {
                firstLocation?.longitude?.let {
                    firstLocation?.latitude?.let { it1 ->
                        Location.distanceBetween(
                            it1,
                            it,
                            loc.latitude,
                            loc.longitude,
                            distMts
                        )
                    }
                }
                mLogBuilder?.append("dist[" + formatter?.format(distMts?.get(0)) + " mts]")
            }
            if (lastLocation != null) {
                lastLocation?.latitude?.let {
                    lastLocation?.longitude?.let { it1 ->
                        Location.distanceBetween(
                            it,
                            it1,
                            loc.latitude,
                            loc.longitude,
                            distMts
                        )
                    }
                }
                mLogBuilder?.append("delta[" + formatter?.format(distMts?.get(0)) + " mts]")
            }
        }
        return mLogBuilder.toString()
    }

    fun getLastKnownLocation(): Location? {
        var location = if (lastLocation == null) firstLocation else lastLocation
        // If we don't have a valid last known location from LocationServices.FusedLocationApi,
        // just pick up the first one from active providers
        if (location == null) {
            if (context?.let {
                    checkRequiredPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        it
                    )
                }!!) {
                val lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val providers = lm.getProviders(true)
                for (provider in providers) {
                    location = lm.getLastKnownLocation(provider!!)
                    if (location != null) {
                        break
                    }
                }
            }
        }
        return location
    }

    override fun onCreate() {
        Timber.i("onCreate()")
        locationRequest = LocationRequest()
        locationRequest?.interval = Constant.Location.LOCATION_REQUEST_INTERVAL_MILLIS
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val resultCode = context?.let { GooglePlayServicesUtil.isGooglePlayServicesAvailable(it) }
        if (resultCode == ConnectionResult.SUCCESS) {
            googleApiClient = context?.let {
                listener?.let { it1 ->
                    GoogleApiClient.Builder(it).addApi(LocationServices.API)
                        .addConnectionCallbacks(
                            it1
                        ).build()
                }
            }
            googleApiClient?.connect()
        } else {

            //GooglePlayServicesUtil.getErrorDialog();
            Timber.e("PlayServices Not Available")
        }
    }

    override fun onResume() {
        Timber.i("onResume()")
        delayedRunnable?.let { handler?.removeCallbacks(it) }
        if (googleApiClient != null && googleApiClient!!.isConnected && !subscribedForLocationUpdates) {
//             handlerLocation?.post(startRunnableLocationUpdates)
        }
    }

    override fun onPause() {
        Timber.i("onPause()")
        delayedRunnable?.let {
            handler?.postDelayed(
                it,
                Constant.Location.LOCATION_UPDATE_PAUSE_TIMEOUT_MILLIS
            )
        }
        handlerLocation?.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        Timber.i("onDestroy()")

        stopLocationUpdates()
        if (connected) {
            Timber.i("onDestroy() googleApiClient.disconnect...")
            googleApiClient?.disconnect()
        }
        connected = false
        firstLocation = null
        lastLocation = null
        handlerLocation?.removeCallbacksAndMessages(null)
    }

    class GoogleApiClientConnectionListener : GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
        override fun onConnected(bundle: Bundle?) {

            //connected = true
            Timber.i("onConnected()")
            // handlerLocation.post(startRunnableLocationUpdates)
        }

        override fun onConnectionSuspended(i: Int) {
            Timber.i("onConnectionSuspended()")
        }

        override fun onConnectionFailed(result: ConnectionResult) {
            Timber.i("onConnectionFailed()")
        }
    }

    class LocationListenerImpl : LocationListener {
        override fun onLocationChanged(loc: Location) {
            // Timber.d("onLocationChanged() " + getLatLongMessage(loc))
            // lastLocation = loc

        }
    }


}