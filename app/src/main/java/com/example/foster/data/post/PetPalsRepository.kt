package com.example.foster.data.post

import com.example.foster.data.Result
import com.example.foster.model.PetPal

interface PetPalsRepository {

    suspend fun getPetPal(id: String): Result<PetPal>

    suspend fun getPetPals(): Result<List<PetPal>>

    suspend fun toggleAdopt(id: String)
}