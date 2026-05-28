package com.example.bookshelf.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookshelf.data.BookItem
import com.example.bookshelf.ui.BookUIState

@Composable
fun HomeScreen(
    bookUiState: BookUIState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (bookUiState) {
        is BookUIState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is BookUIState.Success -> BooksListScreen(
            bookUiState.books, contentPadding = contentPadding, modifier = modifier.fillMaxWidth()
        )
        is BookUIState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text("Laden...")
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Fehler beim Laden.")
        Button(onClick = retryAction) {
            Text("Wiederholen")
        }
    }
}

@Composable
fun BooksListScreen(
    books: List<BookItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (books.isEmpty()) {
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
            Text("Keine Bücher gefunden.")
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = books, key = { book -> book.id }) { book ->
                BookCard(book = book, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun BookCard(book: BookItem, modifier: Modifier = Modifier) {
    val title = book.volumeInfo?.title ?: "Unbekannter Titel"
    val authors = book.volumeInfo?.authors?.joinToString(", ") ?: "Unbekannter Autor"
    val description = book.volumeInfo?.description ?: ""
    val publishedDate = book.volumeInfo?.publishedDate ?: ""
    val imageUrl = book.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://")

    Card(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Cover von $title",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.FillWidth
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )

            Text(
                text = authors,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .align(Alignment.Start)
            )

            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .align(Alignment.Start)
                )
            }

            if (publishedDate.isNotEmpty()) {
                Text(
                    text = "Veröffentlicht: $publishedDate",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .align(Alignment.Start)
                )
            }
        }
    }
}
