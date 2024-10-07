package com.mash.upax.di

import com.mash.upax.data.local.PokemonListDao
import com.mash.upax.data.repository.PokemonRepository
import com.mash.upax.data.repository.PokemonRepositoryImpl
import com.mash.upax.data.network.APIService
import com.mash.upax.util.NetworkStatus
import com.mash.upax.util.StringResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        apiService: APIService,
        networkStatus: NetworkStatus,
        resource: StringResource,
        pokemonListDao: PokemonListDao
    ): PokemonRepository = PokemonRepositoryImpl(
        apiService,
        networkStatus,
        resource,
        pokemonListDao
    )
}