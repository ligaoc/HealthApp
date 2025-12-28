package com.healthapp.data.api

import com.healthapp.data.model.ApiResponse
import com.healthapp.data.model.alarm.AlarmDetailResponse
import com.healthapp.data.model.alarm.AlarmListResponse
import com.healthapp.data.model.alarm.HandleAlarmRequest
import com.healthapp.data.model.alarm.SOSRequest
import com.healthapp.data.model.alarm.SOSResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AlarmApi {
    @GET("api/alarm/list")
    suspend fun getAlarmList(
        @Query("userId") userId: String,
        @Query("role") role: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<AlarmListResponse>

    @GET("api/alarm/detail")
    suspend fun getAlarmDetail(
        @Query("alarmId") alarmId: String
    ): ApiResponse<AlarmDetailResponse>

    @POST("api/alarm/sos")
    suspend fun sendSOS(@Body request: SOSRequest): ApiResponse<SOSResponse>

    @POST("api/alarm/handle")
    suspend fun handleAlarm(@Body request: HandleAlarmRequest): ApiResponse<Unit>
}
