package com.denchic45.rickandmorty.ui.characterdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.domain.model.CharacterDetails
import com.denchic45.rickandmorty.domain.model.CharacterItem
import com.denchic45.rickandmorty.domain.model.EpisodeItem
import com.denchic45.rickandmorty.domain.model.LocationItem
import com.denchic45.rickandmorty.ui.HeaderItem
import com.denchic45.rickandmorty.ui.ResourceContent
import com.denchic45.rickandmorty.ui.ResultFailedBox
import com.denchic45.rickandmorty.ui.ResultFailedItem
import com.denchic45.rickandmorty.ui.characters.displayName
import com.denchic45.rickandmorty.ui.fullimage.FullScreenImage
import com.denchic45.rickandmorty.ui.theme.RickAndMortyAppTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun CharacterDetailsScreen(
    viewModel: CharacterDetailsViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateToLocation: (Int) -> Unit,
    navigateToEpisode: (Int) -> Unit
) {
    val resource by viewModel.characterState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    ResourceContent(resource, onFailed = { failure, value ->
        value?.let { state ->
            Box {
                CharacterDetailsContent(
                    state = state,
                    errorContent = {
                        ResultFailedItem(failure = failure, onRetryClick = viewModel::onRetry)
                    },
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::onRefresh,
                    navigateBack = navigateBack,
                    onFavoriteClick = viewModel::onFavoriteCLick,
                    onLocationClick = navigateToLocation,
                    onEpisodeClick = navigateToEpisode
                )
            }
        } ?: ResultFailedBox(failure = failure, onRetryClick = viewModel::onRetry)
    }) { state ->
        CharacterDetailsContent(
            state = state,
            errorContent = null,
            isRefreshing = isRefreshing,
            onRefresh = viewModel::onRefresh,
            navigateBack = navigateBack,
            onFavoriteClick = viewModel::onFavoriteCLick,
            onLocationClick = navigateToLocation,
            onEpisodeClick = navigateToEpisode
        )
    }
}

