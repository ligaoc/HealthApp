package com.healthapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.healthapp.ui.auth.LoginScreen
import com.healthapp.ui.auth.SplashScreen
import com.healthapp.ui.common.*
import com.healthapp.ui.doctor.alarms.AlarmDetailScreen
import com.healthapp.ui.doctor.patients.PatientDetailScreen
import com.healthapp.ui.patient.device.DeviceManagementScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")

    // 患者端
    object PatientHome : Screen("patient/home")
    object PatientData : Screen("patient/data")
    object PatientDataDetail : Screen("patient/data/{type}")
    object PatientSOS : Screen("patient/sos")
    object PatientAlarmList : Screen("patient/alarms")
    object PatientProfile : Screen("patient/profile")

    // 医生端
    object DoctorDashboard : Screen("doctor/dashboard")
    object DoctorPatients : Screen("doctor/patients")
    object DoctorPatientDetail : Screen("doctor/patient/{patientId}") {
        fun createRoute(patientId: String) = "doctor/patient/$patientId"
    }
    object DoctorAlarms : Screen("doctor/alarms")
    object DoctorAlarmDetail : Screen("doctor/alarm/{alarmId}") {
        fun createRoute(alarmId: String) = "doctor/alarm/$alarmId"
    }
    object DoctorProfile : Screen("doctor/profile")

    // 通用页面
    object PersonalInfo : Screen("common/personal-info")
    object NotificationSettings : Screen("common/notification-settings")
    object PrivacySettings : Screen("common/privacy-settings")
    object SystemSettings : Screen("common/system-settings")
    object About : Screen("common/about")
    object MessageList : Screen("common/messages")
    object DeviceManagement : Screen("patient/devices")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToPatientHome = {
                    navController.navigate(Screen.PatientHome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDoctorDashboard = {
                    navController.navigate(Screen.DoctorDashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == "patient") {
                        navController.navigate(Screen.PatientHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.DoctorDashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // 患者端页面
        composable(Screen.PatientHome.route) {
            PatientMainScreen(
                navController = navController,
                onNavigateToPersonalInfo = { navController.navigate(Screen.PersonalInfo.route) },
                onNavigateToDevices = { navController.navigate(Screen.DeviceManagement.route) },
                onNavigateToNotifications = { navController.navigate(Screen.NotificationSettings.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.PrivacySettings.route) },
                onNavigateToSettings = { navController.navigate(Screen.SystemSettings.route) },
                onNavigateToAbout = { navController.navigate(Screen.About.route) },
                onNavigateToMessages = { navController.navigate(Screen.MessageList.route) }
            )
        }

        // 医生端页面
        composable(Screen.DoctorDashboard.route) {
            DoctorMainScreen(
                navController = navController,
                onNavigateToPersonalInfo = { navController.navigate(Screen.PersonalInfo.route) },
                onNavigateToNotifications = { navController.navigate(Screen.NotificationSettings.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.PrivacySettings.route) },
                onNavigateToSettings = { navController.navigate(Screen.SystemSettings.route) },
                onNavigateToAbout = { navController.navigate(Screen.About.route) },
                onNavigateToMessages = { navController.navigate(Screen.MessageList.route) },
                onNavigateToPatientDetail = { patientId ->
                    navController.navigate(Screen.DoctorPatientDetail.createRoute(patientId))
                },
                onNavigateToAlarmDetail = { alarmId ->
                    navController.navigate(Screen.DoctorAlarmDetail.createRoute(alarmId))
                }
            )
        }

        // 通用页面
        composable(Screen.PersonalInfo.route) {
            PersonalInfoScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.PrivacySettings.route) {
            PrivacySettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.SystemSettings.route) {
            SystemSettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.MessageList.route) {
            MessageListScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.DeviceManagement.route) {
            DeviceManagementScreen(onBack = { navController.popBackStack() })
        }

        // 医生端详情页面
        composable(
            route = Screen.DoctorPatientDetail.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            PatientDetailScreen(
                patientId = patientId,
                onBack = { navController.popBackStack() },
                onAlarmClick = { alarmId ->
                    navController.navigate(Screen.DoctorAlarmDetail.createRoute(alarmId))
                }
            )
        }

        composable(
            route = Screen.DoctorAlarmDetail.route,
            arguments = listOf(navArgument("alarmId") { type = NavType.StringType })
        ) { backStackEntry ->
            val alarmId = backStackEntry.arguments?.getString("alarmId") ?: ""
            AlarmDetailScreen(
                alarmId = alarmId,
                onBack = { navController.popBackStack() },
                onPatientClick = { patientId ->
                    navController.navigate(Screen.DoctorPatientDetail.createRoute(patientId))
                }
            )
        }
    }
}
