package com.example.pokedex.data.local

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetails(
    val dominantColor : Int,
    val pokemonName : String
)