private val expandedAppBarHeight = 116.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailsContent(
    state: CharacterDetails,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    errorContent: (@Composable () -> Unit)?,
    navigateBack: () -> Unit,
    onFavoriteClick: (Boolean) -> Unit,
    onLocationClick: (Int) -> Unit,
    onEpisodeClick: (Int) -> Unit
) {
    with(state) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val scrollState = scrollBehavior.state
        val appBarExpanded by remember {
            derivedStateOf { scrollState.collapsedFraction < 0.9f }
        }
        Scaffold(
            topBar = {
                CollapsedAppBar(
                    visible = !appBarExpanded,
                    favorite = state.character.favorite,
                    title = character.name,
                    imageUrl = character.image,
                    errorContent = errorContent,
                    navigateBackClick = navigateBack,
                    onFavoriteClick = onFavoriteClick
                )
            }
        ) { innerPadding ->
            PullToRefreshBox(
                isRefreshing,
                onRefresh = onRefresh,
                Modifier.padding(innerPadding)
            ) {
                Column(
                    Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .verticalScroll(rememberScrollState())
                ) {
                    TopAppBar(
                        title = {
                            AppBarHeader(
                                modifier = Modifier,
                                character = character
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent
                        ),
                        expandedHeight = expandedAppBarHeight,
                        windowInsets = WindowInsets(0),
                        scrollBehavior = scrollBehavior
                    )
                    OutlinedCard(Modifier.padding(horizontal = 16.dp)) {
                        Column(Modifier.padding(vertical = 16.dp)) {
                            HeaderItem("About the character")
                            ListItem(
                                overlineContent = { Text("Gender") },
                                headlineContent = { Text(character.gender.displayName) }
                            )
                            origin?.let {
                                ListItem(
                                    modifier = Modifier.clickable { onLocationClick(origin.id) },
                                    overlineContent = { Text("Origin") },
                                    headlineContent = { Text(origin.name) }
                                )
                            } ?: ListItem(
                                overlineContent = { Text("Origin") },
                                headlineContent = { Text("Unknown") })

                            location?.let {
                                ListItem(
                                    modifier = Modifier.clickable { onLocationClick(location.id) },
                                    overlineContent = { Text("Last known location:") },
                                    headlineContent = { Text(location.name) }
                                )
                            } ?: ListItem(
                                overlineContent = { Text("Last known location:") },
                                headlineContent = { Text("Unknown") })
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    if (episodes.isNotEmpty())
                        OutlinedCard(Modifier.padding(horizontal = 16.dp)) {
                            Column(Modifier.padding(vertical = 16.dp)) {
                                HeaderItem("Episodes")
                                episodes.forEach { episode ->
                                    ListItem(
                                        modifier = Modifier.clickable { onEpisodeClick(episode.id) },
                                        headlineContent = {
                                            Text(episode.name)
                                        },
                                        supportingContent = {
                                            Text(episode.airDateText)
                                        },
                                        trailingContent = {
                                            Text(episode.episode)
                                        }
                                    )
                                }
                            }
                        }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollapsedAppBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    favorite: Boolean,
    imageUrl: String,
    title: String,
    errorContent: (@Composable () -> Unit)?,
    navigateBackClick: () -> Unit,
    onFavoriteClick: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.shadow(
            elevation = if (visible) 8.dp else 0.dp,
            shape = RoundedCornerShape(0.dp),
            clip = false
        )
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(navigateBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { onFavoriteClick(!favorite) }) {
                    Icon(
                        if (favorite) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        null,
                        tint = if (favorite) MaterialTheme.colorScheme.primary
                        else Color.Unspecified
                    )
                }
            },
            title = {
                Column {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween()),
                        exit = fadeOut(animationSpec = tween())
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape),
                                model = imageUrl,
                                contentDescription = null
                            )
                            Text(
                                title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors()
        )
        errorContent?.invoke()
    }
}

@Composable
fun AppBarHeader(
    modifier: Modifier = Modifier,
    character: CharacterItem
) {
    LocalContext.current
    var show by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (show) {
                FullScreenImage(url = character.image, onDismissRequest = { show = false })
            }
            AsyncImage(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .clickable { show = true },
                model = character.image,
                contentDescription = null
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(character.name, style = MaterialTheme.typography.titleLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .clip(CircleShape)
                            .size(8.dp)
                            .background(
                                when (character.status) {
                                    CharacterStatus.ALIVE -> MaterialTheme.colorScheme.tertiaryContainer
                                    CharacterStatus.DEAD -> MaterialTheme.colorScheme.errorContainer
                                    CharacterStatus.UNKNOWN -> MaterialTheme.colorScheme.inverseSurface
                                }
                            )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "${character.status.displayName} - ${character.species}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun CharacterStatus(status: CharacterStatus) {
    Box(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                when (status) {
                    CharacterStatus.ALIVE -> MaterialTheme.colorScheme.tertiaryContainer
                    CharacterStatus.DEAD -> MaterialTheme.colorScheme.errorContainer
                    CharacterStatus.UNKNOWN -> MaterialTheme.colorScheme.inverseSurface
                }
            )
            .padding(4.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            status.displayName,
            color = when (status) {
                CharacterStatus.ALIVE -> MaterialTheme.colorScheme.onTertiaryContainer
                CharacterStatus.DEAD -> MaterialTheme.colorScheme.onErrorContainer
                CharacterStatus.UNKNOWN -> MaterialTheme.colorScheme.inverseOnSurface
            }
        )
    }
}

@Preview
@Composable
private fun CharacterDetailsContentPreview() {
    RickAndMortyAppTheme {
        CharacterDetailsContent(
            state = CharacterDetails(
                character = CharacterItem(
                    id = 0,
                    name = "Rick",
                    status = CharacterStatus.ALIVE,
                    species = "Human",
                    type = "",
                    gender = CharacterGender.MALE,
                    image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNFocitPbQwsSkhMyZTXavJZXP_TC5-EQt-A&s",
                    favorite = false
                ),
                origin = LocationItem(
                    id = 0,
                    name = "Earth",
                    type = "",
                    dimension = "unknown"
                ),
                location = LocationItem(
                    id = 0,
                    name = "Earth",
                    type = "",
                    dimension = "unknown"
                ),
                episodes = listOf(
                    EpisodeItem(
                        id = 0,
                        name = "Sample episode",
                        airDate = LocalDate.now(),
                        episode = "12"
                    ),
                    EpisodeItem(
                        id = 0,
                        name = "Sample episode",
                        airDate = LocalDate.now(),
                        episode = "12"
                    )
                )
            ),
            isRefreshing = false,
            onRefresh = {},
            errorContent = {},
            navigateBack = {},
            onFavoriteClick = {},
            onLocationClick = { },
            onEpisodeClick = { }
        )
    }
}