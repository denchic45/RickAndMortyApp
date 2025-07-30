package com.denchic45.rickandmorty.ui.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.domain.model.CharacterItem
import com.denchic45.rickandmorty.domain.model.asFailure
import com.denchic45.rickandmorty.ui.CircularLoadingBox
import com.denchic45.rickandmorty.ui.EmptyContent
import com.denchic45.rickandmorty.ui.ResultFailedBox
import com.denchic45.rickandmorty.ui.ResultFailedItem
import com.denchic45.rickandmorty.ui.theme.RickAndMortyAppTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter


@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel,
    navigateToCharacterDetails: (Int) -> Unit,
    navigateToFilters: () -> Unit
) {
    val searchState = viewModel.searchState
    val charactersPaging = viewModel.charactersState.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state = rememberPullToRefreshState()
    LaunchedEffect(Unit) {
        snapshotFlow { searchState.name }
            .drop(1)
            .debounce(300)
            .collect {
                viewModel.onSearchRequest()
            }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Column {
                        SearchBar(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.statusBars)
                                .height(56.dp),
                            inputField = {
                                ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                                    SearchBarDefaults.InputField(
                                        query = searchState.name,
                                        onQueryChange = { searchState.name = it },
                                        onSearch = { viewModel.onSearchRequest() },
                                        expanded = false,
                                        onExpandedChange = {},
                                        placeholder = { Text("Search characters") },
                                        leadingIcon = { Icon(Icons.Default.Search, null) },
                                        trailingIcon = {
                                            Row {
                                                if (searchState.name.isNotEmpty())
                                                    IconButton(
                                                        onClick = viewModel::onClearSearchClick
                                                    ) {
                                                        Icon(Icons.Default.Close, null)
                                                    }
                                                if (searchState.hasFilters)
                                                    FilledIconButton(
                                                        onClick = {
                                                            viewModel.onFiltersClick()
                                                            navigateToFilters()
                                                        }
                                                    ) {
                                                        Icon(Icons.Outlined.FilterAlt, null)
                                                    }
                                                else FilledTonalIconButton(navigateToFilters) {
                                                    Icon(Icons.Outlined.FilterAlt, null)
                                                }
                                            }
                                        }
                                    )
                                }
                            },
                            expanded = false,
                            onExpandedChange = {},
                            content = {}
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                })
        }) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.padding(innerPadding),
            isRefreshing = isRefreshing && charactersPaging.loadState.refresh is LoadState.Loading,
            onRefresh = {
                charactersPaging.refresh()
                viewModel.onRefreshingChange(true)
            },
            state = state
        ) {
            Column {
                    val refresh by snapshotFlow { charactersPaging.loadState.refresh }
                        .filter { it !is LoadState.Loading }
                        .collectAsState(LoadState.Loading)

                    (refresh as? LoadState.Error)?.error?.asFailure()?.let { failure ->
                        if (charactersPaging.itemCount == 0) {
                            ResultFailedBox(
                                failure = failure,
                                onRetryClick = charactersPaging::retry
                            )
                        } else {
                            ResultFailedItem(
                                failure = failure,
                                onRetryClick = charactersPaging::retry
                            )
                        }
                    }

                charactersPaging.loadState.let { loadState ->
                    when {
                        loadState.refresh is LoadState.Loading
                                && charactersPaging.itemCount == 0 -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { CircularLoadingBox() }
                        }

                        loadState.refresh is LoadState.NotLoading
                                && charactersPaging.itemCount == 0
                                && loadState.append.endOfPaginationReached -> {
                            EmptyContent(
                                painter = rememberVectorPainter(Icons.Default.Search),
                                text = "Nothing found"
                            )
                        }
                    }
                }


                PagingCharactersGrid(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    items = charactersPaging,
                    navigateToCharacterDetails = navigateToCharacterDetails,
                    onFavoriteClick = viewModel::onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun CharactersGrid(
    modifier: Modifier = Modifier,
    items: List<CharacterItem>,
    navigateToCharacterDetails: (Int) -> Unit,
    onFavoriteClick: (Int, Boolean) -> Unit
) {
    CharactersGrid(modifier) {
        items(items, key = { it.id }) { character ->
            CharacterListItem(
                modifier = Modifier.animateItem(),
                item = character,
                onClick = { navigateToCharacterDetails(character.id) },
                onFavoriteClick = { onFavoriteClick(character.id, !character.favorite) })
        }
    }
}

@Composable
private fun CharactersGrid(modifier: Modifier = Modifier, content: LazyGridScope.() -> Unit) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
private fun PagingCharactersGrid(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<CharacterItem>,
    navigateToCharacterDetails: (Int) -> Unit,
    onFavoriteClick: (Int, Boolean) -> Unit
) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items.itemCount, key = items.itemKey { it.id }) { index ->
            items[index]?.let { item ->
                CharacterListItem(
                    Modifier.animateItem(),
                    CharacterItem(
                        item.id,
                        item.name,
                        status = item.status,
                        species = item.species,
                        type = item.type,
                        gender = item.gender,
                        image = item.image,
                        favorite = item.favorite
                    ),
                    onClick = { navigateToCharacterDetails(item.id) },
                    onFavoriteClick = { onFavoriteClick(item.id, !item.favorite) })
            }
        }
    }
}

@Composable
fun CharacterListItem(
    modifier: Modifier = Modifier,
    item: CharacterItem,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), onClick = onClick) {
        Box(Modifier.height(178.dp)) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = item.image,
                contentDescription = null,
            )
            IconButton(onFavoriteClick) {
                Icon(
                    Icons.Default.Favorite,
                    null,
                    tint = if (item.favorite) Color.Red else Color.White
                )
            }
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(topStart = 8.dp))
                    .background(
                        when (item.status) {
                            CharacterStatus.ALIVE -> MaterialTheme.colorScheme.tertiaryContainer
                            CharacterStatus.DEAD -> MaterialTheme.colorScheme.errorContainer
                            CharacterStatus.UNKNOWN -> MaterialTheme.colorScheme.inverseSurface
                        }
                    )
                    .padding(4.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    item.status.displayName, color = when (item.status) {
                        CharacterStatus.ALIVE -> MaterialTheme.colorScheme.onTertiaryContainer
                        CharacterStatus.DEAD -> MaterialTheme.colorScheme.onErrorContainer
                        CharacterStatus.UNKNOWN -> MaterialTheme.colorScheme.inverseOnSurface
                    }
                )
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp)
        ) {
            Text(
                item.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "${item.gender.displayName} | ${item.species}",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

val CharacterGender.displayName: String
    get() = when (this) {
        CharacterGender.FEMALE -> "Female"
        CharacterGender.MALE -> "Male"
        CharacterGender.GENDERLESS -> "Genderless"
        CharacterGender.UNKNOWN -> "Unknown"
    }

val CharacterStatus.displayName: String
    get() = when (this) {
        CharacterStatus.ALIVE -> "Alive"
        CharacterStatus.DEAD -> "Dead"
        CharacterStatus.UNKNOWN -> "Unknown"
    }

@Preview
@Composable
private fun CharacterGridPreview() {
    RickAndMortyAppTheme {
        LazyVerticalGrid(
            GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(2) }) { Spacer(Modifier.height(16.dp)) }
            repeat(4) {
                item {
                    CharacterListItem(
                        Modifier, CharacterItem(
                            0,
                            "Morty Smith",
                            status = CharacterStatus.ALIVE,
                            species = "Human",
                            type = "",
                            gender = CharacterGender.MALE,
                            image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                            favorite = false
                        ), onClick = {}, onFavoriteClick = {})
                }
            }
        }
    }
}