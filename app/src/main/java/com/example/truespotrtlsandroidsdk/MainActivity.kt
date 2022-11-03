package com.example.truespotrtlsandroidsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.truespotrtlsandroid.TrueSpot
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
        // Latest update committed

        TrueSpot.configure(
            this,
            "5da7715dd7eafd2aec878b6c",
            "x8KKW+fIrgikKASLL0CxzqmxAbG9iz6ZUe32bRq+6UYszf8PuWQoa9jQaTY+gLla",
            true
        ) {
            if (it == null) {
                Log.i("Configure", "Success")
            } else {
            }
        }
    }

    fun launchModarMode() {
        TrueSpot.launchTruedarMode(supportFragmentManager, "0000-11RNS", this)
    }

}