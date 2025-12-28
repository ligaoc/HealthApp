package com.healthapp.data.model.health

data class RealtimeHealthResponse(
    val heartRate: Int,
    val bloodPressure: BloodPressureData,
    val bloodOxygen: Int,
    val temperature: Float,
    val bloodSugar: Float,
    val updateTime: String
)

data class BloodPressureData(
    val systolic: Int,
    val diastolic: Int
)

data class HistoryHealthResponse(
    val type: String,
    val unit: String,
    val records: List<HealthRecord>,
    val average: Float,
    val max: Float,
    val min: Float
)

data class HealthRecord(
    val time: String,
    val value: Float
)

data class HealthScoreResponse(
    val totalScore: Int,
    val details: ScoreDetails,
    val trend: String,
    val suggestion: String
)

data class ScoreDetails(
    val heartRate: Int,
    val bloodPressure: Int,
    val bloodOxygen: Int,
    val sleep: Int,
    val activity: Int
)

data class MedicationRemindersResponse(
    val reminders: List<MedicationReminder>
)

data class MedicationReminder(
    val id: String,
    val medicationName: String,
    val time: String,
    val status: String
)
