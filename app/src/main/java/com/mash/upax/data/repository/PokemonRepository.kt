package com.mash.upax.data.repository

import com.mash.upax.data.responses.APIPokeDetailResponse
import com.mash.upax.data.responses.APIPokeResponse
import com.mash.upax.model.base.BaseResult

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): BaseResult<APIPokeResponse>
    suspend fun getPokemonDetail(name: String): BaseResult<APIPokeDetailResponse>
}