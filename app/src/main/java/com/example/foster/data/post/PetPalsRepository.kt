package com.example.foster.data.post

import com.example.foster.data.Result
import com.example.foster.model.PetPal
import kotlinx.coroutines.flow.Flow

interface PetPalsRepository {

    suspend fun getPetPal(id: String): Result<PetPal>

    suspend fun getPetPals(): Result<List<PetPal>>

    fun observeAdoption(): Flow<Set<String>>

    suspend fun toggleAdopt(id: String)
}