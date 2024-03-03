package com.estholon.alarmapp.data.localDB

import android.app.Application
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.estholon.alarmapp.domain.model.Alarm

@Dao
interface AlarmDao {

    @Query("SELECT * FROM Alarm")
    suspend fun selectAlarmAll(): MutableList<Alarm>

    @Query("DELETE FROM Alarm WHERE id = :id")
    suspend fun deleteAlarm(id : Int)

    @Query("DELETE FROM Alarm")
    fun deleteAlarmAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(
        alarm: Alarm
    )

    @Query("UPDATE Alarm SET status = :status WHERE id = :id")
    suspend fun changeStatus(id: Int, status: Boolean)

}