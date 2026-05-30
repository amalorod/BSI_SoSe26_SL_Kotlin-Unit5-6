package com.example.flightsearchapp.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.R
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite

@Composable
fun FlightSearchScreen(
    searchQuery: String,
    uiState: SearchUiState,
    favorites: List<Favorite>,
    onQueryChange: (String) -> Unit,
    onAirportSelect: (Airport) -> Unit,
    onFavoriteToggle: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is SearchUiState.FavoritesList -> {
                    FavoritesListView(favorites = uiState.favorites, onFavoriteToggle = onFavoriteToggle)
                }
                is SearchUiState.Suggestions -> {
                    SuggestionsListView(suggestions = uiState.list, onAirportSelect = onAirportSelect)
                }
                is SearchUiState.FlightsList -> {
                    FlightsListView(
                        departure = uiState.departure,
                        destinations = uiState.destinations,
                        favorites = favorites,
                        onFavoriteToggle = onFavoriteToggle
                    )
                }
            }
        }
    }
}

@Composable
fun SuggestionsListView(suggestions: List<Airport>, onAirportSelect: (Airport) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(suggestions, key = { it.id }) { airport ->
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onAirportSelect(airport) }.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = airport.iataCode, fontWeight = FontWeight.Bold)
                Text(text = airport.name)
            }
        }
    }
}

@Composable
fun FlightsListView(
    departure: Airport,
    destinations: List<Airport>,
    favorites: List<Favorite>,
    onFavoriteToggle: (String, String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.search_results, departure.iataCode),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(destinations, key = { it.id }) { destination ->
                val isFav = favorites.any { it.departureCode == departure.iataCode && it.destinationCode == destination.iataCode }
                FlightRowItem(
                    depCode = departure.iataCode, depName = departure.name,
                    destCode = destination.iataCode, destName = destination.name,
                    isFavorite = isFav, onFavoriteToggle = onFavoriteToggle
                )
            }
        }
    }
}

@Composable
fun FavoritesListView(favorites: List<Favorite>, onFavoriteToggle: (String, String) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.favorite_routes),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (favorites.isEmpty()) {
            Text(text = stringResource(R.string.no_favorites), style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(favorites, key = { "${it.departureCode}-${it.destinationCode}" }) { fav ->
                    FlightRowItem(
                        depCode = fav.departureCode, depName = "",
                        destCode = fav.destinationCode, destName = "",
                        isFavorite = true, onFavoriteToggle = onFavoriteToggle
                    )
                }
            }
        }
    }
}

@Composable
fun FlightRowItem(
    depCode: String, depName: String,
    destCode: String, destName: String,
    isFavorite: Boolean,
    onFavoriteToggle: (String, String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.depart), style = MaterialTheme.typography.labelSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = depCode, fontWeight = FontWeight.Bold)
                    if (depName.isNotBlank()) Text(text = depName)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.arrive), style = MaterialTheme.typography.labelSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = destCode, fontWeight = FontWeight.Bold)
                    if (destName.isNotBlank()) Text(text = destName)
                }
            }
            IconButton(onClick = { onFavoriteToggle(depCode, destCode) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}
