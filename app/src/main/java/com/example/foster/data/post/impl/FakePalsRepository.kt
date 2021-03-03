/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.foster.data.post.impl

import com.example.foster.data.Result
import com.example.foster.data.post.PetPalsRepository
import com.example.foster.model.PetPal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakePalsRepository @Inject constructor() : PetPalsRepository {

    private val adoptPetPals = MutableStateFlow<Set<String>>(setOf())

    private val mutex = Mutex()

    override suspend fun getPetPal(id: String): Result<PetPal> {
        return withContext(Dispatchers.IO) {
            val petPal = PET_PALS.find { it.id == id }
            if (petPal == null) {
                Result.Error(IllegalArgumentException("Pet pal not found."))
            } else {
                Result.Success(petPal)
            }
        }
    }

    override suspend fun getPetPals(): Result<List<PetPal>> {
        return withContext(Dispatchers.IO) {
            delay(800)
            if (shouldRandomlyFail()) {
                Result.Error(IllegalStateException())
            } else {
                Result.Success(PET_PALS)
            }
        }
    }

    override fun observeAdoption(): Flow<Set<String>> = adoptPetPals

    override suspend fun toggleAdopt(id: String) {
        // 加锁
        mutex.withLock {
            val set = adoptPetPals.value.toMutableSet()
            set.addOrRemove(id)
            adoptPetPals.value = set.toSet()
        }
    }

    private var requestCount = 0

    private fun shouldRandomlyFail(): Boolean = ++requestCount % 5 == 0
}

private fun <E> MutableSet<E>.addOrRemove(element: E) {
    if (!add(element)) {
        remove(element)
    }
}
