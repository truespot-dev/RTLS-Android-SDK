package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Authorization
import com.example.truespotrtlsandroid.models.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitBuilder {

    private fun getRetrofit(statusPassURL : Boolean?): Retrofit {
        //test
        var statusPass : Boolean? = statusPassURL
        if (statusPassURL == null) statusPass = false
        val headersInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            val authorization : String = if(statusPass!!) "Basic ${Credentials.clientSecret}" else "Bearer ${Credentials.jwt}"
            requestBuilder.header("Authorization", authorization)
            chain.proceed(requestBuilder.build())
        }
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .followRedirects(true)
            .addInterceptor(headersInterceptor)
            .build()


        val passingURL : String =  if (statusPass!!) API.authURL else API.RTLSBaseURL
        val retrofit2 : Retrofit = Retrofit.Builder()
                .baseUrl(passingURL)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build() //Doesn't require the adapter
        return retrofit2
    }

    fun getRetrofitBuild(statusPassURL : Boolean?) : BeaconAPIServices
    {
      return  getRetrofit(statusPassURL).create(BeaconAPIServices::class.java)
    }
    //val apiAuthService: BeaconAPIServices = getAuthURLRetrofit().create(BeaconAPIServices::class.java)


}