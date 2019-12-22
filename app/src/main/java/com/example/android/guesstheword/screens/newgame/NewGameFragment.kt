package com.example.android.guesstheword.screens.newgame


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.PlayersDatabase
import kotlinx.android.synthetic.main.fragment_new_game.*

class NewGameFragment : Fragment() {

    private lateinit var viewModel: NewGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = PlayersDatabase.getInstance(application).playersDatabaseDao
        val viewModelFactory = NewGameViewModelFactory(dataSource)
        val listAdapter = PlayersAdapter(deleteClickListener = { viewModel.deletePlayer(it) })

        players_list.run {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewGameViewModel::class.java)
        viewModel.players.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.data = it
            }
        })

        play_button.setOnClickListener { play() }
        new_player_button.setOnClickListener { addNewPlayer() }
    }

    private fun play() {
        if ((viewModel.players.value?.size ?: 0) > 1) {
            findNavController().navigate(NewGameFragmentDirections.actionNewGameToStats())
        } else {
            Toast.makeText(context, getString(R.string.new_game_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun addNewPlayer() {
        if (player_name.text.isNotEmpty()) {
            viewModel.addPlayer(player_name.text.toString())
            clearInput()
        } else {
            Toast.makeText(context, getString(R.string.name_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInput() = player_name.setText("")
}


