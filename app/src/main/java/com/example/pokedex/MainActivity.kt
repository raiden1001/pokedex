package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokedex.data.local.PokemonDetails
import com.example.pokedex.pokemonList.PokemonHomeScreen
import com.example.pokedex.ui.theme.PokedexTheme
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = PokemonLists,
                ){
                    composable<PokemonLists> {
                        PokemonHomeScreen(navController)
                    }
                    composable<PokemonDetails> {
                        val args = it.toRoute<PokemonDetails>()
                        Text(text = "${args.pokemonName}")
                    }
                }
            }
        }
    }
}

@Serializable
object PokemonLists




