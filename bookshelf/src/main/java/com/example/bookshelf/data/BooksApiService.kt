package com.example.bookshelf.data

import com.google.gson.annotations.SerializedName // WICHTIG: Gson-Annotation!
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// @Serializable wurde komplett entfernt!
data class BookSearchResponse(
    @SerializedName("items") val items: List<BookItem>? = null
)

data class BookItem(
    @SerializedName("id") val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo? = null
)

data class VolumeInfo(
    @SerializedName("title") val title: String? = null,
    @SerializedName("authors") val authors: List<String>? = null, // Google liefert ein JSON-Array []
    @SerializedName("description") val description: String? = null,
    @SerializedName("publishedDate") val publishedDate: String? = null,
    @SerializedName("imageLinks") val imageLinks: ImageLinks? = null
)

data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String? = null
)

interface BooksApiService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BookSearchResponse

    @GET("volumes/{id}")
    suspend fun getBookDetails(@Path("id") id: String): BookItem
}
