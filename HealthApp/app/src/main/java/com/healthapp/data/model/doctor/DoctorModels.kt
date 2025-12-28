package com.healthapp.data.model.doctor

import com.healthapp.data.model.health.BloodPressureData

data class DoctorStatisticsResponse(
    val overview: OverviewData,
    val alarmTrend: List<TrendData>,
    val alarmTypeDistribution: List<DistributionData>,
    val deviceTypeDistribution: List<DistributionData>,
    val pendingTasks: List<PendingTaskData>
)

data class OverviewData(
    val totalPatients: Int,
    val onlineDevices: Int,
    val todayAlarms: Int,
    val abnormalPatients: Int
)

data class TrendData(
    val date: String,
    val count: Int
)

data class DistributionData(
    val type: String,
    val name: String,
    val count: Int
)

data class PendingTaskData(
    val type: String,
    val patientName: String,
    val content: String,
    val time: String,
    val level: String
)

data class PatientListResponse(
    val total: Int,
    val page: Int,
    val size: Int,
    val patients: List<PatientListItem>
)

data class PatientListItem(
    val patientId: String,
    val name: String,
    val gender: String,
    val age: Int,
    val phone: String,
    val diseases: List<String>,
    val riskLevel: String,
    val deviceStatus: String,
    val lastUpdateTime: String
)

data class PatientDetailResponse(
    val basicInfo: PatientBasicInfo,
    val healthArchive: HealthArchive,
    val realtimeData: RealtimeData,
    val devices: List<DeviceInfo>,
    val recentAlarms: List<RecentAlarm>,
    val medications: List<MedicationInfo>,
    val emergencyContacts: List<EmergencyContact>
)

data class PatientBasicInfo(
    val patientId: String,
    val name: String,
    val gender: String,
    val age: Int,
    val phone: String,
    val address: String
)

data class HealthArchive(
    val height: Int,
    val weight: Int,
    val bmi: Float,
    val diseases: List<String>,
    val allergies: List<String>,
    val riskLevel: String
)

data class RealtimeData(
    val heartRate: Int,
    val bloodPressure: BloodPressureData,
    val bloodOxygen: Int,
    val temperature: Float,
    val updateTime: String
)

data class DeviceInfo(
    val deviceId: String,
    val deviceName: String,
    val status: String,
    val battery: Int
)

data class RecentAlarm(
    val alarmId: String,
    val type: String,
    val time: String,
    val status: String
)

data class MedicationInfo(
    val name: String,
    val dosage: String,
    val frequency: String
)

data class EmergencyContact(
    val name: String,
    val phone: String,
    val relation: String
)
