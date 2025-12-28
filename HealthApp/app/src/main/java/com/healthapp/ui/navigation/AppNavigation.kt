package com.healthapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.healthapp.ui.auth.LoginScreen
import com.healthapp.ui.auth.SplashScreen

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
    object DoctorPatientDetail : Screen("doctor/patient/{patientId}")
    object DoctorAlarms : Screen("doctor/alarms")
    object DoctorAlarmDetail : Screen("doctor/alarm/{alarmId}")
    object DoctorProfile : Screen("doctor/profile")
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

        // 患者端页面 - 占位
        composable(Screen.PatientHome.route) {
            PatientMainScreen(navController = navController)
        }

        // 医生端页面 - 占位
        composable(Screen.DoctorDashboard.route) {
            DoctorMainScreen(navController = navController)
        }
    }
}
