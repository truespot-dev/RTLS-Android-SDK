package com.example.truespotrtlsandroid

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.observe
import com.example.truespotrtlsandroid.models.Credentials
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


object BeaconServices {

    fun authenticate(viewModelStoreOwner: ViewModelStoreOwner,viewLifecycleOwner : LifecycleOwner,context: Context, activity: Activity)
    {
        val beaconServiceViewModel : BeaconServiceViewModel  = ViewModelProvider(viewModelStoreOwner,BeaconServiceViewModelFactory(activity.application, ApiHelper(RetrofitBuilder.apiAuthService)))
           .get(BeaconServiceViewModel::class.java)

        beaconServiceViewModel.authenticate(Credentials.tenantId).observe(viewLifecycleOwner)
        {
            when (it.status)
            {
                Status.SUCCESS ->{
                    if(it.data != null)
                    {
                        val result = it.data
                        Credentials.jwt = result.jwt
                        getAppinfo(viewModelStoreOwner,viewLifecycleOwner,context,activity)
                    }
                    else
                    {
                        AlertDialog.Builder(context)
                            .setTitle(R.string.error)
                            .setMessage(it.message)
                            .setPositiveButton("OK",null).show()
                    }

                }
                Status.LOADING -> {}
                Status.ERROR ->{
                    AlertDialog.Builder(context)
                        .setTitle(R.string.error)
                        .setMessage(it.message)
                        .setPositiveButton("OK",null).show()
                }
            }
        }
    }

    fun getAppinfo(viewModelStoreOwner: ViewModelStoreOwner,viewLifecycleOwner : LifecycleOwner,context: Context,activity: Activity)
    {
        val beaconServiceViewModel : BeaconServiceViewModel  = ViewModelProvider(viewModelStoreOwner,BeaconServiceViewModelFactory(activity.application, ApiHelper(RetrofitBuilder.apiAuthService)))
            .get(BeaconServiceViewModel::class.java)


        beaconServiceViewModel.getAppinfo().observe(viewLifecycleOwner){
            when(it.status)
            {
                Status.SUCCESS ->{
                    if(it.data != null)
                    {
                        val result = it.data
                        Credentials.appInfo = result
                        TSLocationManager(context,activity).startScanning()
                    }
                    else
                    {
                        AlertDialog.Builder(context)
                            .setTitle(R.string.error)
                            .setMessage(it.message)
                            .setPositiveButton("OK",null).show()
                    }

                }
                Status.LOADING->{
                }
                Status.ERROR ->
                {
                    AlertDialog.Builder(context)
                        .setTitle(R.string.error)
                        .setMessage(it.message)
                        .setPositiveButton("OK",null).show()
                }
            }
        }




       /* BeaconAPI.getBeaconApi()
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
            }*/

    }




}
object API{
    //DEV
    val authURL = "https://authprovider-d-us-c-api.azurewebsites.net"

    //PROD
    // val authURL = "https://auth.truespot.com/"

    //DEV
    val RTLSBaseURL = "https://rtls-d-us-c-api.azurewebsites.net"

    //PROD
    //val RTLSBaseURL = "https://rtls.truespot.com/"

    object Endpoint{
      const val authorization = "api/api-authorizations"
      const val trackingDevices = "api/tracking-devices"
      const val applications = "api/applications"
    }
}