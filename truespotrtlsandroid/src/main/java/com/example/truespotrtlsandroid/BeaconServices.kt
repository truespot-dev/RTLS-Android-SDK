package com.example.truespotrtlsandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.observe
import com.example.truespotrtlsandroid.TrueSpot.startScanning
import com.example.truespotrtlsandroid.models.*
import com.example.truespotrtlsandroid.models.Credentials
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import okhttp3.HttpUrl
import org.json.JSONArray

import org.json.JSONObject


object BeaconServices {

   fun authenticate(completion: (exception: Exception?) -> Unit)
    {
        val url = API.authURL+API.Endpoint.authorization+"?tenantId=${Credentials.tenantId}"
        // add parameter
        val formBody = FormBody.Builder().add("tenantId", Credentials.tenantId)
            .build()
        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Basic ${Credentials.clientSecret}")
            .post(formBody)
            .build()
        val client = OkHttpClient()
         client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    val auth : Authorization = Gson().fromJson(response.body?.charStream(),Authorization::class.java)
                    Credentials.jwt = auth.jwt
                    getAppinfo()
                    getTrackingDevices { devices, exception -> }
                    completion(response.message as Exception)
                }else
                {
                    completion(response.message as Exception)
                }
            }

        })

    }

    fun getAppinfo()
    {
        val url = API.RTLSBaseURL+API.Endpoint.applications+"?self"
        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .get()
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    val tsApplication : TSApplication = Gson().fromJson(response.body?.charStream(),TSApplication::class.java)
                    Credentials.appInfo = tsApplication
                    TSLocationManager.startScanning()
                }
            }

        })

    }

    fun getTrackingDevices(completion: (devices: MutableList<TSDevice>, exception: Exception?) -> Unit)
    {
        val url = API.RTLSBaseURL+API.Endpoint.trackingDevices
        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .get()
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    val tsDevice : TSDevice = Gson().fromJson(response.body?.charStream(),TSDevice::class.java)
                    TSBeaconManagers.updateTrackingDevices(arrayListOf(tsDevice))
                    completion.invoke(arrayListOf(tsDevice),response.message as Exception)

                }
            }

        })

    }

    fun pair(assetIdentifier: String, assetType: String, tagId: String, completion: (devices: TSDevice?, exception: Exception?) -> Unit)
    {
        val url = API.RTLSBaseURL+API.Endpoint.trackingDevices+"/${tagId}/pairings"

        // add parameter
        val formBody = FormBody.Builder().add("assetIdentifier", assetIdentifier).add("assetType",assetType)
            .build()

        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .post(formBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    val device : TSDevice = Gson().fromJson(response.body?.charStream(),TSDevice::class.java)
                    completion(device,response.message as Exception)
                }
            }

        })

    }

    fun unpair(deviceID: String, pairingId: String,  completion: (exception: Exception?) -> Unit)
    {
        val url = API.RTLSBaseURL+API.Endpoint.trackingDevices+"/${deviceID}/pairings/${pairingId}"
        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .delete()
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    completion(response.message as Exception)
                }
            }

        })

    }
}

enum class TSEnvironment{
    dev,
    prod

}


object API {

    var  environment: TSEnvironment = TSEnvironment.dev
    val authURL = when(environment){
        TSEnvironment.dev -> "https://authprovider-d-us-c-api.azurewebsites.net/"
        TSEnvironment.prod -> "https://auth.truespot.com/"
    }

    val RTLSBaseURL = when(environment){
        TSEnvironment.dev -> "https://rtls-d-us-c-api.azurewebsites.net/"
        TSEnvironment.prod -> "https://rtls.truespot.com/"
    }

    object Endpoint {
        const val authorization = "api/api-authorizations"
        const val trackingDevices = "api/tracking-devices"
        const val applications = "api/applications"
    }
}