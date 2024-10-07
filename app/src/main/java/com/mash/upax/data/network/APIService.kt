package com.mash.upax.data.network

import com.mash.upax.data.responses.APIPokeDetailResponse
import com.mash.upax.data.responses.APIPokeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface APIService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int): APIPokeResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): APIPokeDetailResponse
}