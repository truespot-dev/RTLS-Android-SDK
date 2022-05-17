package com.example.truespotrtlsandroid

class BaseApiHelper (private val beaconBaseAPIServices: BeaconBaseAPIServices)   {
    suspend fun getAppinfo() = beaconBaseAPIServices.getAppinfo()
}