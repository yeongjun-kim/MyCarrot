package com.mvvm.mycarrot.data.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


const val SERVER_KEY =
    "AAAALuWY7vQ:APA91bH94krn2VRpcwvxfyEPB_2sLM5docEQMunMI4hNb3M3yDSnUtG3zkqzLzd7WHDK01LMr3bjbQgPrbJN1g04A2Alz9bwPqY_KeVTjHvqwjjwZASaA3dK2Quzx-45RzMP96Hpvw8-"

interface Api {
    @Headers("Authorization: key=${SERVER_KEY}", "Content-Type: application/json")
    @POST("fcm/send")

    fun sendNotification(
        @Body root: NotificationBody
    ): Call<ResponseBody>
}

