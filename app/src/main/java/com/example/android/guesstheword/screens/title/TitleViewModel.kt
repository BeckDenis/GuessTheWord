package com.example.android.guesstheword.screens.title

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.database.Player
import com.example.android.guesstheword.database.PlayersDatabaseDao
import kotlinx.coroutines.*

class TitleViewModel(
    val database: PlayersDatabaseDao
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val players = database.getAllPlayers()

    fun deletePlayer(player: Player) {
        uiScope.launch {
            clearPlayer(player)
        }
    }

    private suspend fun clearPlayer(player: Player) {
        withContext(Dispatchers.IO) {
            database.clear(player)
        }
    }

    fun addPlayer(name: String) {
        uiScope.launch {
            val player = Player(name = name)
            insert(player)
        }
    }

    private suspend fun insert(player: Player) {
        withContext(Dispatchers.IO) {
            database.insert(player)
        }
    }

}