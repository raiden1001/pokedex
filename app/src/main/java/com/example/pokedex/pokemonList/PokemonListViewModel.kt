package com.example.pokedex.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import com.example.pokedex.data.models.PokedexList
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Constants.PAGE_SIZE
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.Locale

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    private var currentPage = 0
    var pokemonList = mutableStateOf<List<PokedexList>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var isSearching = mutableStateOf(false)


    private var cachedPokemonList = listOf<PokedexList>()
    private var isSearchStart: Boolean = true


    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if(isSearchStart) {
            pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {

            if(query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStart = true
                return@launch
            }
            val results = listToSearch.filter {
                it.name.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query
            }
            if(isSearchStart) {
                cachedPokemonList = pokemonList.value
                isSearchStart = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinished: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { paletecolor ->
            paletecolor?.dominantSwatch?.rgb?.let { color ->
                onFinished(Color(color))
            }
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(1302, 0)
            when (result) {
                is Resource.Success -> {
                    endReached.value = PAGE_SIZE * currentPage >= result.data!!.count
                    val pokedexEntry: List<PokedexList> =
                        result.data.results.mapIndexed { index, entry ->
                            val number = if (entry.url.endsWith("/")) {
                                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                entry.url.takeLastWhile { it.isDigit() }
                            }
                            val imgUrl =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                            PokedexList(
                                entry.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                },
                                imgUrl = imgUrl,
                                number.toInt()
                            )

                        }
                    currentPage++
                    isLoading.value = false
                    loadError.value = ""
                    pokemonList.value += pokedexEntry
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

                is Resource.Loading -> TODO()
            }
        }
    }
}