package com.example.truespotrtlsandroid

import android.content.Context
import android.widget.Toast
import com.example.truespotrtlsandroid.models.Credentials

class TrueSpot private constructor()  {

     val shared = TrueSpot()

    /// Debug mode flag. Keep this off for production. Only for debugging purposes.
    var isDebugMode = false

    init { }



    /// Configure is the entry point to initializing the SDK.
    /// - Parameters:
    ///   - tenatId: the tenantId for your organization - will be provided for your organization
    ///   - clientSecret: client secret - will be provided for your organization
    ///   - isDebugMode: If turn on, you can see logs as you use the SDK,
    fun  configure(tenatId: String, clientSecret: String, isDebugMode: Boolean)
    {
        TrueSpot().isDebugMode = isDebugMode
        Credentials.tenantId = tenatId
        Credentials.clientSecret = clientSecret
        BeaconServices().authenticate()

    }



    fun showMessage(context : Context, message : String)
    {
        //Test
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

}