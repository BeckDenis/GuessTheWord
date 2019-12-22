package com.example.android.guesstheword.screens.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.database.Player
import com.example.android.guesstheword.database.PlayersDatabaseDao
import kotlinx.coroutines.*

class StatsViewModel(val database: PlayersDatabaseDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val players = database.getAllPlayers()

    private val _pickedPlayer = MutableLiveData<Player>()
    val pickedPlayer: LiveData<Player>
        get() = _pickedPlayer

    init {
        uiScope.launch {
            _pickedPlayer.value = getNewestPlayer()
        }
    }

    private suspend fun getNewestPlayer(): Player? {
        return withContext(Dispatchers.IO) {
            database.getNewestPlayer()
        }
    }

    fun changePickedPlayer(player: Player) {
        _pickedPlayer.value = player
    }
}