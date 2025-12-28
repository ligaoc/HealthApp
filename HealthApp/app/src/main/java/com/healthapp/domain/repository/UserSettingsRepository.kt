package com.healthapp.domain.repository

import com.healthapp.data.model.auth.UserProfileResponse
import com.healthapp.data.model.user.Message
import com.healthapp.data.model.user.NotificationSettings
import com.healthapp.data.model.user.PrivacySettings
import com.healthapp.data.model.user.UpdateProfileRequest

interface UserSettingsRepository {
    suspend fun updateProfile(request: UpdateProfileRequest): Result<UserProfileResponse>
    suspend fun getNotificationSettings(): Result<NotificationSettings>
    suspend fun updateNotificationSettings(settings: NotificationSettings): Result<NotificationSettings>
    suspend fun getPrivacySettings(): Result<PrivacySettings>
    suspend fun updatePrivacySettings(settings: PrivacySettings): Result<PrivacySettings>
    suspend fun getMessages(page: Int, size: Int, type: String?): Result<Pair<List<Message>, Int>>
    suspend fun markMessageRead(messageId: String): Result<Unit>
    suspend fun markAllMessagesRead(): Result<Unit>
}
