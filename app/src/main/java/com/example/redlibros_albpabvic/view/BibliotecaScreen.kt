package com.example.redlibros_albpabvic.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.redlibros_albpabvic.model.Libro
import com.example.redlibros_albpabvic.viewModel.BibliotecaFiltro
import com.example.redlibros_albpabvic.viewModel.BibliotecaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaScreen(
    viewModel: BibliotecaViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Biblioteca (${uiState.libros.size})") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir libro")
            }
        },
        bottomBar = {
            BibliotecaBottomBar(
                filtro = uiState.filtro,
                onFiltroChange = { viewModel.setFiltro(it) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                if (uiState.filtro == BibliotecaFiltro.AUTOR) {
                    AutorSearchBar(
                        autor = uiState.autorFiltro,
                        onAutorChange = { viewModel.setAutorFiltro(it) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyColumn {
                    items(uiState.libros) { libro ->
                        LibroItem(
                            libro = libro,
                            onDelete = { viewModel.deleteLibro(libro) }
                        )
                    }
                }
            }

            if (showDialog) {
                AddLibroDialog(
                    onDismiss = { showDialog = false },
                    onAdd = { titulo, autor, isbn, editorial ->
                        viewModel.addLibro(titulo, autor, isbn, editorial)
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun LibroItem(
    libro: Libro,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(libro.nombre, style = MaterialTheme.typography.titleMedium)
                if (libro.autor != null) {
                    Text("Autor: ${libro.autor}")
                }
                if (libro.isbn != null) {
                    Text("ISBN: ${libro.isbn}")
                }
                if (libro.editorial != null) {
                    Text("Editorial: ${libro.editorial}")
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar libro",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun BibliotecaBottomBar(
    filtro: BibliotecaFiltro,
    onFiltroChange: (BibliotecaFiltro) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = filtro == BibliotecaFiltro.TODOS,
            onClick = { onFiltroChange(BibliotecaFiltro.TODOS) },
            icon = {},
            label = { Text("Todos") }
        )
        NavigationBarItem(
            selected = filtro == BibliotecaFiltro.AUTOR,
            onClick = { onFiltroChange(BibliotecaFiltro.AUTOR) },
            icon = {},
            label = { Text("Por Autor") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutorSearchBar(
    autor: String,
    onAutorChange: (String) -> Unit
) {
    OutlinedTextField(
        value = autor,
        onValueChange = onAutorChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Buscar por autor") },
        placeholder = { Text("Escribe el nombre del autor...") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLibroDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var editorial by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (titulo.isNotBlank()) {
                        onAdd(titulo, autor, isbn, editorial)
                    }
                },
                enabled = titulo.isNotBlank()
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Nuevo libro") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text("ISBN") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = editorial,
                    onValueChange = { editorial = it },
                    label = { Text("Editorial") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    )
}