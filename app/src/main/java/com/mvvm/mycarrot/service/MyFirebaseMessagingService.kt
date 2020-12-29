package com.mvvm.mycarrot.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.view.MainActivity
import java.lang.Error


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null) {
            sendNotification(
                remoteMessage.notification!!.body.toString(),
                remoteMessage.notification!!.title.toString()
            )
        } else if (remoteMessage.data.isNotEmpty()) {
            val tittle = remoteMessage.data["title"].toString()
            val msg = remoteMessage.data["body"].toString()
            sendNotification(tittle, msg)
        }
    }


    fun sendNotification(tittle: String?, text: String?) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = "my_channel_01"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,channelId,NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100,200,100,200)
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }


        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle(tittle)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_carrot_push))
                .setSmallIcon(R.drawable.ic_carrot_push)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = manager.getRunningTasks(1)
        val componentName = info[0].topActivity
        val topActivitYName = componentName!!.shortClassName

        // 채팅창(ChatLogActivity)을 보고 있는 상태면, Noti 알림 X
        if(topActivitYName != ".view.ChatLogActivity")notificationManager.notify(0 , notificationBuilder.build())






    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("fhrm", "MyFirebaseMessagingService -onNewToken(),    token: ${token}")
    }
}
