package com.example.truespotrtlsandroidsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.truespotrtlsandroid.*
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    var trackingDeviceBtn : Button? = null
    var tagDeviceBtn : Button? = null
    var tagList : RecyclerView? = null
    var adapter: BeaconsAdapter? = null
    var beaconManager : BeaconManagers? = null
    var updatedBeaconList : MutableList<TSBeacon> = ArrayList()
    var countDownTimer: CountDownTimer? = null
    var handler: Handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    var delay = 30000
    interface Listener {
        fun onSelected(beaconId: String?)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrueSpot.configure(application,this,this,applicationContext,this@MainActivity,
            getString(R.string.tenantId),getString(R.string.secret),true)

        trackingDeviceBtn = findViewById(R.id.trackingDevice)
        tagDeviceBtn = findViewById(R.id.tagDevice)


       // beaconManager = BeaconManagers(applicationContext,this@MainActivity)

       // beaconManager!!.startMonitoring()

        trackingDeviceBtn!!.setOnClickListener {
            val tagBottomSheetFragment = TagBottomSheetFragment("trackingDevice")
            tagBottomSheetFragment.show(supportFragmentManager, TagBottomSheetFragment.TAG)
        }

        tagDeviceBtn!!.setOnClickListener {
            val tagBottomSheetFragment = TagBottomSheetFragment("tagDevice")
            tagBottomSheetFragment.show(supportFragmentManager, TagBottomSheetFragment.TAG)
        }

       // TrueSpot.showMessage(applicationContext,"Heloo Android Team.....")

       // TrueSpot.startScanning(applicationContext,this@MainActivity)

       // TrueSpot.getTrackingDevices(this,this,applicationContext,this@MainActivity)

       /* btn = findViewById(R.id.btnFindTag)
        tagList = findViewById(R.id.tags_list)
        btn!!.setOnClickListener {

        }*/

       /* if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }


        //var result : MutableList<TSBeaconSighting>? = beaconManager!!.beaconUpdatedList
        val result : Collection<TSBeacon>  = TSBeaconManagers.getBeaconWithIdentifiers()!!.values
        for(s in result!!)
        {
            if(!updatedBeaconList.contains(s))
            {
                updatedBeaconList.add(s)
            }
        }
        if(updatedBeaconList != null)
        {
            adapter = BeaconsAdapter(updatedBeaconList, "", object : Listener {

                override fun onSelected(beaconId: String?) {
                    val bottomSheetFragment = ModarModeFragment(beaconId!!)
                    bottomSheetFragment.show(supportFragmentManager, ModarModeFragment.TAG)


                }

            })

            tagList!!.adapter = adapter
            tagList!!.layoutManager = LinearLayoutManager(this@MainActivity)
            (tagList!!.adapter as BaseRecyclerViewAdapter).onItemClickListener = this
        }*/

        Log.i("getTrackingDevice-->", Gson().toJson(TSBeaconManagers.mTrackingDevices))

    }







    override fun onResume() {
        super.onResume()
        Log.i("onResume","---")
     /*   handler.postDelayed(Runnable {
            if (runnable != null) {
                handler.postDelayed(runnable!!, delay.toLong())
                 Log.i("getTrackingDevice", "Size:${TSBeaconManagers.mTrackingDevices.size}")
                 Log.i("getTrackingDevice-->", Gson().toJson(TSBeaconManagers.mTrackingDevices))
                getBeaconList()
            }
        }.also { runnable = it }, delay.toLong())*/
    }

    private fun getBeaconList()
    {
        countDownTimer = object : CountDownTimer(30000, 2000), ClickHandler {
            override fun onTick(millisUntilFinished: Long) {
                this@MainActivity!!.runOnUiThread {
                    // beaconManager = BeaconManagers(applicationContext,this@MainActivity)

                   // beaconManager!!.startMonitoring()
                    var result1 : MutableList<TSBeaconSighting>? = beaconManager!!.beaconUpdatedList
                    val result : Collection<TSBeacon>  = TSBeaconManagers.getBeaconWithIdentifiers()!!.values

                    this@MainActivity!!.runOnUiThread {
                        for(s in result!!)
                        {
                            if(!updatedBeaconList.contains(s))
                            {
                                updatedBeaconList.add(s)
                            }
                        }

                        this@MainActivity.runOnUiThread{
                          //  adapter?.setUpdatedList(updatedBeaconList)

                        }

                    }
                }




            }

            override fun onFinish() {
                ///  TODO("Not yet implemented")
            }

            override fun invoke(view: View, position: Int, type: Int) {
                // TODO("Not yet implemented")
            }


        }.start()
    }


}