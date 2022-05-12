package com.example.truespotrtlsandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class BeaconServiceViewModel(application: Application, private val mainRepository: MainRepository) : AndroidViewModel(application) {

    fun authenticate(tenantId: String)  = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = mainRepository.authenticate(tenantId)
            emit(Resource.success(result))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))

        }
    }


}