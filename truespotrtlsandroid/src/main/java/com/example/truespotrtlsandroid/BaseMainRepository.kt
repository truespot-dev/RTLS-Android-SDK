package com.example.truespotrtlsandroid

class BaseMainRepository(private val baseApiHelper: BaseApiHelper)  {

    suspend fun getAppinfo() = baseApiHelper.getAppinfo()
    suspend fun getTrackingDevices() = baseApiHelper.getTrackingDevices()
}