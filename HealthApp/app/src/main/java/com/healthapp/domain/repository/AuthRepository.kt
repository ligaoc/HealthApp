package com.healthapp.domain.repository

import com.healthapp.domain.model.User
import com.healthapp.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(phone: String, password: String, role: UserRole): Result<User>
    suspend fun register(phone: String, password: String, name: String, role: UserRole): Result<User>
    suspend fun logout()
    suspend fun getCurrentUser(): User?
    fun isLoggedIn(): Flow<Boolean>
    fun getCurrentUserFlow(): Flow<User?>
}
