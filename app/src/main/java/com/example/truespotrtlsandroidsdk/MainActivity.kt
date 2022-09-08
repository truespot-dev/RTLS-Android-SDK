package com.example.truespotrtlsandroidsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.truespotrtlsandroid.*
import com.example.truespotrtlsandroid.CompletionCallBack
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.google.gson.Gson
import kotlin.Unit.toString

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrueSpot.configure(
            this,
            "5da7715dd7eafd2aec878b6c",
            "x8KKW+fIrgikKASLL0CxzqmxAbG9iz6ZUe32bRq+6UYszf8PuWQoa9jQaTY+gLla",
            true
        ) {
            if (it == null) {
                Log.i("Configure", "Success")
            } else {
                Log.i("Configure", "Error:${it}")
            }
        }

        TrueSpot.getTrackingDevices { devices, exception ->
            if (exception == null) {
                Log.i("Configure", "Success")

                val device = devices[1]
                TrueSpot.pair(device.assetIdentifier, device.assetType, device.tagIdentifier) { devices, exception ->
                    if (exception == null) {
                        Log.i("Paired", "Success")
                    } else {
                        Log.i("Configure", "Error:${exception}")
                    }
                }
            } else {
                Log.i("Configure", "Error:${exception}")
            }
        }


        //TrueSpot.requestLocationPermission()

    }

}