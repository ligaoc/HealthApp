package com.healthapp.ui.patient.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.model.device.DeviceData
import com.healthapp.data.model.health.HealthScoreResponse
import com.healthapp.data.model.health.MedicationReminder
import com.healthapp.data.model.health.RealtimeHealthResponse
import com.healthapp.domain.model.User
import com.healthapp.domain.repository.AuthRepository
import com.healthapp.domain.repository.HealthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PatientHomeUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    val healthScore: HealthScoreResponse? = null,
    val realtimeData: RealtimeHealthResponse? = null,
    val devices: List<DeviceData> = emptyList(),
    val medicationReminders: List<MedicationReminder> = emptyList()
)

@HiltViewModel
class PatientHomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val healthRepository: HealthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientHomeUiState())
    val uiState: StateFlow<PatientHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val user = authRepository.getCurrentUser()
            _uiState.update { it.copy(user = user) }

            val userId = user?.userId ?: "U10001"

            // 并行加载数据
            launch {
                healthRepository.getHealthScore(userId).fold(
                    onSuccess = { score ->
                        _uiState.update { it.copy(healthScore = score) }
                    },
                    onFailure = { /* 忽略单个错误 */ }
                )
            }

            launch {
                healthRepository.getRealtimeData(userId).fold(
                    onSuccess = { data ->
                        _uiState.update { it.copy(realtimeData = data) }
                    },
                    onFailure = { /* 忽略单个错误 */ }
                )
            }

            launch {
                healthRepository.getDeviceList(userId).fold(
                    onSuccess = { devices ->
                        _uiState.update { it.copy(devices = devices) }
                    },
                    onFailure = { /* 忽略单个错误 */ }
                )
            }

            launch {
                healthRepository.getMedicationReminders(userId).fold(
                    onSuccess = { reminders ->
                        _uiState.update { it.copy(medicationReminders = reminders) }
                    },
                    onFailure = { /* 忽略单个错误 */ }
                )
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadData()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
}
