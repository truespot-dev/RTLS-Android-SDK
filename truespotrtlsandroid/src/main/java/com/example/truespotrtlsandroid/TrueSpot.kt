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

    fun configure(context: Context,
        tenantId: String,
        clientSecret: String,
        isDebugMode: Boolean,
        completion: (exception: Exception?) -> Unit) {
        TrueSpot.isDebugMode = isDebugMode
        Credentials.tenantId = tenantId
        Credentials.clientSecret = clientSecret
        TSApplicationContext.TSContext = context
        BeaconServices.authenticate(completion)
    }


    fun requestLocationPermission() {
        TSLocationManager.requestLocationPermission()
    }

    fun startScanning() {
        TSLocationManager.startScanning()
    }

    fun stopScanning() {
        TSLocationManager.stopScanning()
    }

    fun launchTruedarMode(supportFragmentManager: FragmentManager, device: TSDevice) {
        val bottomSheetFragment = ModarModeFragment(device.tagIdentifier)
        bottomSheetFragment.show(supportFragmentManager, ModarModeFragment.TAG)

    }

    fun observeBeaconRanged(context: Context,listener: (beacons: MutableList<TSBeacon>)-> Unit): BroadcastReceiver
    {
        return TSBeaconManagers.observeBeaconRanged(context,listener)
    }

    fun getTrackingDevices(completion: (devices: MutableList<TSDevice>, exception: Exception?) -> Unit,viewModelStoreOwner: ViewModelStoreOwner, viewLifecycleOwner: LifecycleOwner, context: Context, activity: Activity)
    {
        BeaconServices.getTrackingDevices(completion)
    }


    fun pair(assetIdentifier: String, assetType: String, tagId: String,completion: (devices: TSDevice?, exception: Exception?) -> Unit) {
        BeaconServices.pair(assetIdentifier,assetType, tagId, completion)
    }

    fun unpair(deviceID: String, pairingId: String, completion: (exception: Exception?) -> Unit) {
        BeaconServices.unpair(deviceID, pairingId,completion)
    }

}