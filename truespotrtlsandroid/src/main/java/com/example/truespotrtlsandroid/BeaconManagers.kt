package com.example.truespotrtlsandroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import com.example.truespotrtlsandroid.models.Beacon
import com.example.truespotrtlsandroid.models.BeaconType
import com.example.truespotrtlsandroid.models.IBeacon
import com.google.gson.Gson
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings.*
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


object BeaconManagers : ScanCallback(),LocationListener {

    private var btManager: BluetoothManager? = null
    private var btAdapter: BluetoothAdapter? = null
    private var btScanner: BluetoothLeScanner? = null
    private var mLocation: Location? = null
    private var scanning = false
    private var iBeaconsMap : HashMap<String, TSBeacon>? = HashMap()
    var beaconUpdatedList: MutableList<TSBeaconSighting>? = ArrayList()
    @SuppressLint("StaticFieldLeak")
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationManager: LocationManager = TSApplicationContext.TSContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager



    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(TSApplicationContext.TSContext)
        btManager = TSApplicationContext.TSContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager?.adapter
        btScanner = btAdapter?.bluetoothLeScanner
        if (btAdapter != null && !btAdapter!!.isEnabled)
            scanning = false


    }

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        if (!result?.device?.name.isNullOrEmpty()) {
            val beaconType: BeaconType =
                result?.scanRecord?.bytes?.let { getBeaconType(it) } ?: return
            val beacon: Beacon? =
                buildBeacon(beaconType, result.device, result.rssi, result.scanRecord?.bytes)
            val beaconList: MutableList<TSBeaconSighting> = ArrayList()
            val getCurrentLocation = getCurrentLocation()
            val sighting: TSBeacon? = iBeaconsMap?.get(result.device.name)
            if(sighting == null)
            {
                if (beacon != null && getCurrentLocation != null)
                {
                    iBeaconsMap?.put(result.device.name,
                        TSBeacon(TSBeaconSighting(beacon.device?.name,beacon.rssi,beacon.device?.address,IBeacon(beacon).uuid,
                            IBeacon(beacon).major,
                            IBeacon(beacon).minor,
                            getCurrentLocation?.latitude!!,
                            getCurrentLocation.longitude,
                            getCurrentLocation.accuracy))
                    )
                }
                if(iBeaconsMap?.size!! > 0){
                    TSLocationManager.locationManager(iBeaconsMap)
                    TSBeaconManagers.initializeBeaconObserver()
                }
            }
           /* if (!beaconList.isNullOrEmpty()) {
                beaconList.forEach {
                    if (!beaconUpdatedList?.contains(it)!!) {
                        beaconUpdatedList?.add(it)
                        val getBeaconList = getBeaconSightings()
                        val getCurrentLocation = getCurrentLocation()
                        if (!getBeaconList.isNullOrEmpty() && getCurrentLocation != null) {
                            TSBeaconManagers.initializeBeaconObserver(getBeaconList, getCurrentLocation)
                        }
                    }
                }
                if (!beaconUpdatedList!!.isNullOrEmpty()) {
                    beaconList.forEach { result ->
                        beaconUpdatedList?.forEach {
                            if (!beaconUpdatedList!!.contains(it)) {
                                if (it.beaconId.equals(result.beaconId)) {
                                    if (it.rssi != result.rssi) {
                                        beaconUpdatedList?.remove(it)
                                        beaconUpdatedList!![beaconUpdatedList!!.indexOf(it)] =
                                            result

                                    }
                                }
                            }
                        }
                    }
                }
            }
            Log.i("Log", "beaconList-->${Gson().toJson(beaconUpdatedList)}")*/
        }
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        super.onBatchScanResults(results)
        Log.i("Log", "onBatchScanResults")
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        Log.i("Log", "onScanFailed")
    }

    fun startMonitoring() {
        val filters: ArrayList<ScanFilter> = ArrayList<ScanFilter>()
        val settings: ScanSettings = Builder()
            .setScanMode(SCAN_MODE_LOW_LATENCY)
            .build()


       /* val settings: ScanSettings = ScanSettings.Builder()
            .setCallbackType(CALLBACK_TYPE_ALL_MATCHES)
            .setLegacy(false)
            .setMatchMode(MATCH_MODE_AGGRESSIVE)
            .setNumOfMatches(MATCH_NUM_MAX_ADVERTISEMENT)
            .setPhy(PHY_LE_ALL_SUPPORTED)
            .setReportDelay(0)
            .setScanMode(SCAN_MODE_LOW_LATENCY)
            .build()*/
        btScanner = btAdapter?.bluetoothLeScanner
        btScanner?.startScan(null,settings,this)
        scanning = true
    }

    fun stopMonitoring() {
        btScanner?.stopScan(this)
        scanning = false
        Log.i("Log", "stopScan")
    }

    private fun buildBeacon(
        beaconType: BeaconType?,
        device: BluetoothDevice?,
        rssi: Int,
        scanRecord: ByteArray?
    ): Beacon? {
        val beacon = Beacon()
        beacon.rssi = rssi
        beacon.scanRecord = scanRecord
        beacon.device = device
        if (beaconType != null) {
            beacon.beaconType = beaconType
            when (beaconType) {
                BeaconType.I_BEACON -> {
                    return IBeacon(beacon)
                }

            }
        }
        return null
    }

    private fun getBeaconType(scanRecord: ByteArray): BeaconType? {
        val hexScanRecord = Beacon.bytesToHex(scanRecord)
        return if (this.containsString(hexScanRecord, BeaconType.I_BEACON.stringType)) {
            BeaconType.I_BEACON
        } else if (this.containsString(hexScanRecord, BeaconType.EDDYSTONE_TLM.stringType)) {
            BeaconType.EDDYSTONE_TLM
        } else {
            if (this.containsString(
                    hexScanRecord,
                    BeaconType.EDDYSTONE_UID.stringType
                )
            ) BeaconType.EDDYSTONE_UID else null
        }
    }

    private fun containsString(str1: String, str2: String): Boolean {
        var str1 = str1
        var str2 = str2
        str1 = str1.replace(" ", "").lowercase(Locale.getDefault())
        str2 = str2.replace(" ", "").lowercase(Locale.getDefault())
        str1 = str1.replace("-", "")
        str2 = str2.replace("-", "")
        return str1.contains(str2)
    }

    private fun getBeaconSightings(): MutableList<TSBeaconSighting> {

        val result: MutableList<TSBeaconSighting> = ArrayList()
        val values: Collection<TSBeaconSighting> = beaconUpdatedList!!
        if (values.isNotEmpty()) {
            for (s in values) {
                if (s.beaconId != null && !TextUtils.isEmpty(s.getBeaconId())) {
                    result.add(TSBeaconSighting(s))
                }
            }
        }
        return result
    }

       private fun getCurrentLocation(): Location? {
           if(checkRequiredPermission(Manifest.permission.ACCESS_FINE_LOCATION,TSApplicationContext.TSContext)!!)
           {
               if(isLocationEnabled())
               {
                   fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(TSApplicationContext.TSContext as Activity){task->
                       task.addOnSuccessListener { locations ->
                           if(locations != null)
                           {
                               Log.i("location","location----${Gson().toJson(locations)}")
                               if (locations != null) {
                                   mLocation = locations
                               }
                           }
                       }
                   }
               }
           }
        /*if (TSApplicationContext.TSContext?.let { checkRequiredPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION, it) }!!) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient?.lastLocation?.addOnCompleteListener { task ->
                    val location: Location = task.result
                    if (location != null) {
                        mLocation = task.result
                    }

                }
            } else {
                Toast.makeText(TSApplicationContext.TSContext, "Turn On Location", Toast.LENGTH_LONG).show()
            }
        } else {

        }*/
        return mLocation
    }



    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = TSApplicationContext.TSContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return true
        }
        else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return  true
        }
        return false
    }

    private fun checkRequiredPermission(permission: String, context: Context): Boolean? {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }


}