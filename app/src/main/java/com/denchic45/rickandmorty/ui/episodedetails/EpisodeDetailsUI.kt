package com.denchic45.rickandmorty.ui.episodedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.denchic45.rickandmorty.domain.model.EpisodeDetails
import com.denchic45.rickandmorty.ui.BottomSheetLoading
import com.denchic45.rickandmorty.ui.HeaderItem
import com.denchic45.rickandmorty.ui.ResourceContent
import com.denchic45.rickandmorty.ui.ResultFailedBox
import com.denchic45.rickandmorty.ui.characters.CharactersGrid
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeBottomSheet(
    viewModel: EpisodeDetailsViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateToCharacterDetails: (Int) -> Unit
) {
    val resource by viewModel.episodeState.collectAsState()
    ModalBottomSheet(onDismissRequest = navigateBack) {
        ResourceContent(
            resource,
            onLoading = { BottomSheetLoading() },
            onFailed = { failure, value ->
                value?.let {
                    EpisodeDetailsContent(
                        state = value,
                        navigateToCharacterDetails = navigateToCharacterDetails,
                        onFavoriteClick = viewModel::onFavoriteClick
                    )
                } ?: ResultFailedBox(failure = failure)
            }) { state ->
            EpisodeDetailsContent(
                state = state,
                navigateToCharacterDetails = navigateToCharacterDetails,
                onFavoriteClick = viewModel::onFavoriteClick
            )
        }
    }
}

@Composable
fun EpisodeDetailsContent(
    state: EpisodeDetails,
    navigateToCharacterDetails: (Int) -> Unit,
    onFavoriteClick: (Int, Boolean) -> Unit
) {
    Row(Modifier.padding(horizontal = 16.dp)) {
        Text(
            state.episode.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
        ) {
            Text(
                text = state.episode.episode,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp, 4.dp)
            )
        }
    }
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        overlineContent = { Text("Air date") },
        headlineContent = { Text(state.episode.airDateText) }
    )
    HeaderItem("Characters")
    HorizontalDivider(Modifier.padding(top = 16.dp))
    CharactersGrid(
        items = state.characters,
        navigateToCharacterDetails = navigateToCharacterDetails,
        onFavoriteClick = onFavoriteClick
    )
}