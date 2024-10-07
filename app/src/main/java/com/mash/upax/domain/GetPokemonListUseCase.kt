package com.mash.upax.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mash.upax.data.repository.PokemonRepository
import com.mash.upax.model.Pokemon
import com.mash.upax.model.base.BaseResult
import com.mash.upax.util.Constants.PAGE_SIZE
import com.mash.upax.util.Utils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(private val repository: PokemonRepository){

    fun getPokemon(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { getPagination() }).flow
    }

    private fun getPagination(): PagingSource<Int, Pokemon> {
        return object : PagingSource<Int, Pokemon>() {
            override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? =
                state.anchorPosition

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
                return try {
                    val page = params.key ?: 1
                    val offset = ((page-1) * PAGE_SIZE + 1)-1

                    return when(val result = repository.getPokemonList(PAGE_SIZE, offset)) {
                        is BaseResult.Success -> {
                            val pokemonResponse = result.data.results.mapIndexed { _, entry ->
                                val number = Utils.getPokemonNumber(entry.url)
                                val imageUrl = Utils.formatPokemonUrl(number)
                                Pokemon( entry.name,number.toInt(), imageUrl)
                            }
                            LoadResult.Page(
                                data = pokemonResponse,
                                prevKey = if(page == 1) null else page.minus(1),
                                nextKey = if(result.data.results.isEmpty()) null else page.plus(1)
                            )
                        }
                        is BaseResult.Error -> {
                            return LoadResult.Error(Throwable(result.exception.message))
                        }
                    }
                } catch (exception: Exception) {
                    return LoadResult.Error(exception)
                }
            }
        }
    }

}