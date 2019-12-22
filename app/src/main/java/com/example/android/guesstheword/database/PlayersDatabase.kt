package com.example.android.guesstheword.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Player::class], version = 2, exportSchema = false)
abstract class PlayersDatabase : RoomDatabase() {

    abstract val playersDatabaseDao: PlayersDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: PlayersDatabase? = null

        fun getInstance(context: Context): PlayersDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PlayersDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}