package com.iovuework.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.iovuework.App
import com.iovuework.MainActivity
import com.iovuework.R


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class NotifyUtils {

    companion object {
        var CHANNEL_ID_Notification = "iovue_notify_ID"
        var NOTIFICATION_NAME = "iovue_notify"
        val REQUEST_CODE_CLICK_PENDING_INTENT = 0x1001

        var KEY_NOTIFICATIONID = "NotificationId"

        @JvmStatic
        fun sendNoti(
            context: Context?,
            title: String,
            content: String,
            id: Int,
            notificationid: Int
        ) {

            val manager = context!!
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notify: Notification = getNotification(context!!, title, content, id)!!

            manager.notify(notificationid, notify)
        }

        @JvmStatic
        fun sendNoti(
            context: Context?,
            title: String,
            content: String,
            pendingIntent: PendingIntent,
            notificationid: Int
        ) {

            val manager = context!!
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notify: Notification = getNotification(context!!, title, content, pendingIntent)!!

            manager.notify(notificationid, notify)
        }

        @JvmStatic
        private fun getNotification(
            context: Context?,
            title: String,
            content: String,
            id: Int
        ): Notification? {
            if (context == null) {
                return null
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID_Notification,
                    NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            val clickPendingIntent = getClickPendingIntent(context, id)
            return NotificationCompat.Builder(context, CHANNEL_ID_Notification)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setFullScreenIntent(clickPendingIntent, true)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0, 1000, 1000, 1000)) //通知栏消息震动
                .setDefaults(NotificationCompat.DEFAULT_ALL) //通知栏提示音、震动、闪灯等都设置为默认
                .setContentIntent(clickPendingIntent)
                .setOngoing(false)
                .build()
        }

        @JvmStatic
        private fun getClickPendingIntent(
            context: Context,
            id: Int
        ): PendingIntent? {

            val intent = Intent(context, MainActivity::class.java)
            intent.action = "$id"
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            if (id != -1) {
                intent.putExtra(KEY_NOTIFICATIONID, id)
            }
            return PendingIntent.getActivity(
                context,
                REQUEST_CODE_CLICK_PENDING_INTENT,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        @JvmStatic
        private fun getNotification(
            context: Context?,
            title: String,
            content: String,
            pendingIntent: PendingIntent
        ): Notification? {
            if (context == null) {
                return null
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID_Notification,
                    NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            return NotificationCompat.Builder(context, CHANNEL_ID_Notification)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0, 1000, 1000, 1000)) //通知栏消息震动
                .setDefaults(NotificationCompat.DEFAULT_ALL) //通知栏提示音、震动、闪灯等都设置为默认
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .build()
        }

        @JvmStatic
        fun getClickPendingIntent(
            context: Context,
            intent: Intent
        ): PendingIntent {

            return PendingIntent.getActivity(
                context,
                REQUEST_CODE_CLICK_PENDING_INTENT,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        @JvmStatic
        fun cancel(id: Int) {
            try {
                val notificationManager = App.context
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}