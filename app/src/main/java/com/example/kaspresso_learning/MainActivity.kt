package com.example.kaspresso_learning

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kaspresso_learning.data.Tea
import com.example.kaspresso_learning.data.TeaRepository
import com.example.kaspresso_learning.features.brewing.NewBrewingScreen
import com.example.kaspresso_learning.features.catalog.TeaSelectScreen
import com.example.kaspresso_learning.features.feed.FeedScreen
import com.example.kaspresso_learning.features.login.AvatarSelectScreen
import com.example.kaspresso_learning.features.login.NameInputScreen
import com.example.kaspresso_learning.features.profile.ProfileScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TeaLogApp()
            }
        }
    }
}

@Composable
private fun TeaLogApp() {
    val navController = rememberNavController()
    var userName by remember { mutableStateOf("User") }
    var userAvatarIndex by remember { mutableIntStateOf(0) }
    var selectedTea by remember { mutableStateOf<Tea?>(null) }

    val avatarRoute = stringResource(R.string.route_avatar_screen)
    val nameInputRoute = stringResource(R.string.route_name_input_screen)
    val feedRoute = stringResource(R.string.route_feed_screen)
    val newBrewingRoute = stringResource(R.string.route_new_brewing_screen)
    val teaSelectRoute = stringResource(R.string.route_tea_select_screen)
    val profileRoute = stringResource(R.string.route_profile_screen)

    NavHost(navController = navController, startDestination = avatarRoute) {

        // 1. Avatar
        composable(avatarRoute) {
            AvatarSelectScreen(
                onAvatarSelected = { avatarIndex ->
                    userAvatarIndex = avatarIndex
                    navController.navigate(nameInputRoute)
                }
            )
        }

        // 2. Name Input
        composable(nameInputRoute) {
            NameInputScreen(
                onLoginClick = { name ->
                    userName = name
                    navController.navigate(feedRoute) {
                        popUpTo(avatarRoute) { inclusive = true }
                    }
                }
            )
        }

        // 3. Feed
        composable(feedRoute) {
            FeedScreen(
                userName = userName,
                entries = TeaRepository.entries,
                onNewBrewingClick = {
                    selectedTea = null
                    navController.navigate(newBrewingRoute)
                },
                bottomBar = { BottomNav(navController, feedRoute, profileRoute) }
            )
        }

        // 4. New Brewing
        composable(newBrewingRoute) {
            NewBrewingScreen(
                selectedTea = selectedTea,
                onSelectTeaClick = {
                    navController.navigate(teaSelectRoute)
                },
                onBack = { navController.popBackStack() },
                onSave = { tea, weight, volume, vessel, infusions ->
                    TeaRepository.addEntry(
                        authorName = userName,
                        authorAvatarIndex = userAvatarIndex,
                        tea = tea,
                        weightGrams = weight,
                        volumeMl = volume,
                        vessel = vessel,
                        infusions = infusions
                    )
                    navController.popBackStack(feedRoute, inclusive = false)
                }
            )
        }

        // 5. Tea Select
        composable(teaSelectRoute) {
            TeaSelectScreen(
                teas = TeaRepository.allTeas,
                onTeaSelected = { tea ->
                    selectedTea = tea
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // 6. Profile
        composable(profileRoute) {
            ProfileScreen(
                userName = userName,
                userAvatarIndex = userAvatarIndex,
                userEntries = TeaRepository.getUserEntries(userName),
                onLogout = {
                    navController.navigate(avatarRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                bottomBar = { BottomNav(navController, feedRoute, profileRoute) }
            )
        }
    }
}

@Composable
private fun BottomNav(navController: NavHostController, feedRoute: String, profileRoute: String) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == feedRoute,
            onClick = {
                if (currentRoute != feedRoute) {
                    navController.navigate(feedRoute) {
                        popUpTo(feedRoute) { inclusive = true }
                    }
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Лента") },
            label = { Text("Лента") }
        )
        NavigationBarItem(
            selected = currentRoute == profileRoute,
            onClick = {
                if (currentRoute != profileRoute) {
                    navController.navigate(profileRoute) {
                        popUpTo(feedRoute)
                    }
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
            label = { Text("Профиль") }
        )
    }
}
