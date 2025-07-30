package com.denchic45.rickandmorty.ui.favorites

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.denchic45.rickandmorty.ui.EmptyContent
import com.denchic45.rickandmorty.ui.characters.CharactersGrid
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel(),
    navigateToCharacterDetails: (Int) -> Unit
) {
    val state by viewModel.favoritesState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Favorites") }, scrollBehavior = scrollBehavior)
        }) { innerPadding ->
        if (state.isEmpty()) {
            EmptyContent(
                painter = rememberVectorPainter(Icons.Default.Favorite),
                text = "No favorites"
            )
        } else CharactersGrid(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            items = state,
            navigateToCharacterDetails = navigateToCharacterDetails,
            onFavoriteClick = viewModel::onFavoriteClick
        )
    }
}