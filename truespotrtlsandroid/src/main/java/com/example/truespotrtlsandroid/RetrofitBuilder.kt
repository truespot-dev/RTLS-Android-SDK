package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Authorization
import com.example.truespotrtlsandroid.models.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitBuilder {
    var retrofit : Retrofit? = null
    private fun getAuthURLRetrofit(): Retrofit {
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


        if(retrofit == null)
        {
            retrofit = Retrofit.Builder()
                .baseUrl(API.authURL)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build() //Doesn't require the adapter
        }
        else
        {
            if(!retrofit!!.baseUrl().equals(API.RTLSBaseURL))
            {
                retrofit = Retrofit.Builder()
                    .baseUrl(API.authURL)
                    .client(okHttpClient)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build() //Doesn't require the adapter
            }
        }
       return  retrofit!!
    }

    val apiAuthService: BeaconAPIServices = getAuthURLRetrofit().create(BeaconAPIServices::class.java)


}