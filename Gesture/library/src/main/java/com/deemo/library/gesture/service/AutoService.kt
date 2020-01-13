package com.deemo.library.gesture.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Path
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.deemo.library.gesture.R
import com.deemo.library.gesture.bean.SwipeBean
import com.deemo.library.gesture.bean.TapBean
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * author： deemo
 * date:    2020-01-10
 * desc:
 */
class AutoService : AccessibilityService() {

    val TAG = "AutoService"

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)

        Log.d(TAG, "onCreate")

        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            Log.d(TAG, "onStartCommand")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTapEvent(event: TapBean) {
        Log.d(TAG, "onTapEvent $event")
        val path = Path()
        path.moveTo(event.x, event.y)
        path.lineTo(event.x, event.y)
        val builder = GestureDescription.Builder()
        builder.addStroke(StrokeDescription(path, 10L, 10L))
        val gestureDescription = builder.build()
        dispatchGesture(gestureDescription, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
            }
        }, null)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSwipeEvent(bean: SwipeBean) {
        Log.d(TAG, "onTapEvent $bean")
        val path = Path()

        bean.positions.forEachIndexed { index, triple ->
            if (index == 0) {
                path.moveTo(triple.first, triple.second)
            } else {
                path.lineTo(triple.first, triple.second)
            }
        }

        val builder = GestureDescription.Builder()
        builder.addStroke(StrokeDescription(path, 10L, bean.duration))
        val gestureDescription = builder.build()
        dispatchGesture(gestureDescription, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
            }
        }, null)
    }

    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent) {}

    override fun onInterrupt() {}


    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent().let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this,
                notificationChannelId
            ) else Notification.Builder(this)

        return builder
            .setContentTitle("Endless Service")
            .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_round)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}