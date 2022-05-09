package com.example.truespotrtlsandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.truespotrtlsandroid.models.Authorization
import com.example.truespotrtlsandroid.models.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.reflect.typeOf

class BeaconServices {
    fun authenticate(context: Context,activity: Activity)
    {
        BeaconAPI.getBeaconApi()
            .authenticate(Credentials.tenantId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    if (it != null) {
                      //  var result = it.jwt
                        Credentials.jwt = it.jwt
                        getAppinfo(context,activity)

                    }else{
                        AlertDialog.Builder(context)
                            .setTitle(R.string.error)
                            .setMessage(R.string.error_msg)
                            .setPositiveButton("OK",null).show()
                    }
                }
            ) { error: Throwable ->

                Toast.makeText(context, R.string.update_failure, Toast.LENGTH_SHORT).show()
            }
    }

    fun getAppinfo(context: Context,activity: Activity)
    {
        BeaconAPI.getBeaconApi()
            .getAppinfo()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                if(it != null)
                {
                    //var result = it.body()
                    TSLocationManager(context,activity).startScanning()
                }
                else
                {
                    AlertDialog.Builder(context)
                        .setTitle(R.string.error)
                        .setMessage(R.string.error_msg)
                        .setPositiveButton("OK",null).show()
                }
            })
            { error: Throwable ->

                Toast.makeText(context, R.string.update_failure, Toast.LENGTH_SHORT).show()
            }

    }




}
object API{
    //DEV
    val authURL = "https://authprovider-d-us-c-api.azurewebsites.net/"

    //PROD
    // val authURL = "https://auth.truespot.com/"

    //DEV
    val RTLSBaseURL = "https://rtls-d-us-c-api.azurewebsites.net/"

    //PROD
    //val RTLSBaseURL = "https://rtls.truespot.com/"

    object Endpoint{
      const val authorization = "api/api-authorizations"
      const val trackingDevices = "api/tracking-devices"
      const val applications = "api/applications"
    }
}