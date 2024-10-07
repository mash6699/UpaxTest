package com.mash.upax.domain

import com.mash.upax.data.repository.PokemonRepository
import com.mash.upax.data.responses.APIPokeDetailResponse
import com.mash.upax.model.base.BaseResult
import javax.inject.Inject

class GtePokemonDetailUseCase @Inject constructor(private val repository: PokemonRepository){

    suspend fun getDetail(name: String): BaseResult<APIPokeDetailResponse> {
        return  repository.getPokemonDetail(name)
    }

    /*suspend fun getDetail2(name: String): PokemonDetail {
        return when(val result = repository.getPokemonDetail(name) ) {
            is BaseResult.Success -> {
                result.data.toPokemonDetail()

            }
            is BaseResult.Error -> {
            
            }
        }
    }*/

}