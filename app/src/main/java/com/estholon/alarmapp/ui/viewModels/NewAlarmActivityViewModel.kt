package com.estholon.alarmapp.ui.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.estholon.alarmapp.data.localDB.AlarmDao
import com.estholon.alarmapp.data.localDB.AppDatabase
import com.estholon.alarmapp.domain.model.Alarm
import kotlinx.coroutines.launch

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
        status: Boolean
    ){

        viewModelScope.launch {
            alarmDao.insertAlarm(
                Alarm(
                    0,
                    title,
                    start_date,
                    start_hour,
                    repetition_type,
                    repetition_time,
                    message,
                    status
                )
            )
        }
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