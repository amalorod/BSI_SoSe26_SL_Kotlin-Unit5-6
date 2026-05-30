package com.example.flightsearchapp

import android.app.Application
import com.example.flightsearchapp.data.FlightDatabase
import com.example.flightsearchapp.data.FlightRepository
import com.example.flightsearchapp.data.UserPreferencesRepository
import com.example.flightsearchapp.data.dataStore

class FlightSearchApplication : Application() {
    val database: FlightDatabase by lazy { FlightDatabase.getDatabase(this) }
    val flightRepository: FlightRepository by lazy { FlightRepository(database.flightDao()) }
    val userPreferencesRepository: UserPreferencesRepository by lazy { 
        UserPreferencesRepository(dataStore)
    }
}
