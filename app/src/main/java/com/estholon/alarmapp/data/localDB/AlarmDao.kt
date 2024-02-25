package com.estholon.alarmapp.data.localDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estholon.alarmapp.domain.model.Alarm

@Dao
interface AlarmDao {

    @Query("SELECT * FROM Alarm")
    fun selectAlarmAll(): MutableList<Alarm>

    @Query("DELETE FROM Alarm")
    fun deleteAlarmAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(
        alarm: Alarm
    )

}