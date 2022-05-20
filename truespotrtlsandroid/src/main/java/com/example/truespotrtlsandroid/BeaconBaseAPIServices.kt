package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.TSApplication
import com.example.truespotrtlsandroid.models.TSDevice
import retrofit2.http.GET

interface BeaconBaseAPIServices {

    @GET("/${API.Endpoint.applications}"+"?self")
    suspend fun getAppinfo(): TSApplication

    @GET("/${API.Endpoint.trackingDevices}")
    suspend fun getTrackingDevices(): TSDevice


}