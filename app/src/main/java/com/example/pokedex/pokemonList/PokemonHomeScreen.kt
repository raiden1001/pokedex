package com.example.pokedex.pokemonList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.pokedex.R
import com.example.pokedex.data.local.PokemonDetails
import com.example.pokedex.data.models.PokedexList
import com.example.pokedex.ui.theme.RobotoCondensed

@Composable
fun PokemonHomeScreen(
    navController: NavController,

    ) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            PokeSearchBar(
                hint = "Search",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            Spacer(modifier = Modifier.height(20.dp))

            PokemonList(navController)
        }

    }
}

@Composable
fun PokeSearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = Modifier) {
        BasicTextField(
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused
                },
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                modifier = Modifier.padding(10.dp),
                style = TextStyle(color = Color.Blue)
            )
        }

    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount =
            if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            (pokemonList.size / 2) + 1
        }
        items(itemCount) {
            if (!endReached && it >= itemCount - 1) {
                viewModel.loadPokemonPaginated()
            }
            PokedexRow(entries = pokemonList, navController = navController, rowIndex = it)
        }
    }
}

@Composable
fun PokedexListEntry(
    entry: PokedexList,
    navController: NavController,
    modifier: Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        //contentAlignment = Center,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(Brush.verticalGradient(listOf(dominantColor, defaultDominantColor)))
            .clickable {
                navController.navigate(
                    PokemonDetails(dominantColor.toArgb(), entry.name)
                )
            }
    ) {
        Column(Modifier.align(Center)) {
            var isImageLoading by remember { mutableStateOf(true) }
         /*   Box(
                modifier = modifier
                    .size(100.dp)
                    .background(dominantColor) // Apply the dominant color as a background
                    .align(CenterHorizontally),
                contentAlignment = Center
            ) {*/

                if (isImageLoading) {

                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                }

                Image(
                    painter = rememberImagePainter(
                        data = entry.imgUrl,
                        builder = {
                            listener(
                                onSuccess = { _, result ->
                                    isImageLoading = false
                                    viewModel.calcDominantColor(result.drawable) { color ->
                                        dominantColor = color
                                    }
                                },
                                onError = { _, _ -> isImageLoading = false }
                            )
                        }
                    ),
                    contentDescription = entry.name,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = entry.name,
                fontFamily = RobotoCondensed,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp
            )
        }
    }
//}

@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexList>,
    navController: NavController
) {
    Column {
        Row {
            PokedexListEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2) {
                PokedexListEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}