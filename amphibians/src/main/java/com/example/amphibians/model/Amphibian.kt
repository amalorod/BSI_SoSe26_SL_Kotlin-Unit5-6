package com.example.amphibians.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable






// Modell der Amphibians
@Serializable
data class Amphibian(
    val name: String,
    val type: String,
    val description: String,
    @SerialName("img_src") val imgSrc: String
)
