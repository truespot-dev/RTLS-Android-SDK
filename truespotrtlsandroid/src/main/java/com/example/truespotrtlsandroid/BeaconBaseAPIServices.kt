package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.PairRequestBody
import com.example.truespotrtlsandroid.models.TSApplication
import com.example.truespotrtlsandroid.models.TSDevice
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface BeaconBaseAPIServices {

    @GET("/${API.Endpoint.applications}"+"?self")
    suspend fun getAppinfo(): TSApplication

    @GET("/${API.Endpoint.trackingDevices}")
    suspend fun getTrackingDevices(): ArrayList<TSDevice>

    @GET("/${API.Endpoint.trackingDevices}/{tagId}/pairings")
    suspend fun pair(@Body pairRequestBody: PairRequestBody?,@Path("tagId") tagId: String, ): Array<TSDevice>


}