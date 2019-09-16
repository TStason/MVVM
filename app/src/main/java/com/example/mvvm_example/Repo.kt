package com.example.mvvm_example

import com.example.mvvm_example.DataClasses.Player
import kotlinx.coroutines.*

class Repo {

    private val defaultData = listOf(
        Player("Jora", 100),
        Player("Vitya", 100),
        Player("Kostya", 100),
        Player("Pasha", 100),
        Player("Dima", 100)
    )

    suspend fun getPlayersAsync(): Deferred<MutableList<Player>> = GlobalScope.async(Dispatchers.IO){
        /*
        Request data
         */
        delay(5000L)
        defaultData.map { it.copy() }.toMutableList()
    }
}