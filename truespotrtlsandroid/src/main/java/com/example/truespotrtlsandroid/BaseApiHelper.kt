package com.example.truespotrtlsandroid

class BaseApiHelper (private val beaconBaseAPIServices: BeaconBaseAPIServices)   {
    suspend fun getAppinfo() = beaconBaseAPIServices.getAppinfo()
    suspend fun getTrackingDevices() = beaconBaseAPIServices.getTrackingDevices()
    suspend fun pair(tagId: String) = beaconBaseAPIServices.pair(tagId)
}