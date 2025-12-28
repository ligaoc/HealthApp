package com.healthapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.model.user.UpdateProfileRequest
import com.healthapp.domain.repository.AuthRepository
import com.healthapp.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserProfile(
    val userId: String,
    val name: String,
    val phone: String,
    val gender: String?,
    val age: Int?,
    val height: Int?,
    val weight: Float?
)

sealed class PersonalInfoUiState {
    object Loading : PersonalInfoUiState()
    data class Success(val profile: UserProfile) : PersonalInfoUiState()
    data class Error(val message: String) : PersonalInfoUiState()
}

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PersonalInfoUiState>(PersonalInfoUiState.Loading)
    val uiState: StateFlow<PersonalInfoUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = PersonalInfoUiState.Loading
            authRepository.getProfile().fold(
                onSuccess = { response ->
                    _uiState.value = PersonalInfoUiState.Success(
                        UserProfile(
                            userId = response.userId,
                            name = response.name,
                            phone = response.phone,
                            gender = response.gender,
                            age = response.age,
                            height = response.height,
                            weight = response.weight
                        )
                    )
                },
                onFailure = { e ->
                    _uiState.value = PersonalInfoUiState.Error(e.message ?: "加载失败")
                }
            )
        }
    }

    fun updateProfile(name: String, gender: String?, age: Int?, height: Int?, weight: Float?) {
        viewModelScope.launch {
            val request = UpdateProfileRequest(
                name = name.takeIf { it.isNotBlank() },
                gender = gender?.takeIf { it.isNotBlank() },
                age = age,
                height = height,
                weight = weight
            )
            userSettingsRepository.updateProfile(request).fold(
                onSuccess = { response ->
                    _uiState.value = PersonalInfoUiState.Success(
                        UserProfile(
                            userId = response.userId,
                            name = response.name,
                            phone = response.phone,
                            gender = response.gender,
                            age = response.age,
                            height = response.height,
                            weight = response.weight
                        )
                    )
                },
                onFailure = { e ->
                    _uiState.value = PersonalInfoUiState.Error(e.message ?: "更新失败")
                }
            )
        }
    }
}
