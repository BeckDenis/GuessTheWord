package com.example.android.guesstheword.screens.title


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

        viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(TitleViewModel::class.java)

        val adapter = PlayersAdapter(deleteClickListener = { viewModel.deletePlayer(it) },
            clickListener = { })

        players_list.adapter = adapter

        viewModel.players.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        play_game_button.setOnClickListener {
            if ((viewModel.players.value?.size ?: 0) > 1) {
                findNavController().navigate(TitleFragmentDirections.actionTitleToGame())
            }
        }

        new_player_button.setOnClickListener { addNewPlayer() }
    }

    private fun addNewPlayer() {
        if (player_name.text.isNotEmpty()) {
            viewModel.addPlayer(player_name.text.toString())
        } else {
            Toast.makeText(context, getString(R.string.name_error), Toast.LENGTH_SHORT).show()
        }
    }
}
