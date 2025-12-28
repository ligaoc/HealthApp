package com.healthapp.domain.repository

import com.healthapp.data.model.alarm.AlarmData
import com.healthapp.data.model.alarm.AlarmDetailResponse

interface AlarmRepository {
    suspend fun getAlarmList(userId: String, role: String, page: Int, size: Int): Result<List<AlarmData>>
    suspend fun getAlarmDetail(alarmId: String): Result<AlarmDetailResponse>
    suspend fun handleAlarm(alarmId: String, handleType: String, handleResult: String, handlerId: String): Result<Unit>
    suspend fun sendSOS(userId: String, address: String?, latitude: Double?, longitude: Double?, description: String): Result<String>
}
