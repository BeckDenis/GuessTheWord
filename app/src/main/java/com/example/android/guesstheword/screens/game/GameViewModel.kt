package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

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
        Log.i("GameViewModel", "GameViewModel created!")
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


    /** Method for the game completed event **/

    private fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}