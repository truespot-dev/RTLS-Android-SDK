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

object TrueSpot {

    var isDebugMode = false

    init {
    }

    fun configure(
        tenantId: String,
        clientSecret: String,
        isDebugMode: Boolean,
        completion: (exception: Exception?) -> Unit) {
        TrueSpot.isDebugMode = isDebugMode
        Credentials.tenantId = tenantId
        Credentials.clientSecret = clientSecret
        BeaconServices.authenticate()
    }


    fun requestLocationPermission(context: Context, activity: Activity) {
        TSLocationManager(context, activity).requestLocationPermission()
    }

    fun startScanning(context: Context, activity: Activity) {
        TSLocationManager(context, activity).startScanning()
    }

    fun stopScanning(context: Context, activity: Activity) {
        TSLocationManager(context, activity).stopScanning()
    }

    fun launchTruedarMode(supportFragmentManager: FragmentManager, device: TSDevice) {
        val bottomSheetFragment = ModarModeFragment(device.tagIdentifier)
        bottomSheetFragment.show(supportFragmentManager, ModarModeFragment.TAG)

    }

    fun observeBeaconRanged(listener: (beacons: MutableList<TSBeacon>)-> Unit,context: Context): BroadcastReceiver
    {
        return TSBeaconManagers.observeBeaconRanged(listener,context)
    }

    fun getTrackingDevices(completion: (devices: MutableList<TSDevice>, exception: Exception?) -> Unit,viewModelStoreOwner: ViewModelStoreOwner, viewLifecycleOwner: LifecycleOwner, context: Context, activity: Activity)
    {
        BeaconServices.getTrackingDevices(completion)
    }


    fun pair(assetIdentifier: String, assetType: String, tagId: String, viewModelStoreOwner: ViewModelStoreOwner, viewLifecycleOwner: LifecycleOwner, context: Context, activity: Activity,completion: (devices: MutableList<TSDevice>, exception: Exception?) -> Unit) {
        BeaconServices.pair(PairRequestBody(assetIdentifier, assetType), tagId, viewModelStoreOwner, viewLifecycleOwner, context, activity,completion)
    }

    fun unpair(deviceID: String, pairingId: String, viewModelStoreOwner: ViewModelStoreOwner, viewLifecycleOwner: LifecycleOwner, context: Context, activity: Activity,completion: (exception: Exception?) -> Unit) {
        BeaconServices.unpair(deviceID, pairingId, viewModelStoreOwner, viewLifecycleOwner, context, activity,completion)
    }

}