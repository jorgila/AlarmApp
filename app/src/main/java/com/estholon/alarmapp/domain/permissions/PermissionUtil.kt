package com.estholon.alarmapp.domain.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class PermissionUtil{
    companion object {

        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 100

        fun checkNotificationPermission(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun requestNotificationPermission(activity: AppCompatActivity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }
}