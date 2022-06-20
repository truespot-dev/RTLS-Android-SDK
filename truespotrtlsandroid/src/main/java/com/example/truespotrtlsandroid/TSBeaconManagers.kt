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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object TSBeaconManagers {
    // private var beaconsObservable: NSObjectProtocol?
    private val shared = TSBeaconManagers
    private val beaconDetected = "beaconDetected"
    private val beaconRSSIUpdate = "beaconRSSIUpdate"
    var isGettingIdentifiers = false
    private var timer: Timer? = null
    var mBeacons: HashMap<String, TSBeacon> = HashMap()
    var mTrackingDevices: HashMap<String, TSDevice> = HashMap()
    var mReceiver: BroadcastReceiver? = null
    var stringBuilder: StringBuilder? = null

    init {
        stringBuilder = StringBuilder()
    }


    fun observeBeaconRanged(beaconMap : HashMap<String, TSBeacon>? ) : HashMap<String, TSBeacon>?{
        return beaconMap
    }

    fun observeBeaconRSSI(beacon :  TSBeacon) : String{
       return beacon.beaconIdentifier
    }


    fun initializeBeaconObserver(
        beaconUpdatedList: MutableList<TSBeaconSighting>,
        mCurrentLocation: Location
    ) {
        Log.e("init", "----")
        if(!beaconUpdatedList.isNullOrEmpty())
        {
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

    }


    private fun process(beacons: List<TSBeaconSighting>, currentLocation: CLLocation?) {
        for (TSBeaconSighting in beacons) {
            val beacon = TSBeacon(TSBeaconSighting, currentLocation)
            beacon.proximity = TSBeaconSighting.proximity
            var key = getKey(stringBuilder!!, beacon)
           /* if (key != null) {
                Log.i(
                    "savedBeacon",
                    "Get:${Gson().toJson(mBeacons!!.get(key))}--->Key${Gson().toJson(mBeacons!![key])}"
                )
                var sighting = mBeacons.get(key)
                if (sighting == null) {
                    mBeacons.put(key, beacon)
                } else {
                    sighting.update(beacon)
                }
            }*/
            var savedBeacon : TSBeacon? = null
            if(mBeacons[key] != null)
            {
                    savedBeacon = mBeacons[key]
                    if (savedBeacon != null) {
                        val savedRSSI = savedBeacon.RSSI
                        val rssi = beacon.RSSI
                        if(savedRSSI != null && rssi != null)
                        {
                            val beaconIdentifier = savedBeacon.beaconIdentifier
                            beacon.beaconIdentifier = beaconIdentifier
                            beacon.assetType = savedBeacon.assetType
                            beacon.assetIdentifier = savedBeacon.assetIdentifier
                        }

                        if (rssi != 0) {
                            if (rssi >= savedRSSI) {
                                mBeacons[key] = beacon
                            }
                            //Update modar if Beacon changes
                            if (rssi != savedRSSI) {

                                //       NotificationCenter.default.post(name: Notification.Name(beaconRSSIUpdate), object: beacon)
                            }
                        }
                    }
                }
            else {
                val device = mTrackingDevices.get(key)
                if (device != null) {
                    beacon.beaconIdentifier = device.tagIdentifier
                    beacon.assetType = device.assetType
                    beacon.assetIdentifier = device.assetIdentifier
                    mBeacons.put(key,beacon)
                    //RSSI update for Modar Mode
                    // NotificationCenter.default.post(name: Notification.Name(beaconRSSIUpdate), object: beacon)
                }
                else
                {
                    var sighting = mBeacons.get(key)
                    if (sighting == null) {
                        mBeacons.put(key, beacon)
                    } else {
                        sighting.update(beacon)
                    }
                }
            }
            observeBeaconRanged(getBeaconWithIdentifiers())
        }


    }

    /// This function generates a unique key for saving the beacons to the dictionary
    ///
    /// - Parameter beacon: the relevant beacon
    /// - Returns: return a unique key using the beaconid, the major and minor.

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
        val beacon = mBeacons["${UUID}-${minor}"]
        return beacon != null
    }

    fun getBeaconWithIdentifiers() : HashMap<String, TSBeacon>? {
        var beacons : HashMap<String, TSBeacon>? = HashMap()
        val itr = mBeacons.keys.iterator()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = mBeacons[key]
            if (value!!.beaconIdentifier != "" && value.beaconIdentifier != null) {
                mBeacons.get("${key}-${value}")
                beacons!![key] = value
            }
        }
        Log.i("getBeaconIdentifier", Gson().toJson(beacons))
        return beacons
    }

    fun updateTrackingDevices() : HashMap<String, TSDevice>? {
        var beacons : HashMap<String, TSDevice>? = null
        val itr = mBeacons.keys.iterator()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = mBeacons[key]
            if (value!!.beaconIdentifier != "" && value.beaconIdentifier != null) {
                mBeacons[key] = value
            }
        }

        return beacons
    }


    fun updateTrackingDevices(devices:  ArrayList<TSDevice>?) {
         devices?.forEach {
            val key = getKey(stringBuilder!!,it)
            mTrackingDevices[key] = it

        }
        Log.i("trackingDevice","-->${Gson().toJson(mTrackingDevices)}")
       /* val itr = devices!!.keys.iterator()
        while (itr.hasNext()) {
            val key = itr.next()

            val value = devices[key]
            val keys = getKey(stringBuilder!!,value!!)
            mTrackingDevices.put(keys,value)
        }*/
    }

}





