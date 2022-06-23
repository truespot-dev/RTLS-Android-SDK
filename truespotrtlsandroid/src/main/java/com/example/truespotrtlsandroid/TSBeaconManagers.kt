package com.example.truespotrtlsandroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.models.TSDevice
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object TSBeaconManagers {
    private val shared = TSBeaconManagers
    private val beaconDetected = "beaconDetected"
    private val beaconRSSIUpdate = "beaconRSSIUpdate"
    var isGettingIdentifiers = false
    private var timer: Timer? = null
    var mBeacons: HashMap<String, TSBeacon> = HashMap()
    var mTrackingDevices: HashMap<String, TSDevice> = HashMap()
    var stringBuilder: StringBuilder? = null

    init {
        stringBuilder = StringBuilder()
    }
    fun observeBeaconRanged(listener: (beacons: MutableList<TSBeacon>)->Unit,context: Context): BroadcastReceiver {
        val mBeaconsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                listener.invoke(intent!!.getSerializableExtra(beaconDetected) as MutableList<TSBeacon> )
            }
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(mBeaconsReceiver, IntentFilter("notification"))
        return mBeaconsReceiver
    }

    fun observeBeaconRSSI(context: Context,listener: (beacons: MutableList<TSBeacon>)->Unit): BroadcastReceiver {
        val mBeaconRSSIReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                listener.invoke(intent!!.getSerializableExtra(beaconRSSIUpdate) as MutableList<TSBeacon> )
            }
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(mBeaconRSSIReceiver, IntentFilter("notification"))
        return mBeaconRSSIReceiver
    }


    fun initializeBeaconObserver(mContext: Context,
        beaconUpdatedList: MutableList<TSBeaconSighting>,
        mCurrentLocation: Location
    ) {
        Log.e("init", "----")
        if (!beaconUpdatedList.isNullOrEmpty()) {
            for (beacons in beaconUpdatedList) {
                Log.e("init--", beacons.beaconId)
            }
            Log.e(
                "currentlocation-->",
                "lat: ${mCurrentLocation.latitude},log:${mCurrentLocation.longitude}"
            )
            process(
                beaconUpdatedList,
                CLLocation(
                    Coordinate(
                        mCurrentLocation.latitude.toString(),
                        mCurrentLocation.longitude.toString()), mCurrentLocation.accuracy.toString())
                       , mContext)
        }

    }


    private fun process(beacons: List<TSBeaconSighting>, currentLocation: CLLocation?,mContext: Context) {
        for (TSBeaconSighting in beacons) {
            val beacon = TSBeacon(TSBeaconSighting, currentLocation)
            beacon.proximity = TSBeaconSighting.proximity
            var key = stringBuilder?.let { getKey(it, beacon) }
            var savedBeacon: TSBeacon? = null
            if (mBeacons[key] != null) {
                savedBeacon = mBeacons[key]
                if (savedBeacon != null) {
                    val savedRSSI = savedBeacon.RSSI
                    val rssi = beacon.RSSI
                    if (savedRSSI != null && rssi != null) {
                        val beaconIdentifier = savedBeacon.beaconIdentifier
                        beacon.beaconIdentifier = beaconIdentifier
                        beacon.assetType = savedBeacon.assetType
                        beacon.assetIdentifier = savedBeacon.assetIdentifier
                    }

                    if (rssi != 0) {
                        if (rssi >= savedRSSI) {
//                            mBeacons[key] = beacon
                            mBeacons?.set(key.toString(),beacon)
                        }
                        //Update modar if Beacon changes
                        if (rssi != savedRSSI) {
                            val intent = Intent("notification")
                            intent.putExtra(beaconRSSIUpdate, mBeacons)
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
                        }
                    }
                }
            } else {
                val device = mTrackingDevices.get(key)
                if (device != null) {
                    beacon.beaconIdentifier = device.tagIdentifier
                    beacon.assetType = device.assetType
                    beacon.assetIdentifier = device.assetIdentifier
                    if (key != null) {
                        mBeacons.put(key, beacon)
                    }
                    //RSSI update for Modar Mode
                    val intent = Intent("notification")
                    intent.putExtra(beaconRSSIUpdate, mBeacons)
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
                } else {
                    var sighting = mBeacons.get(key)
                    if (sighting == null) {
                        if (key != null) {
                            mBeacons.put(key, beacon)
                        }
                    } else {
                        sighting.update(beacon)
                    }
                }
            }
            val intent = Intent("notification")
            intent.putExtra(beaconDetected, getBeaconWithIdentifiers())
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
        }


    }

    /// This function generates a unique key for saving the beacons to the dictionary
    ///
    /// - Parameter beacon: the relevant beacon
    /// - Returns: return a unique key using the beaconid, the major and minor.

    private fun getKey(sb: StringBuilder, beacon: TSBeacon): String {
        sb.delete(0, sb.length)
        sb.append(beacon.uuid.lowercase())
        sb.append("-")
        sb.append(beacon.minor)
        if (sb.isNotEmpty()) {
            return sb.toString()
        }
        return ""
    }

    private fun getKey(sb: StringBuilder, device: TSDevice): String {
        sb.delete(0, sb.length)
        sb.append(device.uuid.lowercase().replace("-", ""))
        sb.append("-")
        sb.append(device.minor)
        if (sb.isNotEmpty()) {
            return sb.toString()
        }
        return ""
    }

    fun beaconExists(UUID: String, minor: String): Boolean {
        val beacon = mBeacons["${UUID}-${minor}"]
        return beacon != null
    }

    fun getBeaconWithIdentifiers(): HashMap<String, TSBeacon>? {
        var beacons: HashMap<String, TSBeacon>? = HashMap()
        val itr = mBeacons.keys.iterator()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = mBeacons[key]
            if (value?.beaconIdentifier != "" && value?.beaconIdentifier != null) {
                mBeacons.get("${key}-${value}")
                beacons?.set(key, value)
            }
        }
        Log.i("getBeaconIdentifier", Gson().toJson(beacons))
        return beacons
    }

    fun updateTrackingDevices(): HashMap<String, TSDevice>? {
        var beacons: HashMap<String, TSDevice>? = null
        val itr = mBeacons.keys.iterator()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = mBeacons[key]
            if (value?.beaconIdentifier != "" && value?.beaconIdentifier != null) {
                mBeacons[key] = value
            }
        }

        return beacons
    }


    fun updateTrackingDevices(devices: ArrayList<TSDevice>?) {
        devices?.forEach {
            val key = stringBuilder?.let { it1 -> getKey(it1, it) }
//            mTrackingDevices[key] = it
            mTrackingDevices?.set(key.toString(),it)

        }
        Log.i("trackingDevice", "-->${Gson().toJson(mTrackingDevices)}")
        /* val itr = devices!!.keys.iterator()
         while (itr.hasNext()) {
             val key = itr.next()

             val value = devices[key]
             val keys = getKey(stringBuilder!!,value!!)
             mTrackingDevices.put(keys,value)
         }*/
    }

}





