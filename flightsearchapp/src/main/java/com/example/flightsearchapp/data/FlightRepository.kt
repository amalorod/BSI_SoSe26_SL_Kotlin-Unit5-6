package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

class FlightRepository(private val flightDao: FlightDao) {
    fun getSuggestions(query: String): Flow<List<Airport>> =
        flightDao.getAutocompleteSuggestions("%$query%")

    fun getDestinations(departureCode: String): Flow<List<Airport>> =
        flightDao.getAllDestinations(departureCode)

    fun getFavorites(): Flow<List<Favorite>> =
        flightDao.getAllFavorites()

    suspend fun insertFavorite(favorite: Favorite) =
        flightDao.insertFavorite(favorite)

    suspend fun deleteFavorite(departure: String, destination: String) =
        flightDao.deleteFavorite(departure, destination)
}
