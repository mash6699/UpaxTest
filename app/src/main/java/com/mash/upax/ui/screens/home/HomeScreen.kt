package com.mash.upax.ui.screens.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mash.upax.R
import com.mash.upax.ext.convertToPokemonNumber
import com.mash.upax.model.Pokemon
import com.mash.upax.ui.components.CircularIndicator
import com.mash.upax.ui.components.ErrorMessage
import com.mash.upax.ui.components.PokeTopAppBar
import com.mash.upax.ui.components.PokemonCircleImage
import com.mash.upax.ui.navigation.NavDestination
import com.mash.upax.ui.theme.UpaxTheme
import com.mash.upax.ui.theme.blueLight
import com.mash.upax.ui.theme.fontMulish
import kotlinx.coroutines.flow.flowOf

object HomeDestination : NavDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToItemDetail: (String) -> Unit
) {
    val items: LazyPagingItems<Pokemon> = viewModel.pokeState.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier,
        topBar = {
            PokeTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        PokemonGrid(
            modifier = Modifier.fillMaxSize(),
            pokemonList = items,
            contentPadding = innerPadding,
            onItemClick = navigateToItemDetail
        )
    }

}

@Composable
fun PokemonGrid(
    modifier: Modifier = Modifier,
    pokemonList: LazyPagingItems<Pokemon>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(pokemonList.itemCount) { index: Int ->
            pokemonList[index]?.let {
                PokemonCard(
                    pokemon = it,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .clickable { onItemClick(it.name) }
                )
            }
        }

        pokemonList.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        CircularIndicator()
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = pokemonList.loadState.refresh as LoadState.Error
                    item {
                        ErrorMessage(
                            modifier = modifier.fillMaxSize(),
                            message = e.error.localizedMessage ?: "",
                            onClickRetry = { retry() })
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                loadState.append is LoadState.Error -> {
                    val e = pokemonList.loadState.append as LoadState.Error
                    Log.i("ERROR A", e.toString())
                    item {
                        ErrorMessage(
                            modifier = Modifier.fillMaxSize(),
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }
            }
        }
    }
}


@Composable
fun PokemonCard(pokemon: Pokemon, modifier: Modifier = Modifier) {

    var isFavorite by remember { mutableStateOf(false) }

    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,),
        border = BorderStroke(2.dp, blueLight),
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }

            PokemonCardImage(pokemon.name, pokemon.number, pokemon.imageUrl)

        }
    }
}

@Composable
fun PokemonCardImage(
    pokemonName: String,
    pokemonNumber: Int,
    pokemonImageUrl: String,
    borderColor: Color = Color.Gray,
    borderSize: Dp = 2.dp,
    backgroundColor: Color = Color.Blue,
    placeholderId: Int = R.drawable.ic_pokeball,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.number, pokemonNumber.convertToPokemonNumber()),
            fontFamily = fontMulish,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        PokemonCircleImage(
            pokemonName,
            pokemonImageUrl,
            borderColor,
            borderSize,
            backgroundColor,
            placeholderId
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = pokemonName.uppercase(),)
    }
}




@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    val mockData = listOf(
        Pokemon(name = "Bulbasaur", imageUrl = "https://example.com/bulbasaur.png", number = 1),
        Pokemon(name = "Ivysaur", imageUrl = "", number = 2),
        Pokemon(name = "Venusaur", imageUrl = "https://example.com/venusaur.png", number = 3),
    )
    val mockPagingItem = flowOf(PagingData.from(mockData)).collectAsLazyPagingItems()
    UpaxTheme {
        PokemonGrid(pokemonList = mockPagingItem, onItemClick = {})
    }
}
