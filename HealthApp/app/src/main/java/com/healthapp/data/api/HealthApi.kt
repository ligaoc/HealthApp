package com.healthapp.data.api

import com.healthapp.data.model.ApiResponse
import com.healthapp.data.model.health.HealthScoreResponse
import com.healthapp.data.model.health.HistoryHealthResponse
import com.healthapp.data.model.health.MedicationRemindersResponse
import com.healthapp.data.model.health.RealtimeHealthResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HealthApi {
    @GET("api/health/realtime")
    suspend fun getRealtimeData(@Query("userId") userId: String): ApiResponse<RealtimeHealthResponse>

    @GET("api/health/history")
    suspend fun getHistoryData(
        @Query("userId") userId: String,
        @Query("type") type: String,
        @Query("range") range: String
    ): ApiResponse<HistoryHealthResponse>

    @GET("api/health/score")
    suspend fun getHealthScore(@Query("userId") userId: String): ApiResponse<HealthScoreResponse>

    @GET("api/health/medication-reminders")
    suspend fun getMedicationReminders(@Query("userId") userId: String): ApiResponse<MedicationRemindersResponse>
}
