package com.healthapp.domain.repository

import com.healthapp.data.model.device.DeviceData
import com.healthapp.data.model.health.HealthScoreResponse
import com.healthapp.data.model.health.HistoryHealthResponse
import com.healthapp.data.model.health.MedicationReminder
import com.healthapp.data.model.health.RealtimeHealthResponse

interface HealthRepository {
    suspend fun getRealtimeData(userId: String): Result<RealtimeHealthResponse>
    suspend fun getHistoryData(userId: String, type: String, range: String): Result<HistoryHealthResponse>
    suspend fun getHealthScore(userId: String): Result<HealthScoreResponse>
    suspend fun getMedicationReminders(userId: String): Result<List<MedicationReminder>>
    suspend fun getDeviceList(userId: String): Result<List<DeviceData>>
}
