package com.example.foster.data.post.impl

import android.content.Context
import com.example.foster.data.Result
import com.example.foster.data.post.PetPalsRepository
import com.example.foster.model.PetPal
import kotlinx.coroutines.flow.Flow

class BlockingFakePalsRepository(
    private val context: Context
) : PetPalsRepository {
    override suspend fun getPetPal(id: String): Result<PetPal> {
        TODO("Not yet implemented")
    }

    override suspend fun getPetPals(): Result<List<PetPal>> {
        return Result.Success(PET_PALS)
    }

    override fun observeAdoption(): Flow<Set<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleAdopt(id: String) {
        TODO("Not yet implemented")
    }
}