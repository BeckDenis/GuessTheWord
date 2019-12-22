package com.example.android.guesstheword.screens.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.Player
import kotlinx.android.synthetic.main.item_stats_player.view.*

class PlayersStatsAdapter(val clickListener:(Player) -> Unit) :
    RecyclerView.Adapter<PlayersStatsAdapter.ViewHolder>() {

    var data =  listOf<Player>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stats_player, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = data[position]
        holder.view.run {
            table_player_name.text = player.name
            table_game_count.text = player.gameCount.toString()
            table_score.text = player.score.toString()
        }
        holder.itemView.setOnClickListener { clickListener(player) }
    }

    override fun getItemCount() = data.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}