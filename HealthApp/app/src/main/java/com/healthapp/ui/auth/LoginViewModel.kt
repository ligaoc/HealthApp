package com.healthapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.domain.model.User
import com.healthapp.domain.model.UserRole
import com.healthapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val phone: String = "",
    val password: String = "",
    val selectedRole: UserRole = UserRole.PATIENT,
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val user: User? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updatePhone(phone: String) {
        _uiState.update { it.copy(phone = phone, error = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun selectRole(role: UserRole) {
        _uiState.update { it.copy(selectedRole = role, error = null) }
    }

    fun login() {
        val state = _uiState.value

        // 验证输入
        if (state.phone.isBlank()) {
            _uiState.update { it.copy(error = "请输入手机号") }
            return
        }
        if (state.phone.length != 11) {
            _uiState.update { it.copy(error = "请输入正确的手机号") }
            return
        }
        if (state.password.isBlank()) {
            _uiState.update { it.copy(error = "请输入密码") }
            return
        }
        if (state.password.length < 6) {
            _uiState.update { it.copy(error = "密码至少6位") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.login(
                phone = state.phone,
                password = state.password,
                role = state.selectedRole
            )

            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true,
                            user = user
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "登录失败"
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetLoginState() {
        _uiState.update { it.copy(loginSuccess = false) }
    }
}
