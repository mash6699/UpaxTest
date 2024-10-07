package com.mash.upax.data.responses

import com.squareup.moshi.Json

data class APIPokeResponse (
    val count: Long,
    val next: String,
    val previous: Any? = null,
    @Json(name = "results")
    val results: List<Result>
)

data class Result(
    val name: String,
    val url: String
)