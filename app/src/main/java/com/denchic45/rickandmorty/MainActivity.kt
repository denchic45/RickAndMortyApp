package com.denchic45.rickandmorty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.denchic45.rickandmorty.ui.CharacterDetails
import com.denchic45.rickandmorty.ui.Characters
import com.denchic45.rickandmorty.ui.CharactersFilters
import com.denchic45.rickandmorty.ui.EpisodeDetails
import com.denchic45.rickandmorty.ui.Favorites
import com.denchic45.rickandmorty.ui.LocationDetails
import com.denchic45.rickandmorty.ui.Navigation
import com.denchic45.rickandmorty.ui.characterdetails.CharacterDetailsScreen
import com.denchic45.rickandmorty.ui.characters.CharactersFilterBottomSheet
import com.denchic45.rickandmorty.ui.characters.CharactersScreen
import com.denchic45.rickandmorty.ui.episodedetails.EpisodeBottomSheet
import com.denchic45.rickandmorty.ui.favorites.FavoritesScreen
import com.denchic45.rickandmorty.ui.locationdetails.LocationBottomSheet
import com.denchic45.rickandmorty.ui.theme.RickAndMortyAppTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.sharedKoinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            keepSplashScreen = false
        }
        enableEdgeToEdge()
        setContent {
            RickAndMortyAppTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold(bottomBar = {
                    AnimatedVisibility(
                        currentRoute == Characters::class.qualifiedName
                                || currentRoute == Favorites::class.qualifiedName
                                || currentRoute == CharactersFilters::class.qualifiedName,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentRoute == Characters::class.qualifiedName
                                        || currentRoute == CharactersFilters::class.qualifiedName,
                                onClick = {
                                    navController.navigate(Characters) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(Icons.Outlined.Explore, null) },
                                label = { Text("Explore") }
                            )
                            NavigationBarItem(
                                selected = currentRoute == Favorites::class.qualifiedName,
                                onClick = {
                                    navController.navigate(Favorites) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(Icons.Outlined.FavoriteBorder, null) },
                                label = { Text("Favorites") }
                            )
                        }
                    }
                }) { innerPadding ->
                    ModalBottomSheetLayout(
                        bottomSheetNavigator,
                    ) {
                        NavHost(
                            modifier = Modifier,
                            navController = navController,
                            startDestination = Characters,
                            route = Navigation::class
                        ) {
                            composable<Characters>(
                                enterTransition = { fadeIn() + slideInVertically { -it / 8 } },
                                exitTransition = { fadeOut() + slideOutVertically { it / 8 } },
                                popEnterTransition = { fadeIn() + slideInVertically { -it / 8 } },
                                popExitTransition = { fadeOut() + slideOutVertically { it / 8 } },
                            ) {
                                CharactersScreen(
                                    viewModel = it.sharedKoinViewModel(navController),
                                    navigateToCharacterDetails = { id ->
                                        navController.navigate(CharacterDetails(id))
                                    },
                                    navigateToFilters = {
                                        navController.navigate(CharactersFilters)
                                    }
                                )

                            }
                            composable<Favorites>(
                                enterTransition = { fadeIn() + slideInVertically { -it / 8 } },
                                exitTransition = { fadeOut() + slideOutVertically { it / 8 } },
                                popEnterTransition = { fadeIn() + slideInVertically { -it / 8 } },
                                popExitTransition = { fadeOut() + slideOutVertically { it / 8 } },
                            ) {
                                FavoritesScreen(
                                    navigateToCharacterDetails = {
                                        navController.navigate(CharacterDetails(it))
                                    }
                                )
                            }
                            composable<CharacterDetails> {
                                CharacterDetailsScreen(
                                    navigateBack = { navController.popBackStack() },
                                    navigateToLocation = { navController.navigate(LocationDetails(it)) },
                                    navigateToEpisode = { navController.navigate(EpisodeDetails(it)) }
                                )
                            }
                            bottomSheet<CharactersFilters> {
                                CharactersFilterBottomSheet(
                                    viewModel = it.sharedKoinViewModel(navController),
                                    navigateBack = { navController.popBackStack() },
                                )
                            }
                            bottomSheet<LocationDetails> {
                                LocationBottomSheet(
                                    navigateBack = { navController.popBackStack() },
                                    navigateToCharacterDetails = {
                                        navController.navigate(CharacterDetails(it))
                                    }
                                )
                            }
                            bottomSheet<EpisodeDetails> {
                                EpisodeBottomSheet(
                                    navigateBack = { navController.popBackStack() },
                                    navigateToCharacterDetails = {
                                        navController.navigate(CharacterDetails(it))
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}