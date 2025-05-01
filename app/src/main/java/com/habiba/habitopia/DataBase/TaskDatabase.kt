package com.habiba.habitopia.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//I had changed the schema two times
@Database(entities = [TaskEntity::class], version = 4, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDAO

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ) .fallbackToDestructiveMigration() /* as i don't pay attention to the migration from version 1 to 2 , we can delete the old one "*/
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
