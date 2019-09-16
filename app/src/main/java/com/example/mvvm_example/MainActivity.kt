package com.example.mvvm_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_example.DataClasses.Player
import com.example.mvvm_example.DataClasses.PlayerChange
import com.example.mvvm_example.RecyclerAdapters.PlayersRecyclerAdapter
import com.example.mvvm_example.ViewHolders.PlayerViewHolder
import leakcanary.AppWatcher

class MainActivity : AppCompatActivity() {

    private val TAG = "View"

    private lateinit var viewModel: MainViewModel

    private lateinit var text1: TextView
    private lateinit var text2: TextView
    private lateinit var text3: TextView
    private lateinit var recycler: RecyclerView

    private val dataList = MutableList<Player?>(0){null}

    private lateinit var adapter: RecyclerView.Adapter<PlayerViewHolder>
    private lateinit var recyclerListObserver: Observer<MutableList<Player>>
    private lateinit var playerIndexObserver: Observer<PlayerChange>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppWatcher.objectWatcher.watch(this)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.initModel()
        text1 = findViewById(R.id.text1)
        text1.apply{
            setOnClickListener {
                viewModel.updatePlayersList()
            }
        }
        text2 = findViewById(R.id.text2)
        text2.apply{
            setOnClickListener {
                viewModel.removeAllPlayer()
            }
        }
        text3 = findViewById(R.id.text3)
        text3.apply{
            setOnClickListener {
                viewModel.beforeFirstOut()
            }
        }
        recycler = findViewById(R.id.recycler)
        adapter = PlayersRecyclerAdapter(dataList)
        recycler.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = this@MainActivity.adapter
        }
        recyclerListObserver = Observer {
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
        }
        playerIndexObserver = Observer{ playerChange ->
            playerChange?.let{
                dataList[it.id]!!.hp += it.changeOn
                adapter.notifyItemChanged(it.id)
            }
        }
        viewModel.getPlayerIndex().observe(this, playerIndexObserver)
        viewModel.getPlayersLiveData().observe(this, recyclerListObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }
}
