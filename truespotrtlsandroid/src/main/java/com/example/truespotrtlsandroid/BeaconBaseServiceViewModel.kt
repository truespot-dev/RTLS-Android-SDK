package com.example.truespotrtlsandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.truespotrtlsandroid.models.PairRequestBody
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class BeaconBaseServiceViewModel(application: Application, private val mainBaseRepository: BaseMainRepository) : AndroidViewModel(application) {

    fun getAppinfo() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try
        {
            val result = mainBaseRepository.getAppinfo()
            emit(Resource.success(result))
        }catch (exception : Exception)
        {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun getTrackingDevices() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try
        {
            val result = mainBaseRepository.getTrackingDevices()
            emit(Resource.success(result))
        }catch (exception : Exception)
        {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun pair(pairRequestBody: PairRequestBody?,tagId: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try
        {
            val result = mainBaseRepository.pair(pairRequestBody,tagId)
            emit(Resource.success(result))
        }catch (exception : Exception)
        {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }


}