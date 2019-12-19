package com.example.android.guesstheword.screens.game


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment

import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.PlayersDatabase
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_title.*

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application

        val dataSource = PlayersDatabase.getInstance(application).playersDatabaseDao

        val viewModelFactory = GameViewModelFactory(dataSource)

        viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(GameViewModel::class.java)

        viewModel.word.observe(this, Observer { newWord ->
            word_text.text = getString(R.string.quote_format, newWord)
        })

        viewModel.score.observe(this, Observer { newScore ->
            score_text.text = getString(R.string.score_format, newScore)
        })

        viewModel.eventGameFinish.observe(this, Observer<Boolean> { hasFinished ->
            if (hasFinished) gameFinished()
        })

        viewModel.currentTimeString.observe(this, Observer { time ->
            timer_text.text = time
        })

        correct_button.setOnClickListener { onCorrect() }
        skip_button.setOnClickListener { onSkip() }
        end_game_button.setOnClickListener { onEndGame() }
    }

    private fun onSkip() {
        viewModel.onSkip()
    }

    private fun onCorrect() {
        viewModel.onCorrect()
    }

    private fun onEndGame() {
        gameFinished()
    }

    private fun updatePlayerScore() {
        viewModel.updatePlayerScore()
    }

    private fun gameFinished() {
        updatePlayerScore()
        NavHostFragment.findNavController(this)
            .navigate(GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0))
        viewModel.onGameFinishComplete()
    }
}
