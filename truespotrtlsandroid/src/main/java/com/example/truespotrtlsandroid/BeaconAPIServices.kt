package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Authen
import com.example.truespotrtlsandroid.models.Authorization
import com.example.truespotrtlsandroid.models.TSApplication
import retrofit2.Response
import retrofit2.http.*
import java.util.*
import rx.Observable
import rx.Single

interface BeaconAPIServices {

    @POST("/api/api-authorizations")
    suspend fun authenticate(@Query("tenantId") tenantId: String): Authen

    @GET("/${API.Endpoint.applications}/?self")
    fun getAppinfo(): Observable<TSApplication?>

    @GET("/${API.Endpoint.applications}/?self")
    fun getTrackingDevices(): Observable<Response<TSApplication?>?>
}