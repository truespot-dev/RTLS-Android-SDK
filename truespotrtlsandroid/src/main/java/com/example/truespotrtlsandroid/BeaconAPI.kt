package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Credentials
import com.fasterxml.jackson.databind.util.ISO8601Utils
import com.google.gson.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object BeaconAPI {

    fun getBeaconApi(): BeaconAPIServices {

        return getApi(BeaconAPIServices::class.java)
    }

    private fun <T> getApi(clazz: Class<T>): T {
        lateinit var retrofit: Retrofit


        val headersInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header("Authorization", "Bearer ${Credentials.clientSecret}")
            chain.proceed(requestBuilder.build())
        }


        val okHttpClient = OkHttpClient()
            .newBuilder()
            .followRedirects(true)
            .addInterceptor(headersInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(API.authURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(clazz)
    }

 /*   var DATE_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    var GSON: Gson = GsonBuilder() *//*.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")*//*
        .registerTypeAdapter(
            Date::class.java,
            JsonSerializer<Date> { date, type, context ->
                if (date == null) null else JsonPrimitive(DATE_FORMAT.format(date))
            } as JsonSerializer<Date?>?
        )
        .registerTypeAdapter(Date::class.java, label@ JsonDeserializer { json, type, context ->
            // https://github.com/google/gson/blob/master/gson/src/main/java/com/google/gson/DefaultDateTypeAdapter.java
            try {
                return@JsonDeserializer DateFormat.getDateTimeInstance(
                    DateFormat.DEFAULT,
                    DateFormat.DEFAULT, Locale.US
                ).parse(json.getAsString())
            } catch (ignored: ParseException) {
            }
            try {
                return@JsonDeserializer ISO8601Utils.parse(json.getAsString(), ParsePosition(0))
            } catch (ignored: ParseException) {
            }
            null
        } as JsonDeserializer?)
        .setLenient()
        .create()*/

}