package com.healthapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.model.user.Message
import com.healthapp.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MessageListUiState {
    object Loading : MessageListUiState()
    data class Success(val messages: List<Message>, val unreadCount: Int) : MessageListUiState()
    data class Error(val message: String) : MessageListUiState()
}

@HiltViewModel
class MessageListViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MessageListUiState>(MessageListUiState.Loading)
    val uiState: StateFlow<MessageListUiState> = _uiState

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _uiState.value = MessageListUiState.Loading
            userSettingsRepository.getMessages(1, 20, null).fold(
                onSuccess = { (messages, unreadCount) ->
                    _uiState.value = MessageListUiState.Success(messages, unreadCount)
                },
                onFailure = { e ->
                    _uiState.value = MessageListUiState.Error(e.message ?: "加载失败")
                }
            )
        }
    }

    fun markRead(messageId: String) {
        viewModelScope.launch {
            userSettingsRepository.markMessageRead(messageId)
            loadMessages()
        }
    }

    fun markAllRead() {
        viewModelScope.launch {
            userSettingsRepository.markAllMessagesRead()
            loadMessages()
        }
    }
}
