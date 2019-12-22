package com.example.android.guesstheword.screens.newgame

import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.database.Player
import com.example.android.guesstheword.database.PlayersDatabaseDao
import kotlinx.coroutines.*

class NewGameViewModel(val database: PlayersDatabaseDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val players = database.getAllPlayers()

    fun deletePlayer(player: Player) {
        uiScope.launch {
            delete(player)
        }
    }

    private suspend fun delete(player: Player) {
        withContext(Dispatchers.IO) {
            database.delete(player)
        }
    }

    fun addPlayer(name: String) {
        uiScope.launch {
            insert(Player(name = name))
        }
    }

    private suspend fun insert(player: Player) {
        withContext(Dispatchers.IO) {
            database.insert(player)
        }
    }
}