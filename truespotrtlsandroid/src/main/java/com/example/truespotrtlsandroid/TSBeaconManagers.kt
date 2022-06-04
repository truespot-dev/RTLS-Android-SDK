package com.example.truespotrtlsandroid

import android.app.Notification
import android.app.NotificationChannel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.truespotrtlsandroid.BeaconAPI.GSON
import com.example.truespotrtlsandroid.TSBeaconManagers.beaconRSSIUpdate
import com.example.truespotrtlsandroid.TSBeaconManagers.mReceiver
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.models.TSDevice
import com.google.gson.Gson
import timber.log.Timber
import java.util.*
import java.util.stream.DoubleStream.builder
import java.util.stream.IntStream.builder
import java.util.stream.Stream.builder

object TSBeaconManagers {
    // private var beaconsObservable: NSObjectProtocol?
    private val shared = TSBeaconManagers
    private val beaconDetected = "beaconDetected"
    private val beaconRSSIUpdate = "beaconRSSIUpdate"
    var isGettingIdentifiers = false
    private var timer: Timer? = null
    var mBeacons: Dictionary<String, TSBeacon>? = null
    var mTrackingDevices: Dictionary<String, TSDevice>? = null
    var mReceiver: BroadcastReceiver? = null
    var stringBuilder: StringBuilder? = null

    init {
        stringBuilder = StringBuilder()
    }


    fun observeBeaconRanged() {

    }

    fun observeBeaconRSSI() {

    }


    fun initializeBeaconObserver(
        beaconUpdatedList: MutableList<TSBeaconSighting>,
        mCurrentLocation: Location
    ) {
        Log.e("init", "----")
        for (beacons in beaconUpdatedList) {
            Log.e("init--", beacons.beaconId)
        }
        Log.e(
            "currentlocation-->",
            "lat: ${mCurrentLocation.latitude},log:${mCurrentLocation.longitude}"
        )
        /* val currentLocation : CLLocation? = null
        currentLocation!!.coordinate.latitude = mCurrentLocation.latitude.toString()
        currentLocation.coordinate.longitude = mCurrentLocation.longitude.toString()
        currentLocation.horizontalAccuracy = mCurrentLocation.accuracy.toString()*/
        process(
            beaconUpdatedList,
            CLLocation(
                Coordinate(
                    mCurrentLocation.latitude.toString(),
                    mCurrentLocation.longitude.toString()
                ), mCurrentLocation.accuracy.toString()
            )
        )
    }


    fun process(beacons: List<TSBeaconSighting>, currentLocation: CLLocation?) {
        for (TSBeaconSighting in beacons) {
            var beacon = TSBeacon(TSBeaconSighting, currentLocation)
            val key = getKey(stringBuilder!!, beacon)
            val savedBeacon = mBeacons!![key]
            var device = mTrackingDevices!![key]
            beacon.proximity = TSBeaconSighting.proximity
            if (key != null) {
                if (savedBeacon != null) {
                    var savedRSSI = savedBeacon.RSSI
                    var rssi = beacon.RSSI
                    var beaconIdentifier = savedBeacon.beaconIdentifier
                    beacon.beaconIdentifier = beaconIdentifier
                    beacon.assetType = savedBeacon.assetType
                    beacon.assetIdentifier = savedBeacon.assetIdentifier
                    if (rssi != null) {
                        if (rssi >= savedRSSI) {
                            beacon = mBeacons!![key]
                        }
                        if (rssi != savedRSSI) {

                            /* fun onReceive(context: Context, intent: Intent) {

                             }*/


                        }


//       NotificationCenter.default.post(name: Notification.Name(beaconRSSIUpdate), object: beacon)
                    }
                } else {
                    if (device != null) {
                        beacon.beaconIdentifier = device.tagIdentifier
                        beacon.assetType = device.assetType
                        beacon.assetIdentifier = device.assetIdentifier
                        beacon = mBeacons!![key]

//       NotificationCenter.default.post(Notification.Name(beaconRSSIUpdate))
                    }
                }
//     NotificationCenter.default.post(Notification.Name(beaconDetected), object: getBeaconWithIdentifiers())


            }
        }


    }

    private fun getKey(sb : StringBuilder, beacon: TSBeacon) : String {
        sb.delete(0, sb.length)
        sb.append(beacon.uuid.lowercase())
        sb.append("-")
        sb.append(beacon.minor)
        if(sb.isNotEmpty())
        {
            return sb.toString()
        }
      return ""
    }
    private fun getKey(sb : StringBuilder, device: TSDevice) : String {
        sb.delete(0, sb.length)
        sb.append(device.uuid.lowercase().replace("-",""))
        sb.append("-")
        sb.append(device.minor)
        if(sb.isNotEmpty())
        {
            return sb.toString()
        }
       return  ""
    }

    fun beaconExists(UUID: String, minor: String) : Boolean {
        val beacon = mBeacons?.get("${UUID}-${minor}")
        return beacon != null
    }

    fun getBeaconWithIdentifiers() : Dictionary<String, TSBeacon>? {
        var beacons : Dictionary<String, TSBeacon>? = null
        val itr = mBeacons!!.keys().iterator()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = mBeacons!![key]
            if (value.beaconIdentifier != "" && value.beaconIdentifier != null) {
                mBeacons?.get("${key}-${value}")
            }
        }

        return beacons
    }

    fun updateTrackingDevices() : Dictionary<String, TSDevice>? {
        var beacons : Dictionary<String, TSDevice>? = null
        val itr = mBeacons!!.keys().iterator()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = mBeacons!![key]
            if (value.beaconIdentifier != "" && value.beaconIdentifier != null) {
                mBeacons?.put(key,value)
            }
        }

        return beacons
    }


    fun updateTrackingDevices(devices:  Dictionary<String, TSDevice>?) {

        val itr = devices!!.keys().iterator()
        while (itr.hasNext()) {
            val key = itr.next()

            val value = devices[key]
            val keys = getKey(stringBuilder!!,value)
            mTrackingDevices!!.put(keys,value)
        }
    }

}





