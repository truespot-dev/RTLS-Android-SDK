package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Authorization
import com.example.truespotrtlsandroid.models.TSApplication
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*
import rx.Observable

interface BeaconAPIServices {

    @POST("/${API.Endpoint.authorization}")
    fun authenticate(@Query("tenantId") tenantId: String?): Observable<Authorization?>

    @GET("/${API.Endpoint.applications}/?self")
    fun getAppinfo(): Observable<TSApplication?>

    @GET("/${API.Endpoint.applications}/?self")
    fun getTrackingDevices(): Observable<Response<TSApplication?>?>
}