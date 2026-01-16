package com.example.redlibros_albpabvic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redlibros_albpabvic.model.Libro
import com.example.redlibros_albpabvic.model.LibroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class BibliotecaFiltro {
    TODOS, AUTOR
}

data class BibliotecaUiState(
    val libros: List<Libro> = emptyList(),
    val filtro: BibliotecaFiltro = BibliotecaFiltro.TODOS,
    val autorFiltro: String = ""
)

class BibliotecaViewModel(
    private val libroRepository: LibroRepository
) : ViewModel() {

    private val filtroState = MutableStateFlow(BibliotecaFiltro.TODOS)
    private val autorFiltroState = MutableStateFlow("")

    val uiState: StateFlow<BibliotecaUiState> =
        filtroState.flatMapLatest { filtro ->
            when (filtro) {
                BibliotecaFiltro.TODOS -> libroRepository.getAllLibros()
                BibliotecaFiltro.AUTOR -> libroRepository.getLibrosByAutor(autorFiltroState.value)
            }
        }.map { lista ->
            BibliotecaUiState(
                libros = lista,
                filtro = filtroState.value,
                autorFiltro = autorFiltroState.value
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BibliotecaUiState()
        )

    fun setFiltro(filtro: BibliotecaFiltro) {
        filtroState.value = filtro
    }

    fun setAutorFiltro(autor: String) {
        autorFiltroState.value = autor
    }

    fun addLibro(nombre: String, autor: String, isbn: String, editorial: String = "") {
        viewModelScope.launch {
            try {
                libroRepository.insertLibro(
                    Libro(
                        nombre = nombre,
                        autor = autor,
                        isbn = isbn,
                        editorial = editorial
                    )
                )
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun deleteLibro(libro: Libro) {
        viewModelScope.launch {
            try {
                libroRepository.deleteLibro(libro)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}