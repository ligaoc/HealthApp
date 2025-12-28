package com.healthapp.data.model.user

data class UpdateProfileRequest(
    val name: String? = null,
    val gender: String? = null,
    val age: Int? = null,
    val height: Int? = null,
    val weight: Float? = null,
    val avatar: String? = null
)

data class NotificationSettings(
    val alarmEnabled: Boolean = true,
    val medicationEnabled: Boolean = true,
    val healthTipsEnabled: Boolean = true,
    val systemEnabled: Boolean = true,
    val quietTimeEnabled: Boolean = false,
    val quietTimeStart: String? = "22:00",
    val quietTimeEnd: String? = "08:00"
)

data class PrivacySettings(
    val shareWithDoctor: Boolean = true,
    val shareWithFamily: Boolean = true,
    val allowDataExport: Boolean = true,
    val allowAnonymousAnalysis: Boolean = false
)

data class Message(
    val id: String,
    val type: String,
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean
)

data class MessageListResponse(
    val total: Int,
    val page: Int,
    val size: Int,
    val unreadCount: Int,
    val messages: List<Message>
)
