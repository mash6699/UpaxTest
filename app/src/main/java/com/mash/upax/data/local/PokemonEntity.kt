package com.mash.upax.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_list")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String
)
