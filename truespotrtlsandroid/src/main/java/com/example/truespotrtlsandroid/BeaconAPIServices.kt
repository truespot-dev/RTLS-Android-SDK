package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Authorization
import retrofit2.http.*

interface BeaconAPIServices {

    @POST("/${API.Endpoint.authorization}")
    suspend fun authenticate(@Query("tenantId") tenantId: String): Authorization

}