package com.example.busscheduleapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.busscheduleapp.BusScheduleApplication
import com.example.busscheduleapp.data.BusSchedule
import com.example.busscheduleapp.data.BusScheduleDao
import kotlinx.coroutines.flow.Flow

/*
 * View model for Bus Schedule
 * contains methods to access Room DB through [busScheduleDao]
 */
class BusScheduleViewModel(private val busScheduleDao: BusScheduleDao): ViewModel() {
    // Get full bus schedule from Room DB
    fun getFullSchedule(): Flow<List<BusSchedule>> = busScheduleDao.getAll()
    // Get bus schedule based on the stop name from Room DB
    fun getScheduleFor(stopName: String): Flow<List<BusSchedule>> =
        busScheduleDao.getByStopName(stopName)

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BusScheduleApplication)
                BusScheduleViewModel(application.database.busScheduleDao())
            }
        }
    }
}