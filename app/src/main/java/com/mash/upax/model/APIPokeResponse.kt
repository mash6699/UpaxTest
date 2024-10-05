package com.mash.upax.model

import com.squareup.moshi.Json

data class APIPokeResponse (
    val count: Long,
    val next: String,
    val previous: Any? = null,
    @Json(name = "results")
    val results: List<PokeResult>
)

data class PokeResult(
    val name: String,
    val url: String
)