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

        TrueSpot.configure(this,"600efa6da13b840ab04ad9c2","1234567890",true){
            if (it == null) {
                Log.i("Configure","Success")
                TrueSpot.startScanning()
            } else {
                Log.i("Configure","Error:${it}")
                TrueSpot.startScanning()
            }
        }

        //TrueSpot.requestLocationPermission()

    }

}