package com.example.android.guesstheword.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlayersDatabaseDao {
    @Insert
    fun insert(night: Player)

    @Update
    fun update(night: Player)

    @Query("SELECT * from players_table WHERE playerId = :key")
    fun get(key: Long): Player?

    @Query("DELETE FROM players_table")
    fun clearAll()

    @Delete
    fun clear(model: Player)

    @Query("SELECT * FROM players_table ORDER BY playerId DESC LIMIT 1")
    fun getNewestPlayer(): Player?

    @Query("SELECT * FROM players_table ORDER BY playerId DESC")
    fun getAllPlayers(): LiveData<List<Player>>
}