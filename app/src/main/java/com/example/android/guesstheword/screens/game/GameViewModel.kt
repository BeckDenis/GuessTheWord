package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.database.Player
import com.example.android.guesstheword.database.PlayersDatabaseDao
import kotlinx.coroutines.*

class GameViewModel(private val playerKey: Long, val database: PlayersDatabaseDao) : ViewModel() {

    private lateinit var wordList: MutableList<String>
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val timer: CountDownTimer

    private var _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer

    private val _gameScore = MutableLiveData<Int>()
    val gameScore: LiveData<Int>
        get() = _gameScore

    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val currentTime = MutableLiveData<Long>()
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    init {
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                currentTime.value = DONE
                onGameFinish()
            }
        }

        _word.value = ""
        _gameScore.value = 0
        initializePlayer()
        resetList()
        nextWord()
        timer.start()
    }

    companion object {
        // Time when the game is over
        private const val DONE = 0L
        // Countdown time interval
        private const val ONE_SECOND = 1000L
        // Total time for the game
        private const val COUNTDOWN_TIME = 60000L
    }

    private fun initializePlayer() {
        uiScope.launch {
            _currentPlayer.value = getPlayerFromDatabase()
        }
    }

    private suspend fun getPlayerFromDatabase(): Player? {
        return withContext(Dispatchers.IO) {
            database.get(playerKey)
        }
    }

    private suspend fun update(player: Player) {
        withContext(Dispatchers.IO) {
            database.update(player)
        }
    }

    private fun resetList() {
        wordList = words.apply { shuffle() }
    }

    private fun nextWord() {
        _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
        _gameScore.value = _gameScore.value?.minus(1)
        if (wordList.isEmpty()) resetList()
        nextWord()
    }

    fun onCorrect() {
        _gameScore.value = _gameScore.value?.plus(3)
        if (wordList.isEmpty()) resetList()
        nextWord()
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
        updatePlayer()
    }

    private fun updatePlayer() {
        _currentPlayer.value?.run {
            score = _currentPlayer.value?.score!!.plus(gameScore.value ?: 0)
            gameCount = _currentPlayer.value?.gameCount!!.plus(1)
        }

        uiScope.launch {
            update(_currentPlayer.value ?: return@launch)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}