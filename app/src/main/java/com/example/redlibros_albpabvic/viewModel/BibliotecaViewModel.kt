package com.example.redlibros_albpabvic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redlibros_albpabvic.model.LibroRepository
import com.example.redlibros_albpabvic.model.Libro
import com.example.redlibros_albpabvic.model.TransaccionConLibro
import com.example.redlibros_albpabvic.model.UserRepository
import com.example.redlibros_albpabvic.model.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class BibliotecaFiltro { TODOS, AUTOR, MIS_LIBROS }

data class BibliotecaUiState(
    val libros: List<Libro> = emptyList(),
    val misLibros: List<TransaccionConLibro> = emptyList(),
    val filtro: BibliotecaFiltro = BibliotecaFiltro.TODOS,
    val autorFiltro: String = "",
    val nombreAlumno: String = "",
    val isLoading: Boolean = true
)

class BibliotecaViewModel(
    private val libroRepository: LibroRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BibliotecaUiState())
    val uiState: StateFlow<BibliotecaUiState> = _uiState

    private var idsLibrosDelAlumno: List<Int> = emptyList()

    init {
        loadDatosAlumno()
    }

    private fun loadDatosAlumno() {
        viewModelScope.launch {
            try {
                val idalumno = UserSession.idalumno ?: return@launch

                // Cargar nombre del alumno
                val alumno = userRepository.getAlumnoById(idalumno)
                _uiState.value = _uiState.value.copy(nombreAlumno = alumno?.nombre ?: "")

                // Cargar cursos del alumno
                val cursos = userRepository.getCursosDelAlumno(idalumno)

                // Cargar libros de esos cursos
                val librosDelAlumno = userRepository.getLibrosPorCursos(cursos)
                idsLibrosDelAlumno = librosDelAlumno.map { it.idlibro }

                // Iniciar carga de libros
                startLoadingLibros()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    nombreAlumno = "",
                    isLoading = false
                )
            }
        }
    }

    private fun startLoadingLibros() {
        viewModelScope.launch {
            when (_uiState.value.filtro) {
                BibliotecaFiltro.TODOS -> {
                    libroRepository.getLibrosPorIds(idsLibrosDelAlumno).collectLatest { libros ->
                        _uiState.value = _uiState.value.copy(
                            libros = libros,
                            isLoading = false
                        )
                    }
                }
                BibliotecaFiltro.AUTOR -> {
                    libroRepository.getLibrosByAutor(
                        _uiState.value.autorFiltro,
                        idsLibrosDelAlumno
                    ).collectLatest { libros ->
                        _uiState.value = _uiState.value.copy(
                            libros = libros,
                            isLoading = false
                        )
                    }
                }
                BibliotecaFiltro.MIS_LIBROS -> {
                    _uiState.value = _uiState.value.copy(
                        libros = emptyList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun setFiltro(filtro: BibliotecaFiltro) {
        _uiState.value = _uiState.value.copy(filtro = filtro)

        if (filtro == BibliotecaFiltro.TODOS) {
            _uiState.value = _uiState.value.copy(autorFiltro = "")
        }

        if (filtro == BibliotecaFiltro.MIS_LIBROS) {
            loadMisLibros()
        }

        startLoadingLibros()
    }

    fun setAutorFiltro(autor: String) {
        _uiState.value = _uiState.value.copy(autorFiltro = autor)
        startLoadingLibros()
    }

    fun devolverLibro(idtransaccion: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.marcarDevolucion(idtransaccion)
                onResult(true, "Libro marcado como devuelto")
                loadMisLibros()
            } catch (e: Exception) {
                onResult(false, "Error al devolver: ${e.message}")
            }
        }
    }

    private fun loadMisLibros() {
        val idalumno = UserSession.idalumno ?: return

        viewModelScope.launch {
            try {
                val libros = userRepository.getTransaccionesByAlumno(idalumno)
                _uiState.value = _uiState.value.copy(misLibros = libros)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(misLibros = emptyList())
            }
        }
    }
}