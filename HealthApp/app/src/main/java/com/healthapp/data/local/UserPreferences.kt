package com.healthapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.healthapp.domain.model.User
import com.healthapp.domain.model.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_PHONE = stringPreferencesKey("user_phone")
        val USER_ROLE = stringPreferencesKey("user_role")
        val USER_AVATAR = stringPreferencesKey("user_avatar")
        val USER_GENDER = stringPreferencesKey("user_gender")
        val USER_AGE = intPreferencesKey("user_age")
        val TOKEN = stringPreferencesKey("token")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.IS_LOGGED_IN] ?: false
    }

    val currentUser: Flow<User?> = context.dataStore.data.map { preferences ->
        val isLoggedIn = preferences[Keys.IS_LOGGED_IN] ?: false
        if (!isLoggedIn) return@map null

        val userId = preferences[Keys.USER_ID] ?: return@map null
        val name = preferences[Keys.USER_NAME] ?: return@map null
        val phone = preferences[Keys.USER_PHONE] ?: return@map null
        val role = preferences[Keys.USER_ROLE] ?: return@map null

        User(
            userId = userId,
            name = name,
            phone = phone,
            role = UserRole.fromString(role),
            avatar = preferences[Keys.USER_AVATAR],
            gender = preferences[Keys.USER_GENDER],
            age = preferences[Keys.USER_AGE],
            organizationId = null,
            organizationName = null
        )
    }

    suspend fun saveUser(user: User, token: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_LOGGED_IN] = true
            preferences[Keys.USER_ID] = user.userId
            preferences[Keys.USER_NAME] = user.name
            preferences[Keys.USER_PHONE] = user.phone
            preferences[Keys.USER_ROLE] = user.role.toApiString()
            preferences[Keys.TOKEN] = token
            user.avatar?.let { preferences[Keys.USER_AVATAR] = it }
            user.gender?.let { preferences[Keys.USER_GENDER] = it }
            user.age?.let { preferences[Keys.USER_AGE] = it }
        }
    }

    suspend fun getUser(): User? {
        return currentUser.first()
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.first()[Keys.TOKEN]
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
