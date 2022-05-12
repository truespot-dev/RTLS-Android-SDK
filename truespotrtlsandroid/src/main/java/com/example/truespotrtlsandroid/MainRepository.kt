package com.example.truespotrtlsandroid

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun authenticate(tenantId: String) = apiHelper.authenticate(tenantId)
}