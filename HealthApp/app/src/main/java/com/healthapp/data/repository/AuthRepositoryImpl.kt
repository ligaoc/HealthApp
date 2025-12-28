package com.healthapp.data.repository

import com.healthapp.data.api.AuthApi
import com.healthapp.data.local.UserPreferences
import com.healthapp.data.model.auth.LoginRequest
import com.healthapp.data.model.auth.RegisterRequest
import com.healthapp.domain.model.User
import com.healthapp.domain.model.UserRole
import com.healthapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(phone: String, password: String, role: UserRole): Result<User> {
        return try {
            val response = authApi.login(
                LoginRequest(
                    phone = phone,
                    password = password,
                    role = role.toApiString()
                )
            )

            if (response.isSuccess && response.data != null) {
                val loginData = response.data
                val user = User(
                    userId = loginData.userId,
                    name = loginData.name,
                    phone = phone,
                    role = UserRole.fromString(loginData.role),
                    avatar = loginData.avatar,
                    gender = null,
                    age = null,
                    organizationId = null,
                    organizationName = null
                )
                userPreferences.saveUser(user, loginData.token)
                Result.success(user)
            } else {
                Result.failure(Exception(response.message ?: "登录失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        phone: String,
        password: String,
        name: String,
        role: UserRole
    ): Result<User> {
        return try {
            val response = authApi.register(
                RegisterRequest(
                    phone = phone,
                    password = password,
                    verifyCode = "1234", // Mock验证码
                    role = role.toApiString(),
                    name = name
                )
            )

            if (response.isSuccess && response.data != null) {
                val registerData = response.data
                val user = User(
                    userId = registerData.userId,
                    name = name,
                    phone = phone,
                    role = role,
                    avatar = null,
                    gender = null,
                    age = null,
                    organizationId = null,
                    organizationName = null
                )
                userPreferences.saveUser(user, registerData.token)
                Result.success(user)
            } else {
                Result.failure(Exception(response.message ?: "注册失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        userPreferences.clearUser()
    }

    override suspend fun getCurrentUser(): User? {
        return userPreferences.getUser()
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return userPreferences.isLoggedIn
    }

    override fun getCurrentUserFlow(): Flow<User?> {
        return userPreferences.currentUser
    }
}
