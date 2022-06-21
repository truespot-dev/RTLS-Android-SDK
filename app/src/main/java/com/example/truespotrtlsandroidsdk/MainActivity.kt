package com.example.truespotrtlsandroidsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.truespotrtlsandroid.TrueSpot
import com.example.truespotrtlsandroid.models.Credentials

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrueSpot.configure(
            getString(R.string.tenantId), getString(R.string.secret), true, application, this, this, applicationContext, this@MainActivity,

        )

    }

}