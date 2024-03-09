package com.estholon.alarmapp.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.estholon.alarmapp.data.localDB.AlarmDao
import com.estholon.alarmapp.data.localDB.AppDatabase
import com.estholon.alarmapp.domain.broadcast.AlarmReceiver
import com.estholon.alarmapp.domain.model.Alarm
import kotlinx.coroutines.launch
import java.util.Calendar

class NewAlarmActivityViewModel(private val application: Application) : ViewModel() {

    // CONNECTIONS

    //// Dao
    private val alarmDao: AlarmDao = AppDatabase.getDatabase(application).alarmDao()

    // FUNCTIONS

    fun newAlarm(
        title: String,
        start_date: String,
        start_hour: String,
        repetition_type: String,
        repetition_time: Int,
        message: String,
        status: Boolean,
        context: Context
    ){

        viewModelScope.launch {
            var id = alarmDao.selectMaxId()?.plus(1) ?: 0
            var alarm = Alarm(
                id,
                title,
                start_date,
                start_hour,
                repetition_type,
                repetition_time,
                message,
                status
            )

            alarmDao.insertAlarm(
                alarm
            )
            initAlarm(context, alarm)
        }

    }


    private fun initAlarm(context: Context, alarm: Alarm) {
        AlarmReceiver.setAlarm(context, alarm)
    }

}

class NewAlarmActivityViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewAlarmActivityViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return NewAlarmActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}