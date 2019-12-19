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

class GameViewModel(
    val database: PlayersDatabaseDao
) : ViewModel() {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var currentPlayer = MutableLiveData<Player?>()

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

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

    private fun initializePlayer() {
        uiScope.launch {
            currentPlayer.value = getPlayerFromDatabase()
        }
    }

    private suspend fun getPlayerFromDatabase(): Player? {
        return withContext(Dispatchers.IO) {
            database.getNewestPlayer()
        }
    }

    private val timer: CountDownTimer

    private lateinit var wordList: MutableList<String>

    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    init {
        _word.value = ""
        _score.value = 0
        initializePlayer()
        resetList()
        nextWord()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                currentTime.value = DONE
                onGameFinish()
            }
        }

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

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }


    /** Methods for updating the UI **/

    fun onSkip() {
        _score.value = (_score.value)?.minus(1)
        if (wordList.isEmpty()) resetList()
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        if (wordList.isEmpty()) resetList()
        nextWord()
    }

    private fun nextWord() {
        _word.value = wordList.removeAt(0)
    }

    private suspend fun update(night: Player) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    fun updatePlayerScore() {
        currentPlayer.value?.score = currentPlayer.value?.score!!.plus(score.value ?: 0)

        uiScope.launch {
            update(currentPlayer.value ?: return@launch)
        }
    }

    /** Method for the game completed event **/

    private fun onGameFinish() {

        _eventGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}