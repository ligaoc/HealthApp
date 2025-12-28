package com.healthapp.data.api

import com.healthapp.data.model.ApiResponse
import com.healthapp.data.model.auth.UserProfileResponse
import com.healthapp.data.model.user.MessageListResponse
import com.healthapp.data.model.user.NotificationSettings
import com.healthapp.data.model.user.PrivacySettings
import com.healthapp.data.model.user.UpdateProfileRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApi {
    @PUT("api/user/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): ApiResponse<UserProfileResponse>

    @GET("api/user/notifications")
    suspend fun getNotificationSettings(): ApiResponse<NotificationSettings>

    @PUT("api/user/notifications")
    suspend fun updateNotificationSettings(@Body settings: NotificationSettings): ApiResponse<NotificationSettings>

    @GET("api/user/privacy")
    suspend fun getPrivacySettings(): ApiResponse<PrivacySettings>

    @PUT("api/user/privacy")
    suspend fun updatePrivacySettings(@Body settings: PrivacySettings): ApiResponse<PrivacySettings>

    @GET("api/user/messages")
    suspend fun getMessages(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("type") type: String? = null
    ): ApiResponse<MessageListResponse>

    @PUT("api/user/messages/{messageId}/read")
    suspend fun markMessageRead(@retrofit2.http.Path("messageId") messageId: String): ApiResponse<Unit>

    @PUT("api/user/messages/read-all")
    suspend fun markAllMessagesRead(): ApiResponse<Unit>
}
