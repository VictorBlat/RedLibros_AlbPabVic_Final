package com.example.redlibros_albpabvic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redlibros_albpabvic.model.UserRepository
import com.example.redlibros_albpabvic.model.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
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
        _uiState.value = _uiState.value.copy(username = newUsername, errorMessage = null)
        validate()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword, errorMessage = null)
        validate()
    }

    private fun validate() {
        val isValid = _uiState.value.username.isNotBlank() && _uiState.value.password.isNotBlank()
        _uiState.value = _uiState.value.copy(isLoginEnabled = isValid)
    }

    fun login(onResult: (Boolean) -> Unit) {
        val state = _uiState.value

        if (state.username.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Por favor, introduce tu email")
            onResult(false)
            return
        }

        if (state.password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Por favor, introduce tu contraseña")
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
                        errorMessage = "Usuario no encontrado. Verifica tu email."
                    )
                    onResult(false)
                    return@launch
                }

                if (user.password != state.password) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Contraseña incorrecta. Inténtalo de nuevo."
                    )
                    onResult(false)
                    return@launch
                }

                // Usuario y contraseña correctos
                UserSession.iduser = user.iduser
                UserSession.username = user.username

                // Verificar si es alumno
                val alumno = userRepository.getAlumnoByUserId(user.iduser)

                if (alumno == null) {
                    // Es profesor - BLOQUEAR
                    UserSession.clear()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Esta aplicación es solo para alumnos.\nPor favor, usa la aplicación de escritorio."
                    )
                    onResult(false)
                    return@launch
                }

                // Es alumno - PERMITIR
                UserSession.idalumno = alumno.idalumno
                UserSession.isProfesor = false

                _uiState.value = _uiState.value.copy(isLoading = false)
                onResult(true)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión: ${e.message}\nVerifica tu conexión a internet."
                )
                onResult(false)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}