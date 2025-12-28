package com.healthapp.domain.model

data class User(
    val userId: String,
    val name: String,
    val phone: String,
    val role: UserRole,
    val avatar: String?,
    val gender: String?,
    val age: Int?,
    val organizationId: String?,
    val organizationName: String?
)

enum class UserRole {
    PATIENT, DOCTOR;

    companion object {
        fun fromString(value: String): UserRole {
            return when (value.lowercase()) {
                "patient" -> PATIENT
                "doctor" -> DOCTOR
                else -> PATIENT
            }
        }
    }

    fun toApiString(): String {
        return when (this) {
            PATIENT -> "patient"
            DOCTOR -> "doctor"
        }
    }
}
