package com.healthapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.domain.model.UserRole
import com.healthapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashNavigationState {
    object Loading : SplashNavigationState()
    object NavigateToLogin : SplashNavigationState()
    object NavigateToPatientHome : SplashNavigationState()
    object NavigateToDoctorDashboard : SplashNavigationState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationState = MutableStateFlow<SplashNavigationState>(SplashNavigationState.Loading)
    val navigationState: StateFlow<SplashNavigationState> = _navigationState.asStateFlow()

    fun checkLoginStatus() {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isLoggedIn().first()
            if (isLoggedIn) {
                val user = authRepository.getCurrentUser()
                _navigationState.value = when (user?.role) {
                    UserRole.PATIENT -> SplashNavigationState.NavigateToPatientHome
                    UserRole.DOCTOR -> SplashNavigationState.NavigateToDoctorDashboard
                    else -> SplashNavigationState.NavigateToLogin
                }
            } else {
                _navigationState.value = SplashNavigationState.NavigateToLogin
            }
        }
    }
}
