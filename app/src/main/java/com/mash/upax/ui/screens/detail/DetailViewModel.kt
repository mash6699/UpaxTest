package com.mash.upax.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mash.upax.data.responses.APIPokeDetailResponse
import com.mash.upax.domain.GtePokemonDetailUseCase
import com.mash.upax.model.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: GtePokemonDetailUseCase
): ViewModel() {

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    val name: String = checkNotNull(savedStateHandle[DetailDestination.pokemonName])

    init {
        fetchDetail()
    }

    fun fetchDetail() = viewModelScope.launch {
        detailUiState = when(val result = useCase.getDetail(name)) {
            is BaseResult.Success -> {
                DetailUiState.Success(result.data)
            }
            is BaseResult.Error -> {
                DetailUiState.Error(result.exception.message.orEmpty())
            }
        }
    }
}

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val data: APIPokeDetailResponse) : DetailUiState
    data class Error(val message: String) : DetailUiState
}