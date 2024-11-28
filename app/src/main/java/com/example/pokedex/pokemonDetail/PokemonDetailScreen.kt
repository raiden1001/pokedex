package com.example.pokedex.pokemonDetail

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.data.remote.responses.Pokemon
import com.example.pokedex.data.remote.responses.Stat
import com.example.pokedex.data.remote.responses.Type
import com.example.pokedex.util.Resource
import com.example.pokedex.util.parseStatToAbbr
import com.example.pokedex.util.parseStatToColor
import com.example.pokedex.util.parseTypeToColor
import java.util.Locale
import kotlin.math.round

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
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 10.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 10.dp
                )
                .background(Color.Transparent)
                .align(Alignment.Center),
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
        }
    }
}

@Composable
fun TopSection(
    navController: NavController,
    modifier: Modifier = Modifier,
    dominantColor: Color
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.background(
             Color.Transparent
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
        is Resource.Success -> {
            PokemonDetailSection(
                modifier = modifier.offset(y = (-10).dp),
                pokemonInfo = pokemonInfo.data!!
            )
        }

        is Resource.Error -> Text(text = "error occurred", color = Color.Red, modifier = modifier)
        is Resource.Loading -> {
            val infiniteTransition = rememberInfiniteTransition(label = "")

            val scale by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Loading",
                modifier = loadingModifier.offset(y = (-10).dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale) // Apply scaling
            )
        }
    }
}

@Composable
fun PokemonDetailSection(
    modifier: Modifier = Modifier,
    pokemonInfo: Pokemon,

    ) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .offset(y = (80).dp)
            .verticalScroll(scrollState)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "# ${pokemonInfo.id} ${pokemonInfo.name.uppercase()}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        TypeSection(types = pokemonInfo.types)
        PokemonDetailDataSection(
            pokemonHeight = pokemonInfo.height.toFloat(),
            pokemonWeight = pokemonInfo.weight.toFloat(),
            sectionHeight = 60.dp
        )
        PokemonDetailStats(pokemonInfo.stats)
    }
}

@Composable
fun TypeSection(types: List<Type>) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        types.forEach {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(
                        parseTypeToColor(it)
                    )
                    .height(30.dp)
            ) {
                Text(text = it.type.name.capitalize(Locale.ROOT))
            }
        }
    }
}


@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Float,
    pokemonHeight: Float,
    sectionHeight: Dp
) {
    val pokemonWeightInKg = remember {
        round(pokemonWeight * 100f) / 1000f
    }
    val pokemonHeightInMtr = remember {
        round(pokemonHeight * 100f) / 1000f
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        PokemonDetailDataItem(pokemonWeightInKg, "Kg", painterResource(R.drawable.ic_weight))
        Spacer(
            modifier = Modifier
                .size(7.dp, sectionHeight)
                .padding(start = 5.dp, end = 5.dp)
                .background(Color.Black)
        )
        PokemonDetailDataItem(pokemonHeightInMtr, "mts", painterResource(R.drawable.ic_height))
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue $dataUnit",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PokemonDetailStats(
    stats: List<Stat>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 10.dp)
    ) {
        Text(
            text = "Base Stat :",
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle.Default
        )
        stats.forEach { stat ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val animatedWidth by animateDpAsState(
                    targetValue = (stat.base_stat.toInt() * 2).dp, // Scale the baseStat to a meaningful width
                    animationSpec = tween(durationMillis = 500) // Animation duration
                )
                Box(
                    modifier = Modifier
                        .padding(7.dp)
                        .width(animatedWidth)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            parseStatToColor(stat.stat)
                        )
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween ,
                        modifier = Modifier.fillMaxWidth() ,
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(
                            text = parseStatToAbbr(stat.stat),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Text(
                            text = stat.base_stat.toString(),
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                }


            }
        }
    }

}

