package com.example.truespotrtlsandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.truespotrtlsandroid.TrueSpot.requestLocationPermission
import com.example.truespotrtlsandroid.TrueSpot.startScanning
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.models.Credentials
import com.example.truespotrtlsandroid.models.PairRequestBody
import com.example.truespotrtlsandroid.models.TSDevice
import timber.log.Timber

object TrueSpot {

    //var shared = TrueSpot()
    // test
    /// Debug mode flag. Keep this off for production. Only for debugging purposes.
    var isDebugMode = false

    init {
    }


    /// Configure is the entry point to initializing the SDK.
    /// - Parameters:
    ///   - tenatId: the tenantId for your organization - will be provided for your organization
    ///   - clientSecret: client secret - will be provided for your organization
    ///   - isDebugMode: If turn on, you can see logs as you use the SDK,
    fun configure(
        tenantId: String,
        clientSecret: String,
        isDebugMode: Boolean,
        application: Application,
        viewModelStoreOwner: ViewModelStoreOwner,
        viewLifecycleOwner: LifecycleOwner,
        context: Context,
        activity: Activity,
    ) {
        TrueSpot.isDebugMode = isDebugMode
        Credentials.tenantId = tenantId
        Credentials.clientSecret = clientSecret
        BeaconServices.authenticate(
            viewModelStoreOwner,
            viewLifecycleOwner,
            context,
            activity,
            application
        )

    }

    /// In order to get access device location, apple requires us to ask the user permission. Call this function when you need to request permission to the user
    fun requestLocationPermission(context: Context, activity: Activity) {
        TSLocationManager(context, activity).requestLocationPermission()
    }


    /// Upon initializing the SDK, the SDK will internally call start scanning. This is for the purpose scanning beacons. You can call this function counterpart stopScanning() if you no longer want to scan.
    fun startScanning(context: Context, activity: Activity) {

        TSLocationManager(context, activity).startScanning()
    }


    /// Call this function when you no longer want scan for beacons
    fun stopScanning(context: Context, activity: Activity) {
        TSLocationManager(context, activity).stopScanning()
    }

    /// Turedar mode is our real time beacon finder. Calling this function will launch our TrudarMode interface, where you can search for your beacon
    /// - Parameters:
    ///   - viewController: The viewcontroller that will present the TrueDarMode
    ///   - device: TSDevice object which contains tag and other relevant infor for TuredarMode to be able to search for your beacon
    fun launchTruedarMode(supportFragmentManager: FragmentManager, device: TSDevice) {
        val bottomSheetFragment = ModarModeFragment(device.tagIdentifier)
        bottomSheetFragment.show(supportFragmentManager, ModarModeFragment.TAG)
    }


    /// Use this function to get notified of nearby beacons. One use case if for detecting beacons for pairing.
    /// - Parameter completion: completion handler everytime a beacon is detected
    /// - Returns: NSObjectProtocol Observable pattern
    fun observeBeaconRanged(beacon: HashMap<String, TSBeacon>?): HashMap<String, TSBeacon>? {
        return TSBeaconManagers.observeBeaconRanged(beacon)
    }


    /// Get list of tracking devices for per your appID
    /// - Parameter completion: callback with a list of TSDevice
    fun getTrackingDevices(
        viewModelStoreOwner: ViewModelStoreOwner,
        viewLifecycleOwner: LifecycleOwner,
        context: Context,
        activity: Activity
    ) {
        BeaconServices.getTrackingDevices(
            viewModelStoreOwner,
            viewLifecycleOwner,
            context,
            activity
        )
    }


    /// Use this function for pairing assets.
    /// - Parameters:
    ///   - assetIdentifier: identifier of the asset to pari
    ///   - assetType: the type of asset
    ///   - tagId: the tagId
    ///   - completion: callback for if the paring was successful or not.
    fun pair(
        assetIdentifier: String,
        assetType: String,
        tagId: String,
        viewModelStoreOwner: ViewModelStoreOwner,
        viewLifecycleOwner: LifecycleOwner,
        context: Context,
        activity: Activity
    ) {
        BeaconServices.pair(
            PairRequestBody(assetIdentifier, assetType),
            tagId,
            viewModelStoreOwner,
            viewLifecycleOwner,
            context,
            activity
        )
    }


}