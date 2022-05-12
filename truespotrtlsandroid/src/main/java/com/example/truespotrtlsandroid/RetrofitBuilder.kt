package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitBuilder {
    private fun getRetrofit(): Retrofit {
        val headersInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header("Authorization", "Basic ${Credentials.clientSecret}")
            chain.proceed(requestBuilder.build())
        }
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .followRedirects(true)
            .addInterceptor(headersInterceptor)
            .build()


        return Retrofit.Builder()
            .baseUrl(API.authURL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .build() //Doesn't require the adapter
    }

    val apiService: BeaconAPIServices = getRetrofit().create(BeaconAPIServices::class.java)
}