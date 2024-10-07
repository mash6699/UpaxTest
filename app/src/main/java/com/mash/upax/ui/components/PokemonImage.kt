package com.mash.upax.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mash.upax.R
import com.mash.upax.ext.convertNameToInitials
import com.mash.upax.ui.theme.UpaxTheme
import com.mash.upax.ui.theme.fontMulish

@Composable
fun PokemonCircleImage(
    pokemonName: String,
    pokemonImageUrl: String,
    borderColor: Color = Color.Gray,
    borderSize: Dp = 2.dp,
    backgroundColor: Color = Color.Blue,
    placeholderId: Int = R.drawable.ic_pokeball,) {
    if (pokemonImageUrl.isNotBlank()) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(pokemonImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = pokemonName,
            placeholder = painterResource(id = placeholderId),
            error = painterResource(id = R.drawable.ic_not_found),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.card_circle_list))
                .clip(CircleShape)
                .border(borderSize, color = borderColor, CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        PokemonCircleInitials(pokemonName,borderColor, borderSize, backgroundColor)
    }
}

@Composable
fun PokemonCircleInitials(
    name: String,
    borderColor: Color,
    borderSize: Dp,
    backgroundColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.card_circle_list))
            .clip(CircleShape)
            .border(borderSize, color = borderColor)
            .background(backgroundColor)
    ) {
        Text(
            text = name.convertNameToInitials(),
            fontFamily = fontMulish,
            fontWeight = FontWeight.W900,
            fontSize = 46.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonCircleImagePreview() {
    UpaxTheme {
        Column {
            PokemonCircleImage(pokemonName = "Pichu", pokemonImageUrl = "")
            PokemonCircleImage(pokemonName = "Muchu Richu", pokemonImageUrl = "")
            PokemonCircleImage(pokemonName = "Bulbasaur", pokemonImageUrl = "https://example.com/bulbasaur.png")
        }
    }
}