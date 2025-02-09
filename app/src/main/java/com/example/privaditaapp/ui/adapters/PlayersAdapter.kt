package com.example.privaditaapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.privaditaapp.R
import com.example.privaditaapp.ui.models.Players

class PlayersAdapter(private val onItemClick: (Players) -> Unit) : ListAdapter<Players, PlayersAdapter.PlayerViewHolder>(PlayerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlayerViewHolder(itemView: View, private val onItemClick: (Players) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.playerName)
        private val scoreTextView: TextView = itemView.findViewById(R.id.playerScore)

        fun bind(player: Players) {
            nameTextView.text = player.name
            scoreTextView.text = player.elo.toString()
            itemView.setOnClickListener { onItemClick(player) }
        }
    }

    class PlayerDiffCallback : DiffUtil.ItemCallback<Players>() {
        override fun areItemsTheSame(oldItem: Players, newItem: Players): Boolean {
            return oldItem.idPlayer == newItem.idPlayer
        }

        override fun areContentsTheSame(oldItem: Players, newItem: Players): Boolean {
            return oldItem == newItem
        }
    }
}