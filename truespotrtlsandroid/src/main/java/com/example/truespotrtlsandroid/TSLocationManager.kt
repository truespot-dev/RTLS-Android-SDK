package com.example.truespotrtlsandroid

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber


class TSLocationManager(context : Context, activity: Activity) {
    //val shared = TSLocationManager(context,activity)
    val mContext = context
    val mActivity = activity
    private var cLocationManager: CLLocationManager? = null
    private var locationManager : LocationManager? = null
    var currentLocation: Location? = null
    private val handlerLocation: Handler? = null
    protected var googleApiClient: GoogleApiClient? = null


    init {

        requestLocationPermission()
        Toast.makeText(mContext,"Location-->init", Toast.LENGTH_LONG).show()

    }


    fun requestLocationPermission()
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

        if(isLocationServiceEnabled(mContext))
        {
            Toast.makeText(mContext,"Location-->True", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(mContext,"Location-->false", Toast.LENGTH_LONG).show()
        }
    }

    fun  stopScanning()
    {
        updateLocation(false)

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