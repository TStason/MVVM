package com.example.mvvm_example

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_example.DataClasses.Player
import com.example.mvvm_example.DataClasses.PlayerChange
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {

    private val TAG = "ViewModel"

    private var job: Job = Job()
    private var viewModelScope = CoroutineScope(Dispatchers.Default + job)
    private val repo = Repo()

    private var isUpdated = false
    private val playersLiveData: MutableLiveData<MutableList<Player>> by lazy {
        MutableLiveData<MutableList<Player>>()
    }
    private val playerIndex: MutableLiveData<PlayerChange> by lazy {
        MutableLiveData<PlayerChange>()
    }

    fun initModel(){
        if (!job.isActive){
            job = Job()
            viewModelScope = CoroutineScope(Dispatchers.Default + job)
        }
        Log.e(TAG, "Init model scope=$viewModelScope job=$job")
    }

    fun getPlayersLiveData(): LiveData<MutableList<Player>> = playersLiveData
    fun getPlayerIndex(): LiveData<PlayerChange> = playerIndex

    fun updatePlayersList(){
        stopChildrenJob()
        viewModelScope.launch{
            try{
                if (!isUpdated){
                    isUpdated = true
                    playersLiveData.postValue(repo.getPlayersAsync().await())
                    isUpdated = false
                } else {
                    Log.e(TAG, "Already updated")
                }
            } finally {
                isUpdated = false
            }
        }
    }

    fun removeAllPlayer(){
        stopChildrenJob()
        playersLiveData.postValue(mutableListOf())
    }

    fun beforeFirstOut(){
        stopChildrenJob()
        viewModelScope.launch{
            var loop = true
            var playersCount = 0
            val playersList = playersLiveData.value?.map{ it.copy() }
            playersList?.let {
                playersCount = it.size
            }
            Log.e(TAG, "Called beforeFirstOut() loop=$loop playersCount=$playersCount list=$playersList")
            while(loop && (playersCount != 0)){
                val randomDmg = (-20..5).random()
                val randomPlayer = (1..playersCount).random()-1
                playersList!![randomPlayer].hp += randomDmg
                if (playersList!![randomPlayer].hp <= 0)
                    loop = false
                Log.e(TAG, "postValue -> $randomPlayer maimed on $randomDmg")
                playerIndex.postValue(PlayerChange(randomPlayer, randomDmg))
                delay(1000L)
            }
            playerIndex.postValue(null)
        }
    }

    private fun stopChildrenJob(){
        job.children.forEach {
            Log.e(TAG, "Canceled child job: $it")
            it.cancel()
        }
    }

    fun onDestroy(){
        stopChildrenJob()
        playerIndex.value = null
        Log.e(TAG, "${playerIndex.value}")
    }
}