package com.example.redlibros_albpabvic.viewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redlibros_albpabvic.model.User
import com.example.redlibros_albpabvic.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val remember: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(
            username = newUsername,
            errorMessage = null
        )
        validate()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(
            password = newPassword,
            errorMessage = null
        )
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

        val isValid = username.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(username).matches() &&
                password.length >= 8

        _uiState.value = _uiState.value.copy(
            isLoginEnabled = isValid
        )
    }

    fun registerUser(onResult: (Boolean) -> Unit) {
        val state = _uiState.value

        // Validación básica
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Email y contraseña son obligatorios"
            )
            onResult(false)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(state.username).matches()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Email no válido"
            )
            onResult(false)
            return
        }

        if (state.password.length < 8) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "La contraseña debe tener al menos 8 caracteres"
            )
            onResult(false)
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                userRepository.insertUser(
                    User(
                        username = state.username,
                        password = state.password
                    )
                )
                _uiState.value = _uiState.value.copy(isLoading = false)
                onResult(true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al registrar: ${e.message}"
                )
                onResult(false)
            }
        }
    }

    fun login(onResult: (Boolean) -> Unit) {
        val state = _uiState.value

        // Validación básica
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Email y contraseña son obligatorios"
            )
            onResult(false)
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val user = userRepository.getUserByUsername(state.username)

                if (user == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Usuario no encontrado"
                    )
                    onResult(false)
                    return@launch
                }

                val isPasswordCorrect = user.password == state.password

                if (isPasswordCorrect) {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    onResult(true)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Contraseña incorrecta"
                    )
                    onResult(false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión: ${e.message}"
                )
                onResult(false)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}