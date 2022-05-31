package com.example.truespotrtlsandroid

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
import com.example.truespotrtlsandroid.beacon.BeaconRegion
import com.example.truespotrtlsandroid.models.Beacon
import com.example.truespotrtlsandroid.models.BeaconType
import com.example.truespotrtlsandroid.models.IBeacon
import com.google.gson.Gson

class BeaconManagers(context: Context, activity: Activity) {

    var btManager : BluetoothManager? = null
    var btAdapter : BluetoothAdapter? = null
    var btScanner : BluetoothLeScanner? = null
    var leScanCallback: ScanCallback? = null

    init {
        btManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager!!.adapter
        btScanner = btAdapter?.bluetoothLeScanner
        if (btAdapter != null && !btAdapter!!.isEnabled)
        {

        }

    }



    fun startMonitoring() {
         leScanCallback  = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                if (!result!!.device.name.isNullOrEmpty()){
                    val beaconType: BeaconType = result.scanRecord?.bytes?.let { getBeaconType(it) } ?: return
                    val beacon: Beacon? = buildBeacon(beaconType, result.device, result.rssi, result.scanRecord?.bytes)

                    Log.i("Log", "onScanResult--->Beacon->${result.device.name}---->beaconType->${beaconType}--->Beacon${Gson().toJson(beacon)}-->Location")
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
        }


        //val filters: ArrayList<ScanFilter> = ArrayList<ScanFilter>()
        //val uuid = UUID.randomUUID()
        /* val uuid = "U5C38DBDE567C4CCAB1DA40A8AD465656"
         val filter: ScanFilter = ScanFilter.Builder().setServiceUuid(ParcelUuid(UUID.fromString(uuid))).build()
         filters.add(filter)*/
        val settings: ScanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
        btScanner = btAdapter?.getBluetoothLeScanner()
        btScanner?.startScan(null, settings, leScanCallback)


        /* val settings = ScanSettings.Builder().setReportDelay(1000)
             .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()*/
        //   btScanner!!.startScan(leScanCallback)

    }

    fun stopMonitoring() {
        btScanner!!.stopScan(leScanCallback)
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
        str1 = str1.replace(" ", "").toLowerCase()
        str2 = str2.replace(" ", "").toLowerCase()
        str1 = str1.replace("-", "")
        str2 = str2.replace("-", "")
        return str1.contains(str2)
    }
}