package com.healthapp.data.api

import com.healthapp.data.model.ApiResponse
import com.healthapp.data.model.auth.LoginRequest
import com.healthapp.data.model.auth.LoginResponse
import com.healthapp.data.model.auth.RegisterRequest
import com.healthapp.data.model.auth.RegisterResponse
import com.healthapp.data.model.auth.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterResponse>

    @GET("api/user/profile")
    suspend fun getProfile(): ApiResponse<UserProfileResponse>
}
