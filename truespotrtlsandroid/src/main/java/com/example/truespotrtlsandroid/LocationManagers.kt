package com.example.truespotrtlsandroid

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationManagers(context: Context, activity: Activity) {


    init{
        checkForLocationPermission(context,activity)
    }


    private fun checkForLocationPermission(context: Context, activity : Activity) {
        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (checkRequiredPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,context) == true)  {
            // Permission Enable
        }
        else
        {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }


    }

    private fun checkRequiredPermission(permission: String, context: Context): Boolean? {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}