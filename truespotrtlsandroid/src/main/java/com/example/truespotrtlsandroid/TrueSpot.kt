package com.example.truespotrtlsandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.truespotrtlsandroid.models.Credentials
import timber.log.Timber

object TrueSpot  {

     //var shared = TrueSpot()
     // test
    /// Debug mode flag. Keep this off for production. Only for debugging purposes.
    var isDebugMode = false

    init { }



    /// Configure is the entry point to initializing the SDK.
    /// - Parameters:
    ///   - tenatId: the tenantId for your organization - will be provided for your organization
    ///   - clientSecret: client secret - will be provided for your organization
    ///   - isDebugMode: If turn on, you can see logs as you use the SDK,
    fun  configure(application : Application,viewModelStoreOwner: ViewModelStoreOwner, viewLifecycleOwner : LifecycleOwner, context : Context, activity: Activity, tenatId: String, clientSecret: String, isDebugMode: Boolean)
    {
        TrueSpot.isDebugMode = isDebugMode
        Credentials.tenantId = tenatId
        Credentials.clientSecret = clientSecret
        BeaconServices.authenticate(viewModelStoreOwner,viewLifecycleOwner,context,activity,application)

    }

    /// In order to get access device location, apple requires us to ask the user permission. Call this function when you need to request permission to the user
    fun requestLocationPermission() {
      //  TSLocationManager.shared.requestLocationPermission()
    }


    /// Upon initializing the SDK, the SDK will internally call start scanning. This is for the purpose scanning beacons. You can call this function counterpart stopScanning() if you no longer want to scan.
    fun startScanning() {
       // TSLocationManager.shared.startScanning()
    }


    /// Call this function when you no longer want scan for beacons
    fun stopScanning() {
       // TSLocationManager.shared.stopScanning()
    }




    fun showMessage(context : Context, message : String)
    {
        //Test
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}