package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.PairRequestBody

class BaseApiHelper (private val beaconBaseAPIServices: BeaconBaseAPIServices)   {
    suspend fun getAppinfo() = beaconBaseAPIServices.getAppinfo()
    suspend fun getTrackingDevices() = beaconBaseAPIServices.getTrackingDevices()
    suspend fun pair(pairRequestBody: PairRequestBody?,tagId: String) = beaconBaseAPIServices.pair(pairRequestBody,tagId)
}