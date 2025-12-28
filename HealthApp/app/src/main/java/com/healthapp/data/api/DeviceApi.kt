package com.healthapp.data.api

import com.healthapp.data.model.ApiResponse
import com.healthapp.data.model.device.DeviceListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DeviceApi {
    @GET("api/device/bindlist")
    suspend fun getDeviceList(@Query("userId") userId: String): ApiResponse<DeviceListResponse>
}
