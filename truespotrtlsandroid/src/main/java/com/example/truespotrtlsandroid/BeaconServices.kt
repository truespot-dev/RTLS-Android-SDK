package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Authorization
import com.example.truespotrtlsandroid.models.Credentials
import com.example.truespotrtlsandroid.models.TSApplication
import com.example.truespotrtlsandroid.models.TSDevice
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type


object BeaconServices {

    fun authenticate(completion: (exception: Exception?) -> Unit) {
        val url = API.authURL + API.Endpoint.authorization + "?tenantId=${Credentials.tenantId}"
        // add parameter
        val formBody = FormBody.Builder().add("tenantId", Credentials.tenantId)
            .build()
        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Basic ${Credentials.clientSecret}")
            .addHeader("Content-Type", "application/json")
            .post(formBody)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion.invoke(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.peekBody(Long.MAX_VALUE)
                        val auth: Authorization =
                            Gson().fromJson(responseBody.string(), Authorization::class.java)
                        Credentials.jwt = auth.jwt
                        getAppinfo()
                        getTrackingDevices { devices, exception ->
                            completion.invoke(exception)
                        }
                    } else {
                        throw Exception(response.message)
                    }
                } catch (exception: Exception) {
                    completion.invoke(exception)
                }
            }
        })
    }

    fun getAppinfo() {
        val url = API.RTLSBaseURL + API.Endpoint.applications + "?self"
        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .addHeader("Content-Type", "application/json")
            .get()
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.peekBody(Long.MAX_VALUE)
                    val tsApplication: TSApplication =
                        Gson().fromJson(responseBody.string(), TSApplication::class.java)
                    Credentials.appInfo = tsApplication
                    TSLocationManager.startScanning()
                }
            }
        })
    }

    fun getTrackingDevices(completion: (devices: ArrayList<TSDevice>, exception: Exception?) -> Unit) {
        val url = API.RTLSBaseURL + API.Endpoint.trackingDevices
        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .addHeader("Content-Type", "application/json")
            .get()
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion.invoke(arrayListOf(), e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.peekBody(Long.MAX_VALUE)
                        val deviceListType: Type = object : TypeToken<ArrayList<TSDevice>>() {}.type

                        val result = Gson().fromJson<ArrayList<TSDevice>>(
                            responseBody.string(),
                            deviceListType
                        )

                        val device = if (result.isNullOrEmpty()) {
                            arrayListOf()
                        } else {
                            result
                        }
                        TSBeaconManagers.updateTrackingDevices(device)
                        completion.invoke(device, null)
                    } else {
                        throw Exception(response.message)
                    }
                } catch (exception: Exception) {
                    completion.invoke(arrayListOf(), exception)
                }
            }
        })
    }

    fun pair(
        assetIdentifier: String,
        assetType: String,
        tagId: String,
        completion: (devices: TSDevice?, exception: Exception?) -> Unit
    ) {
        val url = API.RTLSBaseURL + API.Endpoint.trackingDevices + "/${tagId}/pairings"

        // add parameter
        val formBody =
            FormBody.Builder().add("assetIdentifier", assetIdentifier).add("assetType", assetType)
                .build()

        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .addHeader("Content-Type", "application/json")
            .post(formBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion.invoke(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.peekBody(Long.MAX_VALUE)
                        val device = Gson().fromJson(
                           responseBody.string(),
                            TSDevice::class.java
                        ) as TSDevice
                        completion.invoke(device, null)
                    } else {
                        throw Exception(response.message)
                    }
                } catch (exception: Exception) {
                    completion.invoke(null, exception)
                }
            }
        })
    }

    fun unpair(deviceID: String, pairingId: String, completion: (exception: Exception?) -> Unit) {
        val url =
            API.RTLSBaseURL + API.Endpoint.trackingDevices + "/${deviceID}/pairings/${pairingId}"
        // creating request
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${Credentials.jwt}")
            .addHeader("Content-Type", "application/json")
            .delete()
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion.invoke(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        completion.invoke(null)
                    } else {
                        throw Exception(response.message)
                    }
                } catch (exception: Exception) {
                    completion.invoke(exception)
                }

            }

        })

    }
}

enum class TSEnvironment {
    dev,
    prod

}


object API {

    var environment: TSEnvironment = TSEnvironment.prod
    val authURL = when (environment) {
        TSEnvironment.dev -> "https://authprovider-d-us-c-api.azurewebsites.net/"
        TSEnvironment.prod -> "https://auth.truespot.com/"
    }

    val RTLSBaseURL = when (environment) {
        TSEnvironment.dev -> "https://rtls-d-us-c-api.azurewebsites.net/"
        TSEnvironment.prod -> "https://rtls.truespot.com/"
    }

    object Endpoint {
        const val authorization = "api/api-authorizations"
        const val trackingDevices = "api/tracking-devices"
        const val applications = "api/applications"
    }
}