package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Authorization
import com.example.truespotrtlsandroid.models.TSApplication
import retrofit2.Response
import retrofit2.http.*
import java.util.*
import rx.Observable

interface BeaconAPIServices {

    @POST("/${API.Endpoint.authorization}")
    suspend fun authenticate(@Query("tenantId") tenantId: String?): Observable<Response<Authorization?>?>

    @GET("/${API.Endpoint.applications}/?self")
    fun getAppinfo(): Observable<TSApplication?>

    @GET("/${API.Endpoint.applications}/?self")
    fun getTrackingDevices(): Observable<Response<TSApplication?>?>
}