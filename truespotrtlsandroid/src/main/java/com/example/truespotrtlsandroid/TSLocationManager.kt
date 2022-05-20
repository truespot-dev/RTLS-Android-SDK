package com.example.truespotrtlsandroid

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Region
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.truespotrtlsandroid.beacon.TSBeaconManager
import com.example.truespotrtlsandroid.models.BeaconList.getInstance
import com.example.truespotrtlsandroid.models.BeaconRegion
import com.example.truespotrtlsandroid.models.Credentials
import com.example.truespotrtlsandroid.models.IBeacon
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList




class TSLocationManager(context : Context, activity: Activity) {
    //val shared = TSLocationManager(context,activity)
    val mContext = context
    val mActivity = activity
    private var cLocationManager: CLLocationManager? = null
    private var locationManager : LocationManager? = null
    var currentLocation: Location? = null
    private val handlerLocation: Handler? = null
    protected var googleApiClient: GoogleApiClient? = null
    private var beaconRangeNotificationName = "beaconRange"
    val beaconUUID: String = "5C38DBDE-567C-4CCA-B1DA-40A8AD465656"
    val beaconUUIDs = arrayOf("5C38DBDE-567C-4CCA-B1DA-40A8AD465656")


    val beaconRegion: ArrayList<BeaconRegion>? = null


    init {

        requestLocationPermission()
        Toast.makeText(mContext,"Location-->init", Toast.LENGTH_LONG).show()
        val uuids = Credentials.appInfo.uuids.toCollection(ArrayList())
        if(uuids.isNotEmpty())
        {
            for (uuid in uuids) {
                beaconRegion!!.add(BeaconRegion(TSLocationManager(context,activity).beaconUUID,"ranged beacons ${uuids.indexOf(uuid)}"))
            }
        }
        else
        {
            beaconRegion!!.add(BeaconRegion("5C38DBDE-567C-4CCA-B1DA-40A8AD465656","ranged beacons ${0}"))
        }



    }


    private fun requestLocationPermission()
    {
        if(isLocationServiceEnabled(mContext))
        {
            cLocationManager = CLLocationManager(mContext,mActivity)
            cLocationManager!!.startLocationUpdates()
        }

    }

    fun startScanning()
    {
        updateLocation(true)
        for(region in beaconRegion!!)
        {
            startMonitoring(region)
        }
    }

    fun  stopScanning()
    {
        updateLocation(false)
        for(region in beaconRegion!!)
        {
            stopMonitoring(region)
        }
    }



    private fun updateLocation(start: Boolean)
    {

        if(cLocationManager == null)
        {
            Timber.i("=====Location Manager NIL======")
            return
        }
        if(start){
            Timber.i("=====Location Manager Start Updating======")
            cLocationManager!!.startLocationUpdates()

        }
        else
        {
            Timber.i("=====Location Manager Stop Updating======")
            cLocationManager!!.stopLocationUpdates()
        }

    }


    private fun startMonitoring(beaconRegion: BeaconRegion)
    {

    }

    private fun stopMonitoring(beaconRegion: BeaconRegion)
    {

    }

    private fun isLocationServiceEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            // This was deprecated in API 28
            val mode: Int = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }


}