package com.example.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.screen.HomeScreen
import com.example.bookshelf.ui.BookViewModel
import com.example.bookshelf.ui.theme.BookShelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookShelfTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BookApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BookApp(modifier: Modifier = Modifier) {
    val bookViewModel: BookViewModel = viewModel(factory = BookViewModel.Factory)
    HomeScreen(
        bookUiState = bookViewModel.bookUiState,
        retryAction = bookViewModel::getBooks,
        modifier = modifier
    )
}
