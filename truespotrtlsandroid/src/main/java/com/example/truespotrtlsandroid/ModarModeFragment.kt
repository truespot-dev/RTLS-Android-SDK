package com.example.truespotrtlsandroid

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.media.MediaPlayer
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
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.truespotrtlsandroid.beacon.TSBeaconSighting
import com.example.truespotrtlsandroid.databinding.FragmentModarModeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ModarModeFragment(var mCurrentTag : String) :  BottomSheetDialogFragment() {

    var isSetProcess = false
    var isBeepSound = false
    var countDownTimer : CountDownTimer? = null
    var mMediaPlayer: MediaPlayer? = null
    var lastSeenDate : Date? = null
    var mDeviceCount = 0
    var jsonObject = JsonObject()
    var gson = Gson()
    var mHandler = Handler(Looper.getMainLooper())
    private var _binding: FragmentModarModeBinding? = null
    val binding get() = _binding!!
    private var previousRSSIValue = 0
    var farRSSILocationDictionary: MutableList<ItemDistance> = ArrayList()
    var nearRSSILocationDictionary: MutableList<ItemDistance> = ArrayList()
    var immediateRSSILocationDictionary: MutableList<ItemDistance> = ArrayList()
    var volume = 100
    var beaconManager : BeaconManagers? = null

    private val mBottomSheetBehaviorCallback: BottomSheetBehavior.BottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
    }

    companion object {
        const val TAG = "FindTagFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentModarModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun triggerAutoUpdate() {
        mHandler.post(object : Runnable{
            override fun run() {
                if (this@ModarModeFragment.isAdded){
                    Log.e("status","check Update-----> $isBeepSound")
                    requireFragmentManager().beginTransaction().detach(this@ModarModeFragment).attach(this@ModarModeFragment).commit()
                }

                mHandler.postDelayed(this,10000)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        triggerAutoUpdate()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.btnCloseIcon.setOnClickListener{

            stopSound()
            dismiss()
            if(countDownTimer != null) {
                countDownTimer!!.cancel()
                countDownTimer = null
            }
        }

        calculatTagLocation()
        beaconManager = BeaconManagers

        binding.tvCurentTag.text = mCurrentTag

        binding.btnCheckOut.setOnClickListener {

            if (volume == 100) {
                volume = 0
                isBeepSound = true
                stopSound()
                binding.btnCheckOut.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_speaker_mute))
            } else {
                volume = 100
                isBeepSound = false
                playSound()
                binding.btnCheckOut.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_speaker))
            }
        }

        countDownTimer = object : CountDownTimer(30000,500) {
            override fun onTick(millisUntilFinished: Long) {
                beaconManager!!.startMonitoring()
                var result1 : MutableList<TSBeaconSighting>? = beaconManager!!.beaconUpdatedList
                Log.i("result1","--->${Gson().toJson(result1)}")
              val result : Collection<TSBeacon> = TSBeaconManagers.getBeaconWithIdentifiers()!!.values
                Log.i("findTag","--->${Gson().toJson(result)}")
                if (result != null) {
                    var tempData : TSBeacon? = null
                    for (data : TSBeacon in result) {
                        if (mCurrentTag.equals(data.getBeaconIdentifier())) {
                            tempData = data
                            break
                        }

                    }
                    if(activity != null)
                    {
                        activity!!.runOnUiThread {

                            if (tempData!= null) {
                                //   FastModarManager.getInstance(activity).startFastModarMode(tempData!!.deviceAddress)
                                if(previousRSSIValue != tempData.rssi)
                                {
                                    setProgress(tempData.rssi)
                                    previousRSSIValue = tempData.rssi
                                    binding.pairedTag.visibility = View.VISIBLE
                                    binding.pairedTag.setText(tempData?.rssi.toString()+"\nrssi")
                                    binding.progressIndicator.visibility = View.GONE
                                    binding.tvNearSearch.visibility = View.GONE
                                    if (isBeepSound)
                                    {
                                        stopSound()
                                    } else {
                                        playSound()
                                    }
                                    mHandler.removeCallbacksAndMessages(null)
                                }


                            }

                        }

                    }


                }

            }

            override fun onFinish() {

                context.let {
                    Timber.e("CountDownTimer ==>>" )
                    countDownTimer!!.start()
                    stopSound()

                    if (!isSetProcess) {
                        mDeviceCount = 0
                        this@ModarModeFragment.activity?.let { it1 ->
                            AlertDialog.Builder(it1)
                                .setTitle(R.string.alert_title_tag)
                                .setMessage(R.string.alert_title_tag_message)
                                .setPositiveButton("OK") { _, _ ->
                                    initializeSetTagProcess()
                                }.show()
                        }
                    }
                    else{
                        mDeviceCount = mDeviceCount.inc()
                    }

                }
            }

        }

        initializeSetTagProcess()
    }

    private fun playSound() {
        mMediaPlayer = MediaPlayer.create(requireContext(), R.raw.hollow)
        mMediaPlayer!!.start()

    }
    private fun stopSound() {

        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
            mHandler.removeCallbacksAndMessages(null)
        }
    }
    private fun setProgress( range: Int) {
        isSetProcess = true
        var percentage = 0.0
        val param = binding.pairedTag.layoutParams as ViewGroup.MarginLayoutParams
        jsonObject.addProperty("setProgress1","setProgress:Method:==>range"+range)
        var margin :  Int = 0;


        var mOriginalValue = range
        if (range < 0){
            mOriginalValue = range * -1
        }
        jsonObject.addProperty("setProgress2","setProgress:mOriginalValue:==>"+mOriginalValue)

        if (mOriginalValue >= 75){

            for (data in farRSSILocationDictionary ) {
                if (data.range == range){
                    margin = data.possition
                    break
                }
            }

            jsonObject.addProperty("setProgress3","setProgress:margin:==>"+margin)


        }else if (mOriginalValue >= 53 && mOriginalValue <= 76 ) {

            for (data in nearRSSILocationDictionary ) {
                if (data.range == range){
                    margin = data.possition
                    break
                }
            }
            jsonObject.addProperty("setProgress3","setProgress:margin:==>"+margin)

        }else {

            for (data in immediateRSSILocationDictionary ) {
                if (data.range == range){
                    margin = data.possition + 150
                    break

                }
            }
            jsonObject.addProperty("setProgress3","setProgress:margin:==>"+margin)
        }

        binding.findTagToolbar.post {
            var tHeight = binding.findTagToolbar.layoutParams.height + margin
            val totalHeight: Int = Resources.getSystem().getDisplayMetrics().heightPixels + tHeight
            Log.e("totalHeight:", totalHeight.toString())

            if (range >= -45) {
                percentage = ((-60 * -1) * 0.01)
            } else {
                percentage = ((range * -1) * 0.01)
            }
            val marHeigt = (totalHeight * percentage)
            Log.e("marHeigt:", marHeigt.toString())
            val marginTop = totalHeight - marHeigt
            Log.e("marginTop:", marginTop.toString())

            param.setMargins(0, marginTop.toInt() + binding.findTagToolbar.layoutParams.height, 0, 0)
            binding.pairedTag.layoutParams = param

            binding.progressIndicator.visibility = View.INVISIBLE
            binding.tvNearSearch.visibility = View.INVISIBLE
            jsonObject.addProperty("setProgress3", "setProgress:TagLabelHeight:==>" + tHeight)


            if (isBeepSound) stopSound() else playSound()

            if (lastSeenDate == null) {
                lastSeenDate = Date()
            }
            var date = lastSeenDate
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            binding.tvLastseen.text = "Last Seen: " + getUpdated(sdf.format(date).toString())
            lastSeenDate = Date()
        }


    }

    private fun getUpdated(updated : String): String? {
        var convTime: String? = null
        val suffix = "ago"
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        try {
            val date = format.parse(updated)
            val timeZone = Calendar.getInstance().timeZone.id
            val local = Date(date.time)
            val nowTime = Date()
            val dateDiff = nowTime.time - local.time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime =  "$minute seconds $suffix"
            } else if (minute < 60) {
                convTime = "$minute minutes $suffix"
            } else if (hour < 24) {
                convTime = "$hour hours $suffix"
            } else if (day >= 30) {

                convTime = if (day > 360) {
                    (day / 360).toString() + " years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " months " + suffix
                } else {
                    (day / 7).toString() + " week " + suffix
                }
            } else if (day < 7) {
                convTime = "$day days $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convTime
    }

    private fun calculatTagLocation() {

        val farViewYPos = 0
        val farViewHeight = binding.lytNear.layoutParams.height

        val nearViewYPos = binding.lytNear.layoutParams.height
        val nearViewHeight = binding.lytImmediate.layoutParams.height - binding.lytNear.layoutParams.height

        val  immediateViewYPos = binding.lytImmediate.layoutParams.height
        val totalHei = Resources.getSystem().getDisplayMetrics().heightPixels - binding.findTagToolbar.layoutParams.height
        val  immediateViewHeight =  totalHei - binding.lytImmediate.layoutParams.height

        val farUpperRange = -100
        val farlowerRange = -75

        val nearUpperRange = -74
        val nearLowerRange = -53

        val immediateUpperRange = -52
        val immediateLowerRange = -30

        addValuesToRSSIDict(farRSSILocationDictionary, farUpperRange,  farlowerRange, farViewYPos,  farViewHeight)

        addValuesToRSSIDict(nearRSSILocationDictionary, nearUpperRange,nearLowerRange,  nearViewYPos,  nearViewHeight)

        addValuesToRSSIDict(immediateRSSILocationDictionary, immediateUpperRange, immediateLowerRange,  immediateViewYPos,immediateViewHeight)

        Timber.e("")

    }

    private fun addValuesToRSSIDict
                (dict: MutableList<ItemDistance>,
                 upperRange: Int,
                 lowerRange: Int,
                 viewYPOS: Int,
                 viewHeight: Int) {

        val screenSegments = viewHeight / (lowerRange - upperRange)
        var initialYValue  = viewYPOS

        for (i in upperRange..lowerRange) {
            initialYValue += screenSegments
            dict.add(ItemDistance(i, initialYValue.toInt()))
//            print(initialYValue)
        }
    }

    private fun initializeSetTagProcess()
    {

        countDownTimer!!.start()
        binding.progressIndicator.visibility = View.VISIBLE
        binding.tvNearSearch.visibility = View.VISIBLE

    }


    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val inflatedView = View.inflate(context, R.layout.fragment_modar_mode, null)
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


}