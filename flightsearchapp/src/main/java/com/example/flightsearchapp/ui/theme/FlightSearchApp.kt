package com.example.flightsearchapp.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchApp(viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { paddingValues ->
        FlightSearchScreen(
            searchQuery = searchQuery,
            uiState = uiState,
            favorites = favorites,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onAirportSelect = { viewModel.selectAirport(it) },
            onFavoriteToggle = { dep, dest -> viewModel.toggleFavorite(dep, dest) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}
