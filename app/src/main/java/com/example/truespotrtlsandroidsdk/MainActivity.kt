package com.example.truespotrtlsandroidsdk

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.truespotrtlsandroid.*
import com.example.truespotrtlsandroid.CompletionCallBack
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.models.TSDevice
import com.google.gson.Gson
import java.lang.Exception
import kotlin.Unit.toString

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
/*
            TrueSpot.configure(this,"600efa6da13b840ab04ad9c2","1234567890",true){
                if (it == null) {
                    Log.i("Configure","Success")
                    TrueSpot.startScanning(this)
                    TrueSpot.launchTruedarMode(this@MainActivity.supportFragmentManager,"0000-11H1W")
                } else {
                    Log.i("Configure","Error:${it}")
                    TrueSpot.startScanning(this)
                    TrueSpot.launchTruedarMode(this@MainActivity.supportFragmentManager,"0000-11H1W")
                }
            }*/

             TrueSpot.launchTruedarMode(this.supportFragmentManager,"0000-11H1W",this)

        }catch (ex : Exception){
            Log.i("Exception---->",ex.message.toString())
        }



    }

}