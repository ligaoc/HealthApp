package com.healthapp.data.repository

import com.healthapp.data.api.DeviceApi
import com.healthapp.data.api.HealthApi
import com.healthapp.data.model.device.DeviceData
import com.healthapp.data.model.health.HealthScoreResponse
import com.healthapp.data.model.health.HistoryHealthResponse
import com.healthapp.data.model.health.MedicationReminder
import com.healthapp.data.model.health.RealtimeHealthResponse
import com.healthapp.domain.repository.HealthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepositoryImpl @Inject constructor(
    private val healthApi: HealthApi,
    private val deviceApi: DeviceApi
) : HealthRepository {

    override suspend fun getRealtimeData(userId: String): Result<RealtimeHealthResponse> {
        return try {
            val response = healthApi.getRealtimeData(userId)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "获取数据失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHistoryData(
        userId: String,
        type: String,
        range: String
    ): Result<HistoryHealthResponse> {
        return try {
            val response = healthApi.getHistoryData(userId, type, range)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "获取数据失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHealthScore(userId: String): Result<HealthScoreResponse> {
        return try {
            val response = healthApi.getHealthScore(userId)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "获取数据失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMedicationReminders(userId: String): Result<List<MedicationReminder>> {
        return try {
            val response = healthApi.getMedicationReminders(userId)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data.reminders)
            } else {
                Result.failure(Exception(response.message ?: "获取数据失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDeviceList(userId: String): Result<List<DeviceData>> {
        return try {
            val response = deviceApi.getDeviceList(userId)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data.devices)
            } else {
                Result.failure(Exception(response.message ?: "获取数据失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
