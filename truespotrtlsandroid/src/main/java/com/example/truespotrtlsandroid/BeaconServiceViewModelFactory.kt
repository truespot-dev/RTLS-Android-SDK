package com.example.truespotrtlsandroid

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BeaconServiceViewModelFactory constructor(private val application: Application, private val apiHelper: ApiHelper):
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BeaconServiceViewModel::class.java) -> {
                BeaconServiceViewModel(application, MainRepository(apiHelper)) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}