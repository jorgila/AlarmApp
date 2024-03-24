package com.estholon.alarmapp.domain.broadcast

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.estholon.alarmapp.R
import com.estholon.alarmapp.domain.model.Alarm
import com.estholon.alarmapp.domain.permissions.PermissionUtil
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            CANCEL_ALARM_CODE ->{
                ringtonePlayer?.stop()
            }

            INCOMING_ALARM_CODE -> {
                if (PermissionUtil.checkNotificationPermission(context)) {
                    val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ringtonePlayer = RingtoneManager.getRingtone(context, ringtone)
                    ringtonePlayer?.stop()
                    ringtonePlayer?.play()
                    notifyAlarm(context)
                }
            }

            else -> {}
        }
    }

    companion object {

        var alarmManager: AlarmManager? = null
        var ringtonePlayer: Ringtone? = null
        private val CHANNEL_ID = "alarm_channel"
        private val CANCEL_ALARM_CODE = "ACTION_CANCEL_ALARM"
        private val INCOMING_ALARM_CODE = "INCOMING_ALARM"

        fun setAlarm(context: Context, alarm: Alarm) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
                action = INCOMING_ALARM_CODE
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context, alarm.id, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE
            )


            val alarmTime = getTimeInMillis(alarm.start_date, alarm.start_hour)
            val repetitionInterval =
                getRepetitionIntervalInMills(alarm.repetition_type, alarm.repetition_time, context)

            //alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent)
            repetitionInterval?.let {
                alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP, alarmTime - it,
                    it, pendingIntent
                )
            }
        }

        fun cancelAlarm(context: Context, id: Int) {
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, id, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager?.cancel(pendingIntent)
            ringtonePlayer?.stop()
        }

        private fun getTimeInMillis(startDate: String, startHour: String): Long {
            val calendar = Calendar.getInstance()
            var calendar2 = Calendar.getInstance()
            val hour = startHour.substringBefore(":").toIntOrNull()
            if (hour != null) {
                calendar.set(Calendar.HOUR_OF_DAY, hour)
            }
            val minutes = startHour.substringAfter(":").toIntOrNull()
            if (minutes != null) {
                calendar.set(Calendar.MINUTE, minutes)
            }
            calendar.set(Calendar.SECOND, 0)

            val tmpList = startDate.split("/")

            if (tmpList.size > 2) {

                val day = tmpList[0].toIntOrNull()
                if (day != null) {
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                }

                var month = tmpList[1].toIntOrNull()
                if (month != null) {
                    calendar.set(Calendar.MONTH, --month)
                }

                val year = tmpList[2].toIntOrNull()
                if (year != null) {
                    calendar.set(Calendar.YEAR, year)
                }

            }

            return calendar.timeInMillis
        }

        private fun getRepetitionIntervalInMills(
            typeInterval: String,
            timeInterval: Int,
            context: Context
        ): Long? {
            return when (typeInterval) {
                context.getString(R.string.seconds) -> TimeUnit.SECONDS.toMillis(timeInterval.toLong())
                context.getString(R.string.minutes) -> TimeUnit.MINUTES.toMillis(timeInterval.toLong())
                context.getString(R.string.hours) -> TimeUnit.HOURS.toMillis(timeInterval.toLong())
                context.getString(R.string.días) -> TimeUnit.DAYS.toMillis(timeInterval.toLong())
                else -> null

            }
        }

        @SuppressLint("MissingPermission")
        fun notifyAlarm(context: Context) {

            val name = "Alarm Channel"
            val descriptionText = "Channel for alarm notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
            }


            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = CANCEL_ALARM_CODE
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_alarm_24)
                .setContentTitle("Título")
                .setContentText("Mensaje")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_delete, "Cancelar Alarma", pendingIntent)


            NotificationManagerCompat.from(context).notify(0, builder.build())
        }
    }

}
