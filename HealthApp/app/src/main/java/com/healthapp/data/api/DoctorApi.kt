package com.healthapp.data.api

import com.healthapp.data.model.ApiResponse
import com.healthapp.data.model.doctor.DoctorStatisticsResponse
import com.healthapp.data.model.doctor.PatientDetailResponse
import com.healthapp.data.model.doctor.PatientListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DoctorApi {
    @GET("api/doctor/statistics")
    suspend fun getStatistics(@Query("doctorId") doctorId: String): ApiResponse<DoctorStatisticsResponse>

    @GET("api/doctor/patients")
    suspend fun getPatients(
        @Query("doctorId") doctorId: String,
        @Query("keyword") keyword: String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<PatientListResponse>

    @GET("api/doctor/patient/{patientId}")
    suspend fun getPatientDetail(@Path("patientId") patientId: String): ApiResponse<PatientDetailResponse>
}
