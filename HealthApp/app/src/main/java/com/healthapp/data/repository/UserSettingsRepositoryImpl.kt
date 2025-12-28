package com.healthapp.data.repository

import com.healthapp.data.api.UserApi
import com.healthapp.data.model.auth.UserProfileResponse
import com.healthapp.data.model.user.Message
import com.healthapp.data.model.user.NotificationSettings
import com.healthapp.data.model.user.PrivacySettings
import com.healthapp.data.model.user.UpdateProfileRequest
import com.healthapp.domain.repository.UserSettingsRepository
import javax.inject.Inject

class UserSettingsRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserSettingsRepository {

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<UserProfileResponse> {
        return try {
            val response = userApi.updateProfile(request)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "更新失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotificationSettings(): Result<NotificationSettings> {
        return try {
            val response = userApi.getNotificationSettings()
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "获取失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettings): Result<NotificationSettings> {
        return try {
            val response = userApi.updateNotificationSettings(settings)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "保存失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPrivacySettings(): Result<PrivacySettings> {
        return try {
            val response = userApi.getPrivacySettings()
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "获取失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePrivacySettings(settings: PrivacySettings): Result<PrivacySettings> {
        return try {
            val response = userApi.updatePrivacySettings(settings)
            if (response.isSuccess && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "保存失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessages(page: Int, size: Int, type: String?): Result<Pair<List<Message>, Int>> {
        return try {
            val response = userApi.getMessages(page, size, type)
            if (response.isSuccess && response.data != null) {
                Result.success(Pair(response.data.messages, response.data.unreadCount))
            } else {
                Result.failure(Exception(response.message ?: "获取失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markMessageRead(messageId: String): Result<Unit> {
        return try {
            val response = userApi.markMessageRead(messageId)
            if (response.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "操作失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAllMessagesRead(): Result<Unit> {
        return try {
            val response = userApi.markAllMessagesRead()
            if (response.isSuccess) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "操作失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
