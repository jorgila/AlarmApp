package com.estholon.alarmapp.ui.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.estholon.alarmapp.data.localDB.AlarmDao
import com.estholon.alarmapp.data.localDB.AppDatabase
import com.estholon.alarmapp.domain.model.Alarm
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivityViewModel(val application: Application) : ViewModel() {

    // CONNECTION
    private val alarmDao : AlarmDao = AppDatabase.getDatabase(application).alarmDao()

    // VARIABLES

    var listOfAlarms : MutableLiveData<MutableList<Alarm>> = MutableLiveData()
        private set

    fun refresh(){
        viewModelScope.launch {
            listOfAlarms.value = alarmDao.selectAlarmAll().sortedBy {
                getTimeInMillis(it.start_date,it.start_hour)
            }.toMutableList()

        }

    }

    private fun getTimeInMillis(startDate: String, startHour: String): Long {
        var calendar = Calendar.getInstance()
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

    fun deleteAlarm(id: Int) {
        viewModelScope.launch {
            alarmDao.deleteAlarm(id)
        }
        refresh()
    }

    fun changeStatus(id: Int, status: Boolean) {
        viewModelScope.launch {
            alarmDao.changeStatus(id, status)
        }
        refresh()
    }

}

class MainActivityViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}