package com.example.foster.model

data class PetPal(
    val id: String,
    val name: String,
    val imageId: Int,
    val breeds: String,
    val metadata: Metadata
) {
}

data class Metadata(
    val gender: String,
    val age: Int,
    val introduce: String
)