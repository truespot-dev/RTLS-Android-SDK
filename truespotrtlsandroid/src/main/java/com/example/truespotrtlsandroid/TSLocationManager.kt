package com.example.truespotrtlsandroid

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class TSLocationManager(context : Context,activity: Activity) {
    val shared = TSLocationManager(context,activity)
    val mContext = context
    val mActivity = activity

    init {
        requestLocationPermission()
        Toast.makeText(mContext,"Location-->init", Toast.LENGTH_LONG).show()

    }

    fun requestLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }

    fun checkRequiredPermission(permission : String) : Boolean
    {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED
    }


    fun startScanning()
    {

        if(checkRequiredPermission(Manifest.permission.ACCESS_FINE_LOCATION))
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

    }
}