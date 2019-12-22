package com.example.android.guesstheword.screens.newgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.guesstheword.database.PlayersDatabaseDao

class NewGameViewModelFactory(
    private val dataSource: PlayersDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewGameViewModel::class.java)) {
            return NewGameViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}