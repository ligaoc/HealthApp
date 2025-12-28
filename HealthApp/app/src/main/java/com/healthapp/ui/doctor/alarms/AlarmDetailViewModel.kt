package com.healthapp.ui.doctor.alarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.local.UserPreferences
import com.healthapp.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlarmDetailData(
    val alarmId: String,
    val type: String,
    val level: String,
    val title: String,
    val content: String,
    val time: String,
    val status: String,
    val patientId: String,
    val patientName: String,
    val patientAge: Int,
    val patientPhone: String,
    val medicalHistory: List<String>,
    val location: String?,
    val latitude: Double?,
    val longitude: Double?,
    val emergencyContacts: List<EmergencyContactData>
)

data class EmergencyContactData(val name: String, val phone: String, val relation: String)

sealed class AlarmDetailUiState {
    object Loading : AlarmDetailUiState()
    data class Success(val alarm: AlarmDetailData) : AlarmDetailUiState()
    data class Error(val message: String) : AlarmDetailUiState()
}

@HiltViewModel
class AlarmDetailViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlarmDetailUiState>(AlarmDetailUiState.Loading)
    val uiState: StateFlow<AlarmDetailUiState> = _uiState

    fun loadAlarm(alarmId: String) {
        viewModelScope.launch {
            _uiState.value = AlarmDetailUiState.Loading
            alarmRepository.getAlarmDetail(alarmId).fold(
                onSuccess = { data ->
                    _uiState.value = AlarmDetailUiState.Success(
                        AlarmDetailData(
                            alarmId = data.alarmId,
                            type = data.type,
                            level = data.level,
                            title = data.title,
                            content = data.content,
                            time = data.time,
                            status = data.status,
                            patientId = data.patientInfo.id,
                            patientName = data.patientInfo.name,
                            patientAge = data.patientInfo.age,
                            patientPhone = data.patientInfo.phone,
                            medicalHistory = data.patientInfo.medicalHistory,
                            location = data.location?.address,
                            latitude = data.location?.latitude,
                            longitude = data.location?.longitude,
                            emergencyContacts = data.emergencyContacts.map {
                                EmergencyContactData(it.name, it.phone, it.relation)
                            }
                        )
                    )
                },
                onFailure = { e ->
                    _uiState.value = AlarmDetailUiState.Error(e.message ?: "加载失败")
                }
            )
        }
    }

    fun handleAlarm(alarmId: String, handleType: String, handleResult: String) {
        viewModelScope.launch {
            val userId = userPreferences.currentUser.first()?.userId ?: ""
            alarmRepository.handleAlarm(alarmId, handleType, handleResult, userId).fold(
                onSuccess = {
                    // 重新加载告警详情
                    loadAlarm(alarmId)
                },
                onFailure = { e ->
                    _uiState.value = AlarmDetailUiState.Error(e.message ?: "处理失败")
                }
            )
        }
    }
}
