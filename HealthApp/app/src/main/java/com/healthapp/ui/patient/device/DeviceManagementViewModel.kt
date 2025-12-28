package com.healthapp.ui.patient.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.api.DeviceApi
import com.healthapp.data.local.UserPreferences
import com.healthapp.data.model.device.DeviceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DeviceManagementUiState {
    object Loading : DeviceManagementUiState()
    data class Success(val devices: List<DeviceData>) : DeviceManagementUiState()
    data class Error(val message: String) : DeviceManagementUiState()
}

@HiltViewModel
class DeviceManagementViewModel @Inject constructor(
    private val deviceApi: DeviceApi,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeviceManagementUiState>(DeviceManagementUiState.Loading)
    val uiState: StateFlow<DeviceManagementUiState> = _uiState

    init {
        loadDevices()
    }

    private fun loadDevices() {
        viewModelScope.launch {
            _uiState.value = DeviceManagementUiState.Loading
            try {
                val userId = userPreferences.currentUser.first()?.userId ?: ""
                val response = deviceApi.getDeviceList(userId)
                if (response.isSuccess && response.data != null) {
                    _uiState.value = DeviceManagementUiState.Success(response.data.devices)
                } else {
                    _uiState.value = DeviceManagementUiState.Error(response.message ?: "加载失败")
                }
            } catch (e: Exception) {
                _uiState.value = DeviceManagementUiState.Error(e.message ?: "加载失败")
            }
        }
    }

    fun bindDevice(deviceId: String) {
        // 模拟绑定设备
        viewModelScope.launch {
            loadDevices()
        }
    }

    fun unbindDevice(deviceId: String) {
        // 模拟解绑设备
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is DeviceManagementUiState.Success) {
                val updatedDevices = currentState.devices.filter { it.deviceId != deviceId }
                _uiState.value = DeviceManagementUiState.Success(updatedDevices)
            }
        }
    }
}
