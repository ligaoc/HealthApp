package com.healthapp.ui.doctor.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.api.DoctorApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PatientDetailData(
    val id: String,
    val name: String,
    val gender: String,
    val age: Int,
    val phone: String,
    val heartRate: Int,
    val bloodPressure: String,
    val bloodOxygen: Int,
    val temperature: Float,
    val height: Int,
    val weight: Float,
    val bloodType: String,
    val medicalHistory: List<String>,
    val allergies: String,
    val devices: List<DeviceInfo>,
    val emergencyContacts: List<ContactInfo>
)

data class DeviceInfo(val name: String, val type: String, val status: String)
data class ContactInfo(val name: String, val phone: String, val relation: String)

sealed class PatientDetailUiState {
    object Loading : PatientDetailUiState()
    data class Success(val patient: PatientDetailData) : PatientDetailUiState()
    data class Error(val message: String) : PatientDetailUiState()
}

@HiltViewModel
class PatientDetailViewModel @Inject constructor(
    private val doctorApi: DoctorApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<PatientDetailUiState>(PatientDetailUiState.Loading)
    val uiState: StateFlow<PatientDetailUiState> = _uiState

    fun loadPatient(patientId: String) {
        viewModelScope.launch {
            _uiState.value = PatientDetailUiState.Loading
            try {
                val response = doctorApi.getPatientDetail(patientId)
                if (response.isSuccess && response.data != null) {
                    val data = response.data
                    _uiState.value = PatientDetailUiState.Success(
                        PatientDetailData(
                            id = data.basicInfo.patientId,
                            name = data.basicInfo.name,
                            gender = if (data.basicInfo.gender == "male") "男" else "女",
                            age = data.basicInfo.age,
                            phone = data.basicInfo.phone,
                            heartRate = data.realtimeData.heartRate,
                            bloodPressure = "${data.realtimeData.bloodPressure.systolic}/${data.realtimeData.bloodPressure.diastolic}",
                            bloodOxygen = data.realtimeData.bloodOxygen,
                            temperature = data.realtimeData.temperature,
                            height = data.healthArchive.height,
                            weight = data.healthArchive.weight.toFloat(),
                            bloodType = "A型", // Mock数据中没有血型
                            medicalHistory = data.healthArchive.diseases,
                            allergies = data.healthArchive.allergies.joinToString("、"),
                            devices = data.devices.map { DeviceInfo(it.deviceName, "智能设备", it.status) },
                            emergencyContacts = data.emergencyContacts.map { ContactInfo(it.name, it.phone, it.relation) }
                        )
                    )
                } else {
                    _uiState.value = PatientDetailUiState.Error(response.message ?: "加载失败")
                }
            } catch (e: Exception) {
                _uiState.value = PatientDetailUiState.Error(e.message ?: "加载失败")
            }
        }
    }
}
