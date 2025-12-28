package com.healthapp.data.model.alarm

data class AlarmListResponse(
    val total: Int,
    val page: Int,
    val size: Int,
    val alarms: List<AlarmData>
)

data class AlarmData(
    val alarmId: String,
    val type: String,
    val level: String,
    val title: String,
    val content: String,
    val patientId: String,
    val patientName: String,
    val deviceId: String,
    val location: String,
    val time: String,
    val status: String
)

data class AlarmDetailResponse(
    val alarmId: String,
    val type: String,
    val level: String,
    val title: String,
    val content: String,
    val patientId: String,
    val patientName: String,
    val deviceId: String,
    val time: String,
    val status: String,
    val patientInfo: PatientBasicInfo,
    val deviceInfo: DeviceInfo?,
    val location: LocationInfo?,
    val emergencyContacts: List<EmergencyContact>
)

data class PatientBasicInfo(
    val id: String,
    val name: String,
    val age: Int,
    val phone: String,
    val medicalHistory: List<String>
)

data class DeviceInfo(
    val deviceId: String,
    val deviceName: String
)

data class LocationInfo(
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class EmergencyContact(
    val name: String,
    val phone: String,
    val relation: String
)

data class SOSRequest(
    val userId: String,
    val location: LocationData?,
    val description: String
)

data class LocationData(
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class SOSResponse(
    val alarmId: String,
    val estimatedResponseTime: String
)

data class HandleAlarmRequest(
    val alarmId: String,
    val handleType: String,
    val handleResult: String,
    val handlerId: String
)
