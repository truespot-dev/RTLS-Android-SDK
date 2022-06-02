package com.example.truespotrtlsandroid

import android.app.Notification
import android.app.NotificationChannel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import androidx.core.app.NotificationCompat
import com.example.truespotrtlsandroid.TSBeaconManagers.beaconRSSIUpdate
import com.example.truespotrtlsandroid.TSBeaconManagers.mReceiver
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.models.TSDevice
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
    var beacons : Dictionary<String,TSBeacon>? = null
    var trackingDevices  : Dictionary<String,TSDevice>? = null
    var mReceiver : BroadcastReceiver? = null

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


 fun process(beacons: List<TSBeaconSighting>, currentLocation: CLLocation?) {
  for(TSBeaconSighting in beacons){

  var beacon = TSBeacon(TSBeaconSighting,currentLocation )
   val key =getKey(beacon)
   val savedBeacon = shared.beacons!![key]
   var device =trackingDevices!![key]
   beacon.proximity  = TSBeaconSighting.proximity
   if(key != null) {
    if (savedBeacon != null){
    var savedRSSI = savedBeacon.RSSI
    var rssi = beacon.RSSI
    var beaconIdentifier = savedBeacon.beaconIdentifier
    beacon.beaconIdentifier = beaconIdentifier
    beacon.assetType = savedBeacon.assetType
    beacon.assetIdentifier = savedBeacon.assetIdentifier
     if(rssi != null){
      if(rssi >= savedRSSI){
       beacon = shared.beacons!![key]
      }
      if(rssi != savedRSSI){

      /* fun onReceive(context: Context, intent: Intent) {

       }*/


      }


//       NotificationCenter.default.post(name: Notification.Name(beaconRSSIUpdate), object: beacon)
      }
     }else{
      if(device != null){
       beacon.beaconIdentifier = device.tagIdentifier
       beacon.assetType = device.assetType
       beacon.assetIdentifier = device.assetIdentifier
       beacon = TSBeaconManagers.shared.beacons!![key]

//       NotificationCenter.default.post(Notification.Name(beaconRSSIUpdate))
      }
     }
//     NotificationCenter.default.post(Notification.Name(beaconDetected), object: getBeaconWithIdentifiers())




    }
   }



  }
 }


 fun getKey(beacon: TSBeacon){
  var UUID = beacon.beaconId
  var minor = beacon.minor
if(UUID != null && minor != minor){
 var minorValue = minor
 if(minor.startsWith("0")){
//  var start =minorValue.indexOf(minorValue.
 }
}
 }

