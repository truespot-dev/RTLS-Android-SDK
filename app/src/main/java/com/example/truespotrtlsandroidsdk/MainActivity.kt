package com.example.truespotrtlsandroidsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.truespotrtlsandroid.TrueSpot

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
                launchModarMode()
            }
        }
    }

    fun launchModarMode() {
        TrueSpot.launchTruedarMode(supportFragmentManager, "0000-11RNS", this)
    }

}