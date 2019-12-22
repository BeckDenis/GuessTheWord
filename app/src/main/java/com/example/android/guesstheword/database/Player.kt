package com.example.android.guesstheword.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players_table")
data class Player(
    @PrimaryKey(autoGenerate = true)
    var playerId: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "score")
    var score: Int = 0,

    @ColumnInfo(name = "game_count")
    var gameCount: Int = 0
)