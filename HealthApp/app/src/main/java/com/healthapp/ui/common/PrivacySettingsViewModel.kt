package com.healthapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.model.user.PrivacySettings
import com.healthapp.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PrivacySettingsUiState {
    object Loading : PrivacySettingsUiState()
    data class Success(val settings: PrivacySettings) : PrivacySettingsUiState()
    data class Error(val message: String) : PrivacySettingsUiState()
}

@HiltViewModel
class PrivacySettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PrivacySettingsUiState>(PrivacySettingsUiState.Loading)
    val uiState: StateFlow<PrivacySettingsUiState> = _uiState

    private var currentSettings = PrivacySettings()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = PrivacySettingsUiState.Loading
            userSettingsRepository.getPrivacySettings().fold(
                onSuccess = { settings ->
                    currentSettings = settings
                    _uiState.value = PrivacySettingsUiState.Success(settings)
                },
                onFailure = { e ->
                    _uiState.value = PrivacySettingsUiState.Error(e.message ?: "加载失败")
                }
            )
        }
    }

    fun updateSetting(
        shareWithDoctor: Boolean? = null,
        shareWithFamily: Boolean? = null,
        allowDataExport: Boolean? = null,
        allowAnonymousAnalysis: Boolean? = null
    ) {
        val newSettings = currentSettings.copy(
            shareWithDoctor = shareWithDoctor ?: currentSettings.shareWithDoctor,
            shareWithFamily = shareWithFamily ?: currentSettings.shareWithFamily,
            allowDataExport = allowDataExport ?: currentSettings.allowDataExport,
            allowAnonymousAnalysis = allowAnonymousAnalysis ?: currentSettings.allowAnonymousAnalysis
        )

        viewModelScope.launch {
            userSettingsRepository.updatePrivacySettings(newSettings).fold(
                onSuccess = { settings ->
                    currentSettings = settings
                    _uiState.value = PrivacySettingsUiState.Success(settings)
                },
                onFailure = { e ->
                    _uiState.value = PrivacySettingsUiState.Error(e.message ?: "保存失败")
                }
            )
        }
    }

    fun exportData() {
        // 模拟导出数据
        viewModelScope.launch {
            // 实际实现中会调用API
        }
    }

    fun deleteAccount() {
        // 模拟注销账号
        viewModelScope.launch {
            // 实际实现中会调用API
        }
    }
}
