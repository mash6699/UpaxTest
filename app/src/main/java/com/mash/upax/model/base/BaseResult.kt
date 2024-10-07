package com.mash.upax.model.base

import com.mash.upax.model.ex.ApiException

sealed interface BaseResult<out T> {
    data class Success<out T>(val data: T) : BaseResult<T>
    data class Error(val exception: ApiException): BaseResult<Nothing>
}