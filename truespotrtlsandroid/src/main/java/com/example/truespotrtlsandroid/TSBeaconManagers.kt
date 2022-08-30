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
    private var beaconsObservable: BroadcastReceiver? = null
    private const val beaconDetected = "beaconDetected"
    private const val beaconRSSIUpdate = "beaconRSSIUpdate"
    private var mBeacons: HashMap<String, TSBeacon> = HashMap()
    private var mTrackingDevices: HashMap<String, TSDevice> = HashMap()
    private var stringBuilder: StringBuilder? = null

    init {
        stringBuilder = StringBuilder()
    }
    fun observeBeaconRanged(context: Context,listener: (beacons: HashMap<String, TSBeacon>?)->Unit): BroadcastReceiver {
        val mBeaconsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null)
                {
                    listener.invoke(intent.getSerializableExtra(beaconDetected) as HashMap<String, TSBeacon>?)
                }
            }
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(mBeaconsReceiver, IntentFilter("notification"))
        return mBeaconsReceiver
    }

    fun observeBeaconRSSI(context: Context,listener: (beacons: HashMap<String, TSBeacon>?)->Unit): BroadcastReceiver {
        val mBeaconRSSIReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null)
                {
                    listener.invoke(intent!!.getSerializableExtra(beaconRSSIUpdate) as HashMap<String, TSBeacon>?)
                }
            }
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(mBeaconRSSIReceiver, IntentFilter("notification"))
        return mBeaconRSSIReceiver
    }


    fun initializeBeaconObserver() {
        beaconsObservable = TSLocationManager.observeBeaconRanged(TSApplicationContext.TSContext){
            if(it != null)
            {
                //val beacons = it.getSerializableExtra("beaconDetected") as HashMap<String, TSBeaconSighting>
                val beacons = it
                process(beacons)
            }

        }
        LocalBroadcastManager.getInstance(TSApplicationContext.TSContext).registerReceiver(beaconsObservable!!, IntentFilter("beaconRange"))
    }


    private fun process(beacons:HashMap<String, TSBeaconSighting>) {
        for (TSBeaconSighting in beacons) {
            val beacon = TSBeacon(TSBeaconSighting.value)
            beacon.proximity = beacon.proximity
            var key = stringBuilder?.let { getKey(it, beacon) }
            var savedBeacon: TSBeacon? = null
            if (mBeacons[key] != null) {
                savedBeacon = mBeacons[key]
                if (savedBeacon != null) {
                    val savedRSSI = savedBeacon.rssi
                    val rssi = beacon.rssi
                    if (savedRSSI != null && rssi != null) {
                        val beaconIdentifier = savedBeacon.beaconIdentifier
                        beacon.beaconIdentifier = beaconIdentifier
                        beacon.assetType = savedBeacon.assetType
                        beacon.assetIdentifier = savedBeacon.assetIdentifier
                    }

                    if (rssi != 0) {
                        if (rssi >= savedRSSI) {
                            mBeacons[key.toString()] = beacon
                        }
                        //Update modar if Beacon changes
                        if (rssi != savedRSSI) {
                            val intent = Intent("notification")
                            intent.putExtra(beaconRSSIUpdate, mBeacons)
                            LocalBroadcastManager.getInstance(TSApplicationContext.TSContext).sendBroadcast(intent)
                        }
                    }
                }
            } else {
                val device = mTrackingDevices[key]
                if (device != null) {
                    beacon.beaconIdentifier = device.tagIdentifier
                    beacon.assetType = device.assetType
                    beacon.assetIdentifier = device.assetIdentifier
                    if (key != null) {
                        mBeacons[key] = beacon
                    }
                    //RSSI update for Modar Mode
                    val intent = Intent("notification")
                    intent.putExtra(beaconRSSIUpdate, mBeacons)
                    LocalBroadcastManager.getInstance(TSApplicationContext.TSContext).sendBroadcast(intent)
                } else {
                    var sighting = mBeacons[key]
                    if (sighting == null) {
                        if (key != null) {
                            mBeacons[key] = beacon
                        }
                    } else {
                        sighting.update(beacon)
                    }
                }
            }
            val intent = Intent("notification")
            intent.putExtra(beaconDetected, getBeaconWithIdentifiers())
            LocalBroadcastManager.getInstance(TSApplicationContext.TSContext).sendBroadcast(intent)
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
            mTrackingDevices?.set(key.toString(),it)

        }
    }

}





