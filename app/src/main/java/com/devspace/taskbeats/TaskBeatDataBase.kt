package com.devspace.taskbeats

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([CategoryEntity::class, TaskEntity::class], version = 2, exportSchema = false)
abstract class TaskBeatDataBase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao
    abstract fun getTaskDao(): TaskDao

}