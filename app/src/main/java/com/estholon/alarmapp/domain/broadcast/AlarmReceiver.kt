package com.estholon.alarmapp.domain.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import com.estholon.alarmapp.domain.model.Alarm
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtonePlayer = RingtoneManager.getRingtone(context,ringtone)
        ringtonePlayer.play()
    }

    companion object{

        fun setAlarm(context: Context, alarm: Alarm){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,alarm.id,alarmIntent,
                PendingIntent.FLAG_IMMUTABLE)

            val alarmTime = getTimeInMillis(alarm.start_date,alarm.start_hour)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent)
        }

        fun cancelAlarm(context: Context, id: Int){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,id,alarmIntent,
                PendingIntent.FLAG_IMMUTABLE)

            alarmManager.cancel(pendingIntent)
        }

        private fun getTimeInMillis(startDate: String, startHour: String): Long {
            var calendar = Calendar.getInstance()
            var calendar2 = Calendar.getInstance()
            var hour = startHour.substringBefore(":").toIntOrNull()
            if (hour != null) {
                calendar.set(Calendar.HOUR_OF_DAY,hour)
            }
            var minutes = startHour.substringAfter(":").toIntOrNull()
            if (minutes != null) {
                calendar.set(Calendar.MINUTE,minutes)
            }

            var tmpList = startDate.split("/")

            if(tmpList.size>2) {

                var day = tmpList[0].toIntOrNull()
                if (day != null) {
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                }

                var month = tmpList[1].toIntOrNull()
                if (month != null) {
                    calendar.set(Calendar.MONTH, month)
                }

                var year = tmpList[2].toIntOrNull()
                if (year != null) {
                    calendar.set(Calendar.YEAR, year)
                }

            }

            return calendar.timeInMillis
        }

    }

}