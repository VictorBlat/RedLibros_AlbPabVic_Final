package com.example.redlibros_albpabvic.viewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redlibros_albpabvic.model.OfflineUserRepository
import com.example.redlibros_albpabvic.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val remember: Boolean = false,
    val isLoginEnabled: Boolean = false
)

class LoginViewModel(
    private val userRepository: OfflineUserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername)
        validate()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
        validate()
    }

    fun toggleRemember() {
        _uiState.value = _uiState.value.copy(
            remember = !_uiState.value.remember
        )
    }

    private fun validate() {
        val username = _uiState.value.username
        val password = _uiState.value.password
        _uiState.value = _uiState.value.copy(
            isLoginEnabled =
                username.isNotBlank() &&
                        Patterns.EMAIL_ADDRESS.matcher(username).matches() &&
                        password.length > 7
        )
    }

    fun registerUser(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            userRepository.insertUser(
                User(
                    username = state.username,
                    password = state.password
                )
            )
            onResult(true)
        }
    }

    fun login(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            val user = userRepository.getUserByUsername(state.username)
            val ok = user?.password == state.password
            onResult(ok)
        }
    }
}
