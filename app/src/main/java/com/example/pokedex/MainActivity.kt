package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokedex.data.local.PokemonDetails
import com.example.pokedex.pokemonDetail.PokemonDetailScreen
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
                ) {
                    composable<PokemonLists> {
                        PokemonHomeScreen(navController)
                    }
                    composable<PokemonDetails> {

                        val args = it.toRoute<PokemonDetails>()
                        PokemonDetailScreen(
                            navController = navController,
                            dominantColorValue = args.dominantColor,
                            pokemonName = args.pokemonName.lowercase(),
                            topPadding = 10.dp,
                            pokemonImageSize = 200.dp
                        )
                    }
                }
            }
        }
    }
}

@Serializable
object PokemonLists




