package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.PairRequestBody

class BaseMainRepository(private val baseApiHelper: BaseApiHelper) {

    suspend fun getAppinfo() = baseApiHelper.getAppinfo()
    suspend fun getTrackingDevices() = baseApiHelper.getTrackingDevices()
    suspend fun pair(pairRequestBody: PairRequestBody?, tagId: String) =
        baseApiHelper.pair(pairRequestBody, tagId)
}