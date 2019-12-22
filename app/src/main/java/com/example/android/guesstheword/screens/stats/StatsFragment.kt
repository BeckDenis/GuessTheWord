package com.example.android.guesstheword.screens.stats


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.PlayersDatabase
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : Fragment() {

    private lateinit var viewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = PlayersDatabase.getInstance(application).playersDatabaseDao
        val viewModelFactory = StatsViewModelFactory(dataSource)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(StatsViewModel::class.java)

        val listAdapter = PlayersStatsAdapter { player ->
            viewModel.changePickedPlayer(player)
        }

        list_stats_players.run {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.players.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.data = it
            }
        })
        viewModel.pickedPlayer.observe(this, Observer {
            picked_player_text.text = getString(R.string.picked_player_text, it?.name ?: "None")
        })

        play_button.setOnClickListener { nextGame() }
        exit_button.setOnClickListener { goToTitle() }
    }

    private fun goToTitle() {
        activity!!.onBackPressed()
    }

    private fun nextGame() {
        findNavController().navigate(StatsFragmentDirections.actionNextGame(viewModel.pickedPlayer.value!!.playerId))
    }
}
