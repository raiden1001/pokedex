package com.example.pokedex.pokemonDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pokedex.data.remote.responses.Pokemon
import com.example.pokedex.util.Resource

@Composable
fun PokemonDetailScreen(
    navController: NavController,
    dominantColorValue: Int,
    pokemonName: String,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val dominantColor = Color(dominantColorValue)

    val pokemonInfo = produceState<Resource<Pokemon>>(Resource.Loading()) {
        value = viewModel.getPokemonDetails(pokemonName)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        TopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.25f)
                .align(Alignment.TopCenter),
            dominantColor = dominantColor,
        )
        StateWrapper(
            pokemonInfo = pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 20.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 20.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter),
            dominantColor = dominantColor
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize(),
        ) {
            if (pokemonInfo is Resource.Success) {
                pokemonInfo.data?.sprites.let {
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it?.front_default)
                            .crossfade(true)
                            .build()
                    )

                    Image(
                        painter = painter,
                        contentDescription = pokemonInfo.data?.name,
                        modifier = Modifier
                            .size(pokemonImageSize)
                            .offset(y = topPadding)
                            .background(Color.Transparent)
                    )
                }
            }

            if (pokemonInfo is Resource.Error) {
                Text(
                    text = "error occured", modifier = Modifier
                        .size(pokemonImageSize)
                        .offset(y = topPadding)
                        .background(Color.White)
                )
            }

            if (pokemonInfo is Resource.Loading) {
                Text(text = "loading", modifier = Modifier
                    .size(pokemonImageSize)
                    .offset(y = topPadding)
                    .background(Color.White))
            }
        }
    }
}

@Composable
fun TopSection(
    navController: NavController,
    modifier: Modifier,
    dominantColor: Color
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.background(
            Brush.verticalGradient(listOf(MaterialTheme.colorScheme.background, dominantColor))
        )
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun StateWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier,
    loadingModifier: Modifier = Modifier,
    dominantColor: Color
) {
    when (pokemonInfo) {
        is Resource.Success -> {}
        is Resource.Error -> Text(text = "error occurred", color = Color.Red, modifier = modifier)
        is Resource.Loading -> CircularProgressIndicator(color = dominantColor,modifier = Modifier.fillMaxSize().background(Color.Transparent))

    }

}

