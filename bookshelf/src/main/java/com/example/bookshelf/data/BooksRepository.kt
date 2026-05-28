package com.example.bookshelf.data

interface BooksRepository {
    suspend fun getBooks(query: String = "jazz history"): List<BookItem>
}

class NetworkBooksRepository(
    private val booksApiService: BooksApiService
) : BooksRepository {

    override suspend fun getBooks(query: String): List<BookItem> {
        return booksApiService.searchBooks(query).items ?: emptyList()
    }
}
