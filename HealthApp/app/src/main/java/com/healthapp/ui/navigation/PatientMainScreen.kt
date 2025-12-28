package com.healthapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.healthapp.ui.patient.home.PatientHomeScreen
import com.healthapp.ui.patient.data.HealthDataScreen
import com.healthapp.ui.patient.sos.SOSScreen
import com.healthapp.ui.patient.profile.PatientProfileScreen
import com.healthapp.ui.theme.PrimaryBlue

data class PatientNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun PatientMainScreen(
    navController: NavController,
    onNavigateToPersonalInfo: () -> Unit = {},
    onNavigateToDevices: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        PatientNavItem("首页", Icons.Filled.Home, Icons.Outlined.Home),
        PatientNavItem("数据", Icons.Filled.ShowChart, Icons.Outlined.ShowChart),
        PatientNavItem("求助", Icons.Filled.Home, Icons.Outlined.Home), // SOS用特殊图标
        PatientNavItem("我的", Icons.Filled.Person, Icons.Outlined.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryBlue,
                            selectedTextColor = PrimaryBlue,
                            indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedIndex) {
                0 -> PatientHomeScreen(onNavigateToMessages = onNavigateToMessages)
                1 -> HealthDataScreen()
                2 -> SOSScreen()
                3 -> PatientProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToPersonalInfo = onNavigateToPersonalInfo,
                    onNavigateToDevices = onNavigateToDevices,
                    onNavigateToNotifications = onNavigateToNotifications,
                    onNavigateToPrivacy = onNavigateToPrivacy,
                    onNavigateToSettings = onNavigateToSettings,
                    onNavigateToAbout = onNavigateToAbout
                )
            }
        }
    }
}
