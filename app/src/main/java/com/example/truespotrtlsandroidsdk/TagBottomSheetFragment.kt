package com.example.truespotrtlsandroidsdk

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.truespotrtlsandroid.BeaconManagers
import com.example.truespotrtlsandroid.TSBeacon
import com.example.truespotrtlsandroid.TSBeaconManagers
import com.example.truespotrtlsandroid.TrueSpot
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.databinding.FragmentModarModeBinding
import com.example.truespotrtlsandroid.models.TSDevice
import com.example.truespotrtlsandroidsdk.databinding.FragmentTagBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson


class TagBottomSheetFragment(var mSelectedDevice : String) : BottomSheetDialogFragment(),ClickHandler {


    var _binding: FragmentTagBottomSheetBinding? = null
    val binding get() = _binding!!
    var handler: Handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    var delay = 2000
    var adapter: BeaconsAdapter? = null
    var tagAdapter: TagAdapter? = null
    var trackingDeviceList : MutableList<TSDevice>? = ArrayList()
    var updatedBeaconList : MutableList<TSBeaconSighting> = ArrayList()
    var beaconManager : BeaconManagers? = null
    interface Listener {
        fun onSelected(device: TSDevice?)
        fun onTagSelected(beacon: TSBeaconSighting?)
    }
    private val mBottomSheetBehaviorCallback: BottomSheetBehavior.BottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTagBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    companion object {
        const val TAG = "TagBottomSheetFragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         beaconManager = this@TagBottomSheetFragment.context?.let { this@TagBottomSheetFragment.activity?.let { it1 ->
             BeaconManagers(it,
                 it1
             )
         } }

        beaconManager!!.startMonitoring()

        binding.closeimg.setOnClickListener {
           dismiss()
       }

        binding.progressBar.visibility = View.VISIBLE
        Log.i("getTrackingDevice-->", Gson().toJson(TSBeaconManagers.mTrackingDevices))
    }

    override fun onResume() {
        super.onResume()
           handler.postDelayed(Runnable {
           if (runnable != null) {
               handler.postDelayed(runnable!!, delay.toLong())
               if(mSelectedDevice.equals("trackingDevice"))
               {
                   TSBeaconManagers.mTrackingDevices?.forEach{
                       trackingDeviceList!!.add(it.value)
                   }

                   if(trackingDeviceList != null)
                   {
                       getBeaconList()
                       this@TagBottomSheetFragment.context?.let { TrueSpot.stopScanning(it,this.requireActivity()) }
                   }
               }

               else
               {
                   getTagList()
               }


           }
       }.also { runnable = it }, delay.toLong())
    }




    private fun getBeaconList() {
        if (trackingDeviceList!! != null) {
            binding.progressBar.visibility = View.GONE
            adapter = BeaconsAdapter(trackingDeviceList!!, "", object : Listener {

                override fun onSelected(device: TSDevice?) {
                    if (device != null) {
                        TrueSpot.launchTruedarMode(fragmentManager!!, device)
                    }
                }

                override fun onTagSelected(device: TSBeaconSighting?) {

                }

            })

            binding.tagsList.adapter = adapter
            binding.tagsList.layoutManager = LinearLayoutManager(context)
            ( binding.tagsList.adapter as BaseRecyclerViewAdapter).onItemClickListener = this
        }
        else
        {
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun getTagList()
    {

            // beaconManager = BeaconManagers(applicationContext,this@MainActivity)

            // beaconManager!!.startMonitoring()
            var result1 : MutableList<TSBeaconSighting>? = beaconManager!!.beaconUpdatedList
            val result : Collection<TSBeacon>  = TSBeaconManagers.getBeaconWithIdentifiers()!!.values


                result1?.forEach {
                    if(!updatedBeaconList.contains(it))
                    {
                        updatedBeaconList.add(it)
                    }
                }

                if(updatedBeaconList!! != null)
                {
                    binding.progressBar.visibility = View.GONE

                        tagAdapter = TagAdapter(updatedBeaconList!!, "", object : Listener {

                            override fun onSelected(device: TSDevice?) {

                            }

                            override fun onTagSelected(beacon: TSBeaconSighting?) {
                                if (beacon != null) {
                                    //TrueSpot.launchTruedarModeTag(fragmentManager!!, beacon)
                                }
                            }

                        })

                        binding.tagsList.adapter = tagAdapter
                        binding.tagsList.layoutManager = LinearLayoutManager(context)
                        ( binding.tagsList.adapter as BaseRecyclerViewAdapter).onItemClickListener = this

                    }

                else
                {
                    binding.progressBar.visibility = View.VISIBLE
                }




    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val inflatedView = View.inflate(context, R.layout.fragment_tag_bottom_sheet, null)
        dialog.setContentView(inflatedView)
        val params = (inflatedView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }
        val parent = inflatedView.parent as View
        parent.fitsSystemWindows = true
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(parent)
        inflatedView.measure(0, 0)
        val displaymetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displaymetrics)
        val screenHeight = displaymetrics.heightPixels
        bottomSheetBehavior.peekHeight = screenHeight - 50
        if (params.behavior is BottomSheetBehavior<*>) {
            (params.behavior as BottomSheetBehavior<*>?)!!.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }
        params.height = screenHeight
        parent.layoutParams = params
    }

    override fun invoke(view: View, position: Int, type: Int) {
        TODO("Not yet implemented")
    }
}