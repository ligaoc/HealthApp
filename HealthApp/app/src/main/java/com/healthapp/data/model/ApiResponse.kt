package com.healthapp.data.model

data class ApiResponse<T>(
    val code: Int,
    val message: String? = null,
    val data: T? = null
) {
    val isSuccess: Boolean get() = code == 200
}
