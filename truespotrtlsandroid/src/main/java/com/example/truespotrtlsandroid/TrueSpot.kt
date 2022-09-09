package com.example.truespotrtlsandroid

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.truespotrtlsandroid.models.Credentials
import com.example.truespotrtlsandroid.models.PairRequestBody
import com.example.truespotrtlsandroid.models.TSDevice
import java.util.*

object TrueSpot : Application() {
    var isDebugMode = false

    init {
    }

    fun configure(
        context: Context,
        tenantId: String,
        clientSecret: String,
        isDebugMode: Boolean,
        completion: (exception: Exception?) -> Unit
    ) {
        TrueSpot.isDebugMode = isDebugMode
        Credentials.tenantId = tenantId
        Credentials.clientSecret = clientSecret
        TSApplicationContext.TSContext = context
        BeaconServices.authenticate(completion)
    }


    fun requestLocationPermission() {
        TSLocationManager.requestLocationPermission()
    }

    fun startScanning(context: Context) {
        TSApplicationContext.TSContext = context
        TSLocationManager.startScanning()
    }

    fun stopScanning() {
        TSLocationManager.stopScanning()
    }

    fun launchTruedarMode(supportFragmentManager: FragmentManager, device: TSDevice) {
        val bottomSheetFragment = ModarModeFragment(device.tagIdentifier)
        bottomSheetFragment.show(supportFragmentManager, ModarModeFragment.TAG)

    }

    fun observeBeaconRanged(
        listener: (beacons: MutableList<TSBeacon>) -> Unit,
        context: Context
    ): BroadcastReceiver {
        return TSBeaconManagers.observeBeaconRanged(listener, context)
    }

    fun getTrackingDevices(completion: (devices: MutableList<TSDevice>, exception: Exception?) -> Unit) {
        BeaconServices.getTrackingDevices(completion)
    }

    fun pair(
        assetIdentifier: String,
        assetType: String,
        tagId: String,
        completion: (devices: TSDevice?, exception: Exception?) -> Unit
    ) {
        BeaconServices.pair(assetIdentifier, assetType, tagId, completion)
    }

    fun unpair(deviceID: String, pairingId: String, completion: (exception: Exception?) -> Unit) {
        BeaconServices.unpair(deviceID, pairingId, completion)
    }

}