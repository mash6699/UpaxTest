package com.mash.upax.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.mash.upax.ui.theme.UpaxTheme

@Composable
fun CircularIndicator(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics { testTag = "circularLoader" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = color)
    }

}

@Preview(apiLevel = 33)
@Composable
fun CircularIndicatorPreview() {
    UpaxTheme {
        CircularIndicator()
    }
}