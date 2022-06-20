package com.example.truespotrtlsandroid

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import com.example.truespotrtlsandroid.beacon.BeaconRegion
import com.example.truespotrtlsandroid.models.Credentials
import com.google.android.gms.common.api.GoogleApiClient
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList




class TSLocationManager(context : Context, activity: Activity) {
    //val shared = TSLocationManager(context,activity)
    val mContext = context
    val mActivity = activity
    private var cLocationManager: LocationManagers? = null
    private var locationManager : LocationManager? = null
    var currentLocation: Location? = null
    private val handlerLocation: Handler? = null
    protected var googleApiClient: GoogleApiClient? = null
    private var beaconRangeNotificationName = "beaconRange"
    val beaconUUID: String = "5C38DBDE-567C-4CCA-B1DA-40A8AD465656"
    val beaconUUIDs = arrayOf("5C38DBDE-567C-4CCA-B1DA-40A8AD465656")

    val beaconRegion: ArrayList<BeaconRegion>? = null

    var beaconManager : BeaconManagers? = null


    init {

        requestLocationPermission()
        /*val uuids = Credentials.appInfo.uuids.toCollection(ArrayList())
        if(uuids.isNotEmpty())
        {
            for (uuid in uuids) {
                beaconRegion!!.add(BeaconRegion("ranged beacons ${uuids.indexOf(uuid)}",TSLocationManager(context,activity).beaconUUID as UUID,0,0))
            }
        }
        else
        {
            beaconRegion!!.add(BeaconRegion("ranged beacons ${0}","5C38DBDE-567C-4CCA-B1DA-40A8AD465656" as UUID,0,0))
        }*/

        beaconManager = BeaconManagers(context,activity)
    }


    fun requestLocationPermission()
    {
        if(isLocationServiceEnabled(mContext))
        {
            cLocationManager = LocationManagers(mContext,mActivity)
          //  cLocationManager!!.startLocationUpdates()
            startScanning()

        }

    }

    fun startScanning()
    {
        updateLocation(true)
        startMonitoring()
       /* for(region in beaconRegion!!)
        {
            startMonitoring(region)
        }*/

      //  TSBeaconManagers.initializeBeaconObserver()


    }

    fun  stopScanning()
    {
        updateLocation(false)
        stopMonitoring()
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
           // cLocationManager!!.startLocationUpdates()

        }
        else
        {
            Timber.i("=====Location Manager Stop Updating======")
           // cLocationManager!!.stopLocationUpdates()
        }

    }


  /*  private fun startMonitoring(beaconRegion: BeaconRegion)
    {
       beaconManager!!.startMonitoring(beaconRegion)
    }*/
    private fun startMonitoring()
    {
        beaconManager = BeaconManagers(mContext,mActivity)
        beaconManager!!.startMonitoring()
    }

    private fun stopMonitoring()
    {
       beaconManager!!.stopMonitoring()
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