package com.habiba.habitopia.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [TaskEntity::class, HabitsEntity::class],
    version = 2,  // تأكدي إنك زودتي هنا رقم النسخة!
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO
    abstract fun habitsDao(): HabitsDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitopia_database"
                )
                    .fallbackToDestructiveMigration()  // هنا السطر المهم
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

