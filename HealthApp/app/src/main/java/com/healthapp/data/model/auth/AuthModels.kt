package com.healthapp.data.model.auth

data class LoginRequest(
    val phone: String,
    val password: String,
    val role: String
)

data class LoginResponse(
    val userId: String,
    val token: String,
    val role: String,
    val name: String,
    val avatar: String?
)

data class RegisterRequest(
    val phone: String,
    val password: String,
    val verifyCode: String,
    val role: String,
    val name: String
)

data class RegisterResponse(
    val userId: String,
    val token: String
)

data class UserProfileResponse(
    val userId: String,
    val name: String,
    val phone: String,
    val role: String,
    val avatar: String?,
    val gender: String?,
    val age: Int?,
    val height: Int?,
    val weight: Float?,
    val organizationId: String?,
    val organizationName: String?
)
