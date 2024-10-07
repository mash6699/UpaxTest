package com.mash.upax.util

object Utils {

    fun getPokemonNumber(pokemonUrl: String): String =
        if (pokemonUrl.endsWith("/")) {
            pokemonUrl.dropLast(1).takeLastWhile { it.isDigit() }
        } else {
            pokemonUrl.takeLastWhile { it.isDigit() }
        }

    fun formatPokemonUrl(number: String): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png"
    }

}