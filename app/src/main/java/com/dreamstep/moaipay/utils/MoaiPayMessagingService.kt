package com.dreamstep.moaipay.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject

class MoaiPayMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MoaiPayMessagingService", "onMessageReceived")


        val dataString = remoteMessage.data["data"]

        var data: JSONObject? = null
        if (dataString != null) {
            try {
                data = JSONObject(dataString)
            } catch (e: JSONException) {
                Log.e("MoaiPayMessagingService",
                    "Ignoring push because of JSON exception while processing: $dataString", e)
                return
            }
        }

        if(data != null){
            val builder = NotificationCompat.Builder(applicationContext,applicationContext.getString(
                R.string.default_notification_channel_id))
//            builder.setSmallIcon(R.mipmap.ic_stat_flame)
            builder.setContentTitle(getString(R.string.app_name))
            builder.setContentText(data.getString("alert"))
            builder.setDefaults(
                Notification.DEFAULT_SOUND
                    or Notification.DEFAULT_VIBRATE
                    or Notification.DEFAULT_LIGHTS)
            builder.setAutoCancel(true)

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("from", "notification")
            val contentIntent = PendingIntent.getActivity(applicationContext,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            builder.setContentIntent(contentIntent)
            val nManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = applicationContext.getString(R.string.default_notification_channel_id)
                val channel = NotificationChannel(channelId, "noti", NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "hu"
                nManager?.createNotificationChannel(channel)
            }
            nManager?.notify(0,builder.build())
            EventBus.getDefault().post(builder.build())

        }

    }
}
