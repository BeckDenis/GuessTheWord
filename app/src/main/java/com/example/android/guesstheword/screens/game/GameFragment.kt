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
import kotlinx.android.synthetic.main.fragment_game.*

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        viewModel.word.observe(this, Observer { newWord ->
            word_text.text = newWord
        })

        viewModel.score.observe(this, Observer { newScore ->
            score_text.text = newScore.toString()
        })

        // Observer for the Game finished event
        viewModel.eventGameFinish.observe(this, Observer<Boolean> { hasFinished ->
            if (hasFinished) gameFinished()
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

    private fun gameFinished() {
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
            NavHostFragment.findNavController(this)
                .navigate(GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0))
        viewModel.onGameFinishComplete()
    }
}
