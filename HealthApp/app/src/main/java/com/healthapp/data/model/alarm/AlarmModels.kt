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
