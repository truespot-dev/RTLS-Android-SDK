package com.example.truespotrtlsandroidsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.truespotrtlsandroid.TrueSpot

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrueSpot.configure(application,this,this,applicationContext,this@MainActivity,
            getString(R.string.tenantId),getString(R.string.secret),true)
        TrueSpot.showMessage(applicationContext,"Heloo Android Team.....")

    }


}