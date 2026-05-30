package com.example.flightsearchapp.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.FlightSearchApplication
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.FlightRepository
import com.example.flightsearchapp.data.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SearchUiState {
    data class Suggestions(val list: List<Airport>) : SearchUiState
    data class FlightsList(val departure: Airport, val destinations: List<Airport>) : SearchUiState
    data class FavoritesList(val favorites: List<Favorite>) : SearchUiState
}

@OptIn(ExperimentalCoroutinesApi::class)
class FlightSearchViewModel(
    private val flightRepository: FlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedAirport = MutableStateFlow<Airport?>(null)

    init {
        viewModelScope.launch {
            userPreferencesRepository.searchQuery.collect { savedQuery ->
                _searchQuery.value = savedQuery
            }
        }
    }

    val favorites: StateFlow<List<Favorite>> = flightRepository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<SearchUiState> = _searchQuery
        .flatMapLatest { query ->
            val selected = _selectedAirport.value
            if (selected != null && query == selected.iataCode) {
                flightRepository.getDestinations(selected.iataCode).map { dest ->
                    SearchUiState.FlightsList(selected, dest)
                }
            } else if (query.isBlank()) {
                flightRepository.getFavorites().map { SearchUiState.FavoritesList(it) }
            } else {
                flightRepository.getSuggestions(query).map { SearchUiState.Suggestions(it) }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchUiState.FavoritesList(emptyList()))

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (_selectedAirport.value?.iataCode != query) {
            _selectedAirport.value = null
        }
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(query)
        }
    }

    fun selectAirport(airport: Airport) {
        _selectedAirport.value = airport
        _searchQuery.value = airport.iataCode
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(airport.iataCode)
        }
    }

    fun toggleFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val isFav = favorites.value.any { it.departureCode == departureCode && it.destinationCode == destinationCode }
            if (isFav) {
                flightRepository.deleteFavorite(departureCode, destinationCode)
            } else {
                flightRepository.insertFavorite(Favorite(departureCode = departureCode, destinationCode = destinationCode))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                FlightSearchViewModel(application.flightRepository, application.userPreferencesRepository)
            }
        }
    }
}
