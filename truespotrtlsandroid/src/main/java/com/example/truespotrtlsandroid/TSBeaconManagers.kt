package com.example.truespotrtlsandroid

import android.app.NotificationChannel
import com.example.truespotrtlsandroid.models.TSDevice
import java.util.*

object TSBeaconManagers {
   // private var beaconsObservable: NSObjectProtocol?
    private val beaconDetected = "beaconDetected"
    private val beaconRSSIUpdate = "beaconRSSIUpdate"
    var isGettingIdentifiers = false
    private var timer: Timer? = null
    var beacons : Dictionary<String,TSBeacon>? = null
    var trackingDevices  : Dictionary<String,TSDevice>? = null

    init {

    }



 fun observeBeaconRanged()
 {

 }

 fun observeBeaconRSSI()
 {

 }


 fun initializeBeaconObserver()
 {

 }

 fun process()
 {

 }

}