package com.example.pokedex.data.remote.responses

data class PokemonList (
    val count: Long,
    val next: String,
    val previous: Any? = null,
    val results: List<Result>
)

data class Result (
    val name: String,
    val url: String
)
