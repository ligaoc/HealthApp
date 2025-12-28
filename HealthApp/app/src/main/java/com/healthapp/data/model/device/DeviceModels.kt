package com.healthapp.data.model.device

data class DeviceListResponse(
    val devices: List<DeviceData>
)

data class DeviceData(
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,
    val status: String,
    val battery: Int,
    val lastSyncTime: String
)
