package com.mash.upax.ext


fun Int.convertToPokemonNumber() = String.format("%04d", this)

fun String.convertNameToInitials(): String {
    val words = this.split(" ")
    val initials = words.take(2).joinToString("") { it.first().uppercase() }
    return initials
}