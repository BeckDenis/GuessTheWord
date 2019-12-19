package com.example.android.guesstheword.screens.title

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.Player
import kotlinx.android.synthetic.main.player_item.view.*

class PlayersAdapter(val clickListener: (Player) -> Unit , val deleteClickListener: (Player) -> Unit) :
    RecyclerView.Adapter<PlayersAdapter.ViewHolder>() {

    var data =  listOf<Player>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.player_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = data[position]
        holder.view.run {
            name.text = player.name
        }
        holder.itemView.setOnClickListener { clickListener(player) }
        holder.view.delete_button.setOnClickListener { deleteClickListener(player) }
    }

    override fun getItemCount() = data.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}