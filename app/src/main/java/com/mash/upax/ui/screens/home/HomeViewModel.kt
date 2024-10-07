package com.mash.upax.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mash.upax.domain.GetPokemonListUseCase
import com.mash.upax.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: GetPokemonListUseCase) : ViewModel() {

    private val _pokeState: MutableStateFlow<PagingData<Pokemon>> = MutableStateFlow(value = PagingData.empty())
    val pokeState: StateFlow<PagingData<Pokemon>> = _pokeState

    init {
        onEvent(HomeEvent.GetHome)
    }

    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.GetHome -> {
                    getPoke()
                }
            }
        }
    }

    private suspend fun getPoke() {
        /*useCase.execute(Unit)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _pokeState.value = it
            }*/

        useCase.getPokemon()
            .cachedIn(viewModelScope)
            .collect {
                _pokeState.value = it
            }
    }
}

sealed class HomeEvent {
    object GetHome: HomeEvent()
}