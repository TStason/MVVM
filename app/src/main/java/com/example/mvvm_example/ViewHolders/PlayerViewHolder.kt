package com.example.mvvm_example.ViewHolders

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_example.DataClasses.Player
import com.example.mvvm_example.R
import com.example.mvvm_example.CustomViews.Bar

class PlayerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val TAG = "PlayerViewHolder"

    private val text1 = view.findViewById<TextView>(R.id.textView1)
    private val text2 = view.findViewById<TextView>(R.id.textView2)
    private val bar = view.findViewById<Bar>(R.id.bar)

    fun bind(player: Player?){
        player?.let{
            text1.text = it.name
            text2.text = it.hp.toString()
            bar.setCurrentValue(it.hp)
            bar.redrawBar()
            Log.e(TAG, "called bind")
        }
    }
}