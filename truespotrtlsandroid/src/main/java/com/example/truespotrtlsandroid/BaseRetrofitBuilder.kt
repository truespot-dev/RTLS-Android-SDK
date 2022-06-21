package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object BaseRetrofitBuilder {
    private fun getBaseURLRetrofit(): Retrofit {
        val headersInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header("Authorization", "Bearer ${Credentials.jwt}")
            chain.proceed(requestBuilder.build())
        }
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .followRedirects(true)
            .addInterceptor(headersInterceptor)
            .build()


        return Retrofit.Builder()
            .baseUrl(API.RTLSBaseURL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .build() //Doesn't require the adapter
    }

    val apiBaseService: BeaconBaseAPIServices =
        getBaseURLRetrofit().create(BeaconBaseAPIServices::class.java)
}