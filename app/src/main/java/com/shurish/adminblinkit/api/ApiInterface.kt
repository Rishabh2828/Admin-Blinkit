package com.shurish.adminblinkit.api

import com.shurish.adminblinkit.models.Notification
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @Headers(
        "Content-Type: application/json",
    "Authorization: key=AAAAwBsPA90:APA91bFWkBIUoLFkv2cMXAXZXQKb-ERp1pQRmbA-RTgjCl7wPS_ANaK0TZROTskrbL3k3ujFvki3JcKFRnJ57CpZmuXGz-P5_uP3pGhHWSO0_lg6mLh6tKcjp4EWcRd1-1SuHaa_MGMp"
    )
    @POST("fcm/send")
    fun sendNotification(@Body notification : Notification) : Call<Notification>
}