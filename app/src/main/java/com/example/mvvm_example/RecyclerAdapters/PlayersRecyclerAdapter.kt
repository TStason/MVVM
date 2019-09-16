package com.example.mvvm_example.RecyclerAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_example.DataClasses.Player
import com.example.mvvm_example.R
import com.example.mvvm_example.ViewHolders.PlayerViewHolder

class PlayersRecyclerAdapter(val dataList: MutableList<Player?>): RecyclerView.Adapter<PlayerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.player_view_holder, parent, false))
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
}