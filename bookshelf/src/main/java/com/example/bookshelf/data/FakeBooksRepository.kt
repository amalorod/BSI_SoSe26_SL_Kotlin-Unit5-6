package com.example.bookshelf.data

import android.content.Context
import com.example.bookshelf.R

class FakeBooksRepository(private val context: Context) : BooksRepository {
    override suspend fun getBooks(query: String): List<BookItem> {
        return listOf(
            BookItem(
                id = "1",
                volumeInfo = VolumeInfo(
                    title = context.getString(R.string.title_1),
                    authors = listOf(context.getString(R.string.author_1)),
                    imageLinks = ImageLinks(thumbnail = "https://images.unsplash.com/photo-1552847340-1e26a6af19d4?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                )
            ),
            BookItem(
                id = "2",
                volumeInfo = VolumeInfo(
                    title = context.getString(R.string.title_2),
                    authors = listOf(context.getString(R.string.author_2)),
                    imageLinks = ImageLinks(thumbnail = "https://images.unsplash.com/flagged/photo-1569231290377-072234d3ee57?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                )
            )
        )
    }
}