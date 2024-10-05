package com.mash.upax.network

import com.mash.upax.model.APIPokeDetailResponse
import com.mash.upax.model.APIPokeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("api/v2/pokemon") //limit=1&offset=0
    suspend fun getPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int): APIPokeResponse

    @GET("api/v2/pokemon/")
    suspend fun getDetail(@Query("name") name: String): APIPokeDetailResponse
}