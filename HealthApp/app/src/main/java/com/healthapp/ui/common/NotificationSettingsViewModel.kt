package com.healthapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.model.user.NotificationSettings
import com.healthapp.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NotificationSettingsUiState {
    object Loading : NotificationSettingsUiState()
    data class Success(val settings: NotificationSettings) : NotificationSettingsUiState()
    data class Error(val message: String) : NotificationSettingsUiState()
}

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationSettingsUiState>(NotificationSettingsUiState.Loading)
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState

    private var currentSettings = NotificationSettings()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = NotificationSettingsUiState.Loading
            userSettingsRepository.getNotificationSettings().fold(
                onSuccess = { settings ->
                    currentSettings = settings
                    _uiState.value = NotificationSettingsUiState.Success(settings)
                },
                onFailure = { e ->
                    _uiState.value = NotificationSettingsUiState.Error(e.message ?: "加载失败")
                }
            )
        }
    }

    fun updateSetting(
        alarmEnabled: Boolean? = null,
        medicationEnabled: Boolean? = null,
        healthTipsEnabled: Boolean? = null,
        systemEnabled: Boolean? = null,
        quietTimeEnabled: Boolean? = null,
        quietTimeStart: String? = null,
        quietTimeEnd: String? = null
    ) {
        val newSettings = currentSettings.copy(
            alarmEnabled = alarmEnabled ?: currentSettings.alarmEnabled,
            medicationEnabled = medicationEnabled ?: currentSettings.medicationEnabled,
            healthTipsEnabled = healthTipsEnabled ?: currentSettings.healthTipsEnabled,
            systemEnabled = systemEnabled ?: currentSettings.systemEnabled,
            quietTimeEnabled = quietTimeEnabled ?: currentSettings.quietTimeEnabled,
            quietTimeStart = quietTimeStart ?: currentSettings.quietTimeStart,
            quietTimeEnd = quietTimeEnd ?: currentSettings.quietTimeEnd
        )

        viewModelScope.launch {
            userSettingsRepository.updateNotificationSettings(newSettings).fold(
                onSuccess = { settings ->
                    currentSettings = settings
                    _uiState.value = NotificationSettingsUiState.Success(settings)
                },
                onFailure = { e ->
                    _uiState.value = NotificationSettingsUiState.Error(e.message ?: "保存失败")
                }
            )
        }
    }
}
