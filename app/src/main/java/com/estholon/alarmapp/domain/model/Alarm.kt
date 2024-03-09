package com.estholon.alarmapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Alarm")
data class Alarm (
    @PrimaryKey(autoGenerate = false) var id: Int,
    @ColumnInfo(name="title") var title: String,
    @ColumnInfo(name="start_date") var start_date: String,
    @ColumnInfo(name="start_hour") var start_hour: String,
    @ColumnInfo(name="repetition_type") var repetition_type: String,
    @ColumnInfo(name="repetition_time") var repetition_time: Int,
    @ColumnInfo(name="message") var message: String,
    @ColumnInfo(name="status") var status: Boolean
)