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

    @POST("api/api-authorizations")
    fun authenticate(@Query("tenantId") tenantId: String?): Single<Response<Authen>>

    @GET("/${API.Endpoint.applications}/?self")
    fun getAppinfo(): Observable<TSApplication?>

    @GET("/${API.Endpoint.applications}/?self")
    fun getTrackingDevices(): Observable<Response<TSApplication?>?>
}