package com.example.truespotrtlsandroid

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.truespotrtlsandroid.models.Credentials

class TrueSpot  {

     var shared = TrueSpot()

    /// Debug mode flag. Keep this off for production. Only for debugging purposes.
    var isDebugMode = false

    init { }



    /// Configure is the entry point to initializing the SDK.
    /// - Parameters:
    ///   - tenatId: the tenantId for your organization - will be provided for your organization
    ///   - clientSecret: client secret - will be provided for your organization
    ///   - isDebugMode: If turn on, you can see logs as you use the SDK,
    fun  configure(context : Context, activity: Activity, tenatId: String, clientSecret: String, isDebugMode: Boolean)
    {
        TrueSpot().isDebugMode = isDebugMode
        Credentials.tenantId = tenatId
        Credentials.clientSecret = clientSecret
        BeaconServices().authenticate(context,activity)

    }



    fun showMessage(context : Context, message : String)
    {
        //Test
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

}