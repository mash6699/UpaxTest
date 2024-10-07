package com.mash.upax.ui.screens.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mash.upax.R
import com.mash.upax.data.responses.APIPokeDetailResponse
import com.mash.upax.data.responses.Type
import com.mash.upax.ext.convertToPokemonNumber
import com.mash.upax.ui.components.CircularIndicator
import com.mash.upax.ui.components.ErrorMessage
import com.mash.upax.ui.components.PokeTopAppBar
import com.mash.upax.ui.navigation.NavDestination
import com.mash.upax.ui.theme.blueLight


object DetailDestination : NavDestination {
    override val route = "detail"
    override val titleRes = R.string.detail
    const val pokemonName = "pokemonName"
    val routeWithArgs = "$route/{$pokemonName}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val detailUiState = viewModel.detailUiState
    Scaffold(
        topBar = {
            PokeTopAppBar(
                modifier = modifier,
                title = stringResource(id = DetailDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        when (detailUiState) {
            is DetailUiState.Loading -> CircularIndicator()
            is DetailUiState.Success -> {
                PokemonDetail(
                    detail = detailUiState.data,
                    modifier = Modifier.padding(
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        top = innerPadding.calculateTopPadding(),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
                    )
                )
            }

            is DetailUiState.Error -> ErrorMessage(
                message = detailUiState.message,
                onClickRetry = viewModel::fetchDetail
            )
        }
    }
}

@Composable
fun PokemonDetail(modifier: Modifier, detail: APIPokeDetailResponse) {
    var isFavorite by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        IconButton(
            onClick = { isFavorite = !isFavorite },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }

        PokemonHeader(detail.id.toInt(), detail.name.uppercase())

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(detail.sprites.other?.officialArtwork?.frontDefault)
                .crossfade(true)
                .build(),
            contentDescription = detail.name,
            placeholder = painterResource(R.drawable.ic_pokeball),
            error = painterResource(R.drawable.ic_not_found),
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        PokemonAttributes(detail.weight, detail.height)
        PokemonTypes(types = detail.types)
    }
}

@Composable
fun PokemonAttributes(weight: Int, height: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.weight),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .border(1.dp, color = blueLight, RectangleShape)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "$weight",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.height),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .border(1.dp, color = blueLight, RectangleShape)
                    .padding(4.dp)
            )
            Text(
                text = "$height",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PokemonHeader(id: Int, name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
    ) {
        Text(
            text = stringResource(
                id = R.string.number,
                id.convertToPokemonNumber()
            ),
            style = MaterialTheme.typography.displayMedium
        )
        Text(name.uppercase(), style = MaterialTheme.typography.displayMedium)
    }
}

@Composable
fun PokemonTypes(types: List<Type>) {
    val viewOuterPadding = 16.dp
    val viewInnerPadding = 8.dp
    val viewFontSize = 18.sp
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.types),
            style = MaterialTheme.typography.displaySmall,
        )
        HorizontalDivider(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_xsmall)),
            thickness = dimensionResource(id = R.dimen.thickness_divider)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(viewOuterPadding)
        ) {
            for (type in types) {
                Card(modifier = Modifier.padding(viewInnerPadding)) {
                    Text(
                        text = type.type.name.uppercase(),
                        fontSize = viewFontSize,
                        modifier = Modifier.padding(viewInnerPadding)
                    )
                }
            }
        }
    }
}