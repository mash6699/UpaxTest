package com.mash.upax.data.repository

import com.mash.upax.R
import com.mash.upax.data.local.PokemonEntity
import com.mash.upax.data.local.PokemonListDao
import com.mash.upax.data.network.APIService
import com.mash.upax.data.responses.APIPokeDetailResponse
import com.mash.upax.data.responses.APIPokeResponse
import com.mash.upax.model.base.BaseResult
import com.mash.upax.model.ex.ApiException
import com.mash.upax.util.NetworkStatus
import com.mash.upax.util.StringResource
import com.mash.upax.util.Utils
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val networkStatus: NetworkStatus,
    resources: StringResource,
    private val pokemonListDao: PokemonListDao
) : PokemonRepository {

    private val connectionError = resources.getString(R.string.network_error)

    override suspend fun getPokemonList(limit: Int, offset: Int): BaseResult<APIPokeResponse> {

        /* val cachedList = pokemonListDao.getPokemonList(25, offset)

         return if (cachedList.isNotEmpty()) {
             cachedList.map { it.toPockemon() }
         } else { }*/
        return if (networkStatus.isNetworkAvailable()) {
            try {
                val response = apiService.getPokemonList(limit, offset)

                val pokemonList = response.results.mapIndexed { _, entry ->
                    val number = Utils.getPokemonNumber(entry.url)
                    PokemonEntity(number.toInt(), entry.name)
                }

                pokemonListDao.insertPokemonList(pokemonList)

                BaseResult.Success(response)
            } catch (e: ApiException) {
                BaseResult.Error(e)
            }
        } else {
            BaseResult.Error(ApiException(0, connectionError))
        }


    }

    override suspend fun getPokemonDetail(name: String): BaseResult<APIPokeDetailResponse> {
        return if (networkStatus.isNetworkAvailable()) {
            try {
                val response = apiService.getPokemonDetail(name)
                BaseResult.Success(response)
            } catch (e: ApiException) {
                BaseResult.Error(e)
            }
        } else {
            BaseResult.Error(ApiException(0, connectionError))
        }
    }


}