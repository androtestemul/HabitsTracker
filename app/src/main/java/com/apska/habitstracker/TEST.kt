package com.apska.habitstracker

import android.util.Log
import com.apska.habitstracker.network.HabitApi
import com.apska.habitstracker.repository.Repository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    GlobalScope.launch {
        val habitResponse = HabitApi.habitApiService.getHabits()

        if (habitResponse.isSuccessful) {
            val habits = habitResponse.body()

            val remoteHabitsMap = habits?.associateBy { it.uid } as HashMap

            val notActual = remoteHabitsMap.values.map { it.copy(isActual = true) }

            println("main: $notActual")
        }

    }

    Thread.sleep(2000L)

}