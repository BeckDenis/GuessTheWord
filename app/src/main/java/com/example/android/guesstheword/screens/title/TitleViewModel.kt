package com.example.android.guesstheword.screens.title

import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.database.PlayersDatabaseDao
import kotlinx.coroutines.*

class TitleViewModel(val database: PlayersDatabaseDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val players = database.getAllPlayers()

    fun onClear() {
        uiScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clearAll()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}