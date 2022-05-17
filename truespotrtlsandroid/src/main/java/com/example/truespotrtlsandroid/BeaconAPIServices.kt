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

    @POST("/${API.Endpoint.authorization}")
    suspend fun authenticate(@Query("tenantId") tenantId: String): Authorization

}