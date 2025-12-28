package com.healthapp.data.repository

import com.healthapp.data.api.AlarmApi
import com.healthapp.data.model.alarm.AlarmData
import com.healthapp.data.model.alarm.AlarmDetailResponse
import com.healthapp.data.model.alarm.HandleAlarmRequest
import com.healthapp.data.model.alarm.LocationData
import com.healthapp.data.model.alarm.SOSRequest
import com.healthapp.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmApi: AlarmApi
) : AlarmRepository {

    override suspend fun getAlarmList(userId: String, role: String, page: Int, size: Int): Result<List<AlarmData>> {
        return try {
            val response = alarmApi.getAlarmList(userId, role, page, size)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data.alarms)
            } else {
                Result.failure(Exception(response.message ?: "获取失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlarmDetail(alarmId: String): Result<AlarmDetailResponse> {
        return try {
            val response = alarmApi.getAlarmDetail(alarmId)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "获取失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun handleAlarm(
        alarmId: String,
        handleType: String,
        handleResult: String,
        handlerId: String
    ): Result<Unit> {
        return try {
            val request = HandleAlarmRequest(alarmId, handleType, handleResult, handlerId)
            val response = alarmApi.handleAlarm(request)
            if (response.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "处理失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendSOS(
        userId: String,
        address: String?,
        latitude: Double?,
        longitude: Double?,
        description: String
    ): Result<String> {
        return try {
            val location = if (address != null && latitude != null && longitude != null) {
                LocationData(address, latitude, longitude)
            } else null
            val request = SOSRequest(userId, location, description)
            val response = alarmApi.sendSOS(request)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data.alarmId)
            } else {
                Result.failure(Exception(response.message ?: "发送失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
