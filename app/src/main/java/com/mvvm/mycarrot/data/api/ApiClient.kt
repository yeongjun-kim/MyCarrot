package com.mvvm.mycarrot.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {
    private const val BASE_URL = "https://fcm.googleapis.com/"
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                val gson = GsonBuilder().setLenient().create()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}