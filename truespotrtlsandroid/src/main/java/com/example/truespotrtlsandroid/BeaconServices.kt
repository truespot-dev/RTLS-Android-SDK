package com.example.truespotrtlsandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.observe
import com.example.truespotrtlsandroid.models.Credentials
import com.example.truespotrtlsandroid.models.PairRequestBody
import com.example.truespotrtlsandroid.models.TSApplication
import com.example.truespotrtlsandroid.models.TSDevice
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


object BeaconServices {

    var locationManager : TSLocationManager? = null

    fun authenticate(viewModelStoreOwner: ViewModelStoreOwner,viewLifecycleOwner : LifecycleOwner,context: Context, activity: Activity,application : Application)
    {
        val beaconServiceViewModel : BeaconServiceViewModel = ViewModelProvider(viewModelStoreOwner,BeaconServiceViewModelFactory(activity.application, ApiHelper(RetrofitBuilder.apiAuthService)))
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
                       /* var appInfo = TSApplication()
                        appInfo.id = "620c3bf2f840f63c650eca3a"
                        appInfo.description = ""
                        appInfo.name ="Ikon"
                        appInfo.uuids = arrayOf("5c38dbde-567c-4cca-b1da-40a8ad465656")
                        Credentials.appInfo = appInfo

                        locationManager =  TSLocationManager(context,activity)
                        locationManager!!.startScanning()*/
                        getAppinfo(viewModelStoreOwner,viewLifecycleOwner,context,activity)
                        getTrackingDevices(viewModelStoreOwner,viewLifecycleOwner,context,activity)
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
        val beaconBaseServiceViewModel : BeaconBaseServiceViewModel  = ViewModelProvider(viewModelStoreOwner,BeaconBaseServiceViewModelFactory(activity.application, BaseApiHelper(BaseRetrofitBuilder.apiBaseService)))
            .get(BeaconBaseServiceViewModel::class.java)


        beaconBaseServiceViewModel.getAppinfo().observe(viewLifecycleOwner){
            when(it.status)
            {
                Status.SUCCESS ->{
                    if(it.data != null)
                    {
                        val result = it.data
                        Credentials.appInfo = result
                        locationManager =  TSLocationManager(context,activity)
                        locationManager!!.startScanning()

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






    }


    fun getTrackingDevices(viewModelStoreOwner: ViewModelStoreOwner,viewLifecycleOwner : LifecycleOwner,context: Context,activity: Activity)
    {
        val beaconBaseServiceViewModel : BeaconBaseServiceViewModel  = ViewModelProvider(viewModelStoreOwner,BeaconBaseServiceViewModelFactory(activity.application, BaseApiHelper(BaseRetrofitBuilder.apiBaseService)))
            .get(BeaconBaseServiceViewModel::class.java)

        beaconBaseServiceViewModel.getTrackingDevices().observe(viewLifecycleOwner)
        {
            when(it.status)
            {
                Status.SUCCESS ->
                {
                    val device = it.data
                    TSBeaconManagers.updateTrackingDevices(device)
                }
                Status.LOADING ->{}

                Status.ERROR -> {}

            }
        }
    }

    fun  pair(pairRequestBody: PairRequestBody?,viewModelStoreOwner: ViewModelStoreOwner, viewLifecycleOwner: LifecycleOwner, context: Context, activity: Activity, tagID : String)
    {
        val beaconBaseServiceViewModel : BeaconBaseServiceViewModel  = ViewModelProvider(viewModelStoreOwner,BeaconBaseServiceViewModelFactory(activity.application, BaseApiHelper(BaseRetrofitBuilder.apiBaseService)))
            .get(BeaconBaseServiceViewModel::class.java)
        beaconBaseServiceViewModel.pair(pairRequestBody,tagID).observe(viewLifecycleOwner)
        {
            when(it.status)
            {
                Status.SUCCESS ->{}
                Status.LOADING ->{}
                Status.ERROR ->{}
            }
        }
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
  //  val RTLSBaseURL = "https://rtls.truespot.com/"

    object Endpoint{
      const val authorization = "api/api-authorizations"
      const val trackingDevices = "api/tracking-devices"
      const val applications = "api/applications"
    }
}