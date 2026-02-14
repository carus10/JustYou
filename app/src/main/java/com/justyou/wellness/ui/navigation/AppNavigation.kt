package com.justyou.wellness.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.justyou.wellness.ui.screens.*
import com.justyou.wellness.ui.theme.BackgroundPrimary
import com.justyou.wellness.ui.theme.IconActive
import com.justyou.wellness.ui.theme.IconInactive
import com.justyou.wellness.viewmodel.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    data object Home : Screen("home", "Ana Sayfa", Icons.Default.Home)
    data object Telkin : Screen("telkin", "Telkin Benimseme", Icons.Default.Favorite)
    data object Istatistik : Screen("istatistik", "İstatistikler", Icons.Default.BarChart)
    data object Ayarlar : Screen("ayarlar", "Ayarlar")
    data object Bilgi : Screen("bilgi", "Bilgi")
    data object Egitim : Screen("egitim", "Eğitim")
    data object Challenge : Screen("challenge", "Challenge")
}

// 3 tab: İstatistikler, Ana Sayfa, Telkin Benimseme (Ayarlar yok - dişli ikondan erişilir)
val bottomNavItems = listOf(Screen.Istatistik, Screen.Home, Screen.Telkin)

@Composable
fun AppNavigation(
    anaSayfaViewModel: AnaSayfaViewModel,
    ayarlarViewModel: AyarlarViewModel,
    telkinViewModel: TelkinViewModel,
    challengeViewModel: ChallengeViewModel,
    istatistikViewModel: IstatistikViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        containerColor = BackgroundPrimary,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = BackgroundPrimary) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                            label = { },
                            alwaysShowLabel = false,
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = IconActive,
                                selectedTextColor = IconActive,
                                unselectedIconColor = IconInactive,
                                unselectedTextColor = IconInactive,
                                indicatorColor = BackgroundPrimary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                AnaSayfaScreen(
                    viewModel = anaSayfaViewModel,
                    onInfoClick = { navController.navigate(Screen.Bilgi.route) },
                    onSettingsClick = { navController.navigate(Screen.Ayarlar.route) }
                )
            }
            composable(Screen.Telkin.route) {
                TelkinScreen(
                    viewModel = telkinViewModel,
                    onSettingsClick = { navController.navigate(Screen.Ayarlar.route) },
                    onEgitimClick = { navController.navigate(Screen.Egitim.route) },
                    onChallengeClick = { navController.navigate(Screen.Challenge.route) }
                )
            }
            composable(Screen.Istatistik.route) {
                IstatistikScreen(viewModel = istatistikViewModel)
            }
            composable(Screen.Ayarlar.route) {
                AyarlarScreen(viewModel = ayarlarViewModel, onBack = { navController.popBackStack() })
            }
            composable(Screen.Bilgi.route) {
                BilgiScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Egitim.route) {
                EgitimScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Challenge.route) {
                ChallengeScreen(viewModel = challengeViewModel, onBack = { navController.popBackStack() })
            }
        }
    }
}
