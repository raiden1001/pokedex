package com.example.pokedex.pokemonDetail

import androidx.lifecycle.ViewModel
import com.example.pokedex.data.remote.responses.Pokemon
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val pokemonRepository : PokemonRepository
) :ViewModel() {
    suspend fun getPokemonDetails(name: String) : Resource<Pokemon>{
        return pokemonRepository.getPokemonInfo(name)

    }
}