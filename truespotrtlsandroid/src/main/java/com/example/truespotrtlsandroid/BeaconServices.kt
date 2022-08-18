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
import com.example.truespotrtlsandroid.models.*
import com.example.truespotrtlsandroid.models.Credentials
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import okhttp3.HttpUrl
import org.json.JSONArray

import org.json.JSONObject




object BeaconServices {

    var locationManager: TSLocationManager? = null


    fun authenticate()
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

        var client = OkHttpClient()
         client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    var auth : Authorization = Gson().fromJson(response.body?.charStream(),Authorization::class.java)
                    Credentials.jwt = auth.jwt
                    getAppinfo()
                    getTrackingDevices { devices, exception -> }
                }
            }

        })

    }

    fun getAppinfo()
    {
        val url = API.RTLSBaseURL+API.Endpoint.applications+"?self"

        // add parameter
        val formBody = FormBody.Builder().add("", "")
            .build()

        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .get()
            .build()

        var client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    var tsApplication : TSApplication = Gson().fromJson(response.body?.charStream(),TSApplication::class.java)
                    Credentials.appInfo = tsApplication
                    /*locationManager = TSLocationManager(context, activity)
                    locationManager?.startScanning()*/
                }
            }

        })

    }

    fun getTrackingDevices(completion: (devices: MutableList<TSDevice>, exception: Exception?) -> Unit)
    {
        val url = API.RTLSBaseURL+API.Endpoint.trackingDevices

        // add parameter
        val formBody = FormBody.Builder().add("", "")
            .build()

        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .get()
            .build()

        var client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    var tsDevice : TSDevice = Gson().fromJson(response.body?.charStream(),TSDevice::class.java)
                    TSBeaconManagers.updateTrackingDevices(arrayListOf(tsDevice))
                    completion.invoke(arrayListOf(tsDevice),response.message as Exception)

                }
            }

        })

    }

    fun pair(pairRequestBody: PairRequestBody?, tagID: String, viewModelStoreOwner: ViewModelStoreOwner, viewLifecycleOwner: LifecycleOwner, context: Context, activity: Activity, completion: (devices: MutableList<TSDevice>, exception: Exception?) -> Unit) {
        val beaconBaseServiceViewModel: BeaconBaseServiceViewModel = ViewModelProvider(viewModelStoreOwner,
            BeaconBaseServiceViewModelFactory(
                activity.application,
                BaseApiHelper(BaseRetrofitBuilder.apiBaseService)))
            .get(BeaconBaseServiceViewModel::class.java)
        beaconBaseServiceViewModel.pair(pairRequestBody, tagID).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val device = it.data
                    completion(device as MutableList<TSDevice>,it.message as Exception)
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
            }
        }
    }

    fun unpair(deviceID: String, pairingId: String, viewModelStoreOwner: ViewModelStoreOwner, viewLifecycleOwner: LifecycleOwner, context: Context, activity: Activity,completion: (exception: Exception?) -> Unit) {
        val beaconBaseServiceViewModel: BeaconBaseServiceViewModel = ViewModelProvider(viewModelStoreOwner,
            BeaconBaseServiceViewModelFactory(
                activity.application,
                BaseApiHelper(BaseRetrofitBuilder.apiBaseService)))
            .get(BeaconBaseServiceViewModel::class.java)
        beaconBaseServiceViewModel.unpair(deviceID, pairingId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    completion(it.message as Exception)
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
            }
        }
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