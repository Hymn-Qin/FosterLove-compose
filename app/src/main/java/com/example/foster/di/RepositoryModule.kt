package com.example.foster.di

import com.example.foster.data.post.PetPalsRepository
import com.example.foster.data.post.impl.FakePalsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providePetPalsRepository(): PetPalsRepository {
        return FakePalsRepository()
    }
}