package com.example.truespotrtlsandroid

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BeaconBaseServiceViewModelFactory constructor(
    private val application: Application,
    private val baseApiHelper: BaseApiHelper
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BeaconBaseServiceViewModel::class.java) -> {
                BeaconBaseServiceViewModel(application, BaseMainRepository(baseApiHelper)) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}