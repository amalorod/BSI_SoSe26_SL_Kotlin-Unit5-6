package com.example.bookshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BooksApplication
import com.example.bookshelf.data.BookItem
import com.example.bookshelf.data.BooksRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookUIState {
    data class Success(val books: List<BookItem>) : BookUIState
    object Error : BookUIState
    object Loading : BookUIState
}

class BookViewModel(private val booksRepository: BooksRepository) : ViewModel() {

    var bookUiState: BookUIState by mutableStateOf(BookUIState.Loading)
        private set

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            bookUiState = BookUIState.Loading
            bookUiState = try {
                BookUIState.Success(booksRepository.getBooks("jazz history"))
            } catch (e: IOException) {
                BookUIState.Error
            } catch (e: HttpException) {
                BookUIState.Error
            } catch (e: Exception) {
                BookUIState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BooksApplication)
                val booksRepository = application.container.booksRepository
                BookViewModel(booksRepository = booksRepository)
            }
        }
    }
}
