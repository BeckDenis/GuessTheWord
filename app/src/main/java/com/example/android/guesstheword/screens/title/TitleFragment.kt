package com.example.android.guesstheword.screens.title


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.PlayersDatabase
import kotlinx.android.synthetic.main.fragment_title.*

class TitleFragment : Fragment() {

    private lateinit var viewModel: TitleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = PlayersDatabase.getInstance(application).playersDatabaseDao
        val viewModelFactory = TitleViewModelFactory(dataSource)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TitleViewModel::class.java)

        viewModel.players.observe(viewLifecycleOwner, Observer {
            game_continue_button.isEnabled = when {
                it.size > 1 ->  true
                else -> false
            }
        })

        new_game_button.setOnClickListener { newGame() }
        game_continue_button.setOnClickListener { continueGame() }
    }

    private fun newGame() {
        viewModel.onClear()
        findNavController().navigate(TitleFragmentDirections.actionTitleToNewGame())
    }

    private fun continueGame() {
        findNavController().navigate(TitleFragmentDirections.actionTitleToStats())
    }
}
