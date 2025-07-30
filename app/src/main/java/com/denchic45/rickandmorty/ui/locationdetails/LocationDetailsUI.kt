package com.denchic45.rickandmorty.ui.locationdetails

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.denchic45.rickandmorty.domain.model.LocationDetails
import com.denchic45.rickandmorty.ui.BottomSheetLoading
import com.denchic45.rickandmorty.ui.HeaderItem
import com.denchic45.rickandmorty.ui.ResourceContent
import com.denchic45.rickandmorty.ui.ResultFailedBox
import com.denchic45.rickandmorty.ui.characters.CharactersGrid
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(
    viewModel: LocationDetailsViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateToCharacterDetails: (Int) -> Unit
) {
    val resource by viewModel.locationState.collectAsState()
    ModalBottomSheet(onDismissRequest = navigateBack) {
        ResourceContent(
            resource,
            onLoading = { BottomSheetLoading() },
            onFailed = { failure, value ->
                value?.let {
                    LocationDetailsContent(
                        state = value,
                        navigateToCharacterDetails = navigateToCharacterDetails,
                        onFavoriteClick = viewModel::onFavoriteClick
                    )
                } ?: ResultFailedBox(failure = failure)
            }) { state ->
            LocationDetailsContent(state, navigateToCharacterDetails, viewModel::onFavoriteClick)
        }
    }
}

@Composable
private fun LocationDetailsContent(
    state: LocationDetails,
    navigateToCharacterDetails: (Int) -> Unit,
    onFavoriteClick: (Int, Boolean) -> Unit
) {
    Text(
        state.location.name,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        overlineContent = { Text("Type") },
        headlineContent = { Text(state.location.type) }
    )
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        overlineContent = { Text("Dimension") },
        headlineContent = { Text(state.location.dimension) }
    )
    if (state.residents.isNotEmpty()) {
        HeaderItem("Residents")
        HorizontalDivider(Modifier.padding(top = 16.dp))
        CharactersGrid(
            items = state.residents,
            navigateToCharacterDetails = navigateToCharacterDetails,
            onFavoriteClick = onFavoriteClick
        )
    }
}