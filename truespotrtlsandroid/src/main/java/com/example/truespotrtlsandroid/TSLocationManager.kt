package com.example.truespotrtlsandroid

import android.app.Activity
import android.content.*
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.provider.Settings
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.truespotrtlsandroid.beacon.BeaconRegion
import com.example.truespotrtlsandroid.models.Credentials
import com.google.android.gms.common.api.GoogleApiClient
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


object TSLocationManager  {

    private var beaconRangeNotificationName = "beaconRange"
    private const val beaconUUID: String = "5C38DBDE-567C-4CCA-B1DA-40A8AD465656"
    val beaconUUIDs = arrayOf("5C38DBDE-567C-4CCA-B1DA-40A8AD465656")
    private val beaconRegion: ArrayList<BeaconRegion>? = null

    init {
        requestLocationPermission()
        val uuids = Credentials.appInfo.uuids.toCollection(ArrayList())
        if(uuids.isNotEmpty()) {
            for (uuid in uuids) {
                beaconRegion?.add(BeaconRegion("ranged beacons ${uuids.indexOf(uuid)}",beaconUUID as UUID,0,0))
            }
        }
        else {
            beaconRegion?.add(BeaconRegion("ranged beacons ${0}","5C38DBDE-567C-4CCA-B1DA-40A8AD465656" as UUID,0,0))
        }
    }


    fun requestLocationPermission() {
        if (isLocationServiceEnabled()) {
            startScanning()
        }
    }

    fun startScanning() {
        updateLocation(true)
        startMonitoring()
    }

    fun stopScanning() {
        updateLocation(false)
        stopMonitoring()
    }


    private fun updateLocation(start: Boolean) {

        if (start) {
            Timber.i("=====Location Manager Start Updating======")

        } else {
            Timber.i("=====Location Manager Stop Updating======")
        }

    }
    private fun startMonitoring() {
        BeaconManagers.startMonitoring()
    }

    private fun stopMonitoring() {
        BeaconManagers.stopMonitoring()
    }

    private fun isLocationServiceEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            val lm = TSApplicationContext.TSContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            // This was deprecated in API 28
            val mode: Int = Settings.Secure.getInt(
                TSApplicationContext.TSContext.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }


    fun observeBeaconRanged(completion: (Intent)->Unit,context: Context): BroadcastReceiver {
        val mBeaconsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                completion.invoke(intent!!.getSerializableExtra(beaconRangeNotificationName) as Intent)
            }
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(mBeaconsReceiver, IntentFilter(beaconRangeNotificationName))
        return mBeaconsReceiver
    }

    // MARK: - CLLocationManagerDelegateb
    fun locationManager(manager: TSLocationManager, didRangeBeacons: Boolean,  beacons : HashMap<String, TSBeacon>?,  region: BeaconRegion) {
        val intent = Intent(beaconRangeNotificationName)
        intent.putExtra("beaconDetected", beacons)
        LocalBroadcastManager.getInstance(TSApplicationContext.TSContext).sendBroadcast(intent)
    }


}