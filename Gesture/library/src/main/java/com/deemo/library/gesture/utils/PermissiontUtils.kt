package com.deemo.library.gesture.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import com.deemo.library.gesture.service.AutoService


/**
 * author： deemo
 * date:    2020-01-10
 * desc:
 */
object PermissiontUtils {


    val TAG = "PermissiontUtils"


    fun openSetting(context: Context) {

        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startServer(context: Context) {
        val intent = Intent(context, AutoService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }


    fun isAccessibilityOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        // TestService为对应的服务
        val service: String =
            mContext.packageName.toString() + "/" + AutoService::class.java.canonicalName

        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: SettingNotFoundException) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.message)
        }

        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {

            val settingValue = Settings.Secure.getString(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )

            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    Log.v(
                        TAG,
                        "-------------- > accessibilityService :: $accessibilityService $service"
                    )
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        Log.v(
                            TAG,
                            "We've found the correct setting - accessibility is switched on!"
                        )
                        return true
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***")
        }
        return false
    }


}