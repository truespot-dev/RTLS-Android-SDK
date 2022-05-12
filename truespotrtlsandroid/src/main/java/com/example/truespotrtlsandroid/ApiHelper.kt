package com.example.truespotrtlsandroid

class ApiHelper (private val beaconAPIServices: BeaconAPIServices)  {

    suspend fun authenticate(tenantId: String) = beaconAPIServices.authenticate(tenantId)
}