package com.example.redlibros_albpabvic.view

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.redlibros_albpabvic.model.UserSession
import com.example.redlibros_albpabvic.viewModel.BibliotecaFiltro
import com.example.redlibros_albpabvic.viewModel.BibliotecaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaScreen(
    viewModel: BibliotecaViewModel,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    BackHandler { showExitDialog = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            when (uiState.filtro) {
                                BibliotecaFiltro.MIS_LIBROS -> "Mis Préstamos"
                                else -> if (uiState.nombreAlumno.isNotEmpty()) {
                                    "Biblioteca de ${uiState.nombreAlumno}"
                                } else {
                                    "Biblioteca"
                                }
                            },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            when (uiState.filtro) {
                                BibliotecaFiltro.MIS_LIBROS -> "${uiState.misLibros.size} libros"
                                else -> "${uiState.libros.size} libros disponibles"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Salir",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = uiState.filtro == BibliotecaFiltro.TODOS,
                    onClick = { viewModel.setFiltro(BibliotecaFiltro.TODOS) },
                    icon = {
                        Icon(
                            Icons.Default.Book,
                            contentDescription = "Todos"
                        )
                    },
                    label = { Text("Todos", fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = uiState.filtro == BibliotecaFiltro.AUTOR,
                    onClick = { viewModel.setFiltro(BibliotecaFiltro.AUTOR) },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Autor"
                        )
                    },
                    label = { Text("Por Autor", fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = uiState.filtro == BibliotecaFiltro.MIS_LIBROS,
                    onClick = { viewModel.setFiltro(BibliotecaFiltro.MIS_LIBROS) },
                    icon = {
                        Icon(
                            Icons.Default.AutoStories,
                            contentDescription = "Mis Libros"
                        )
                    },
                    label = { Text("Mis Libros", fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Pestaña MIS LIBROS
            if (uiState.filtro == BibliotecaFiltro.MIS_LIBROS) {
                if (uiState.misLibros.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.LibraryBooks,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "No tienes libros prestados",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.misLibros) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (item.fechaDevolucion == null)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                border = BorderStroke(1.dp, Color.Black)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            item.libro.nombre,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        if (item.libro.autor != null) {
                                            Text(
                                                "Autor: ${item.libro.autor}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.CalendarToday,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                "Préstamo: ${item.fechaPrestamo.take(10)}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                        if (item.fechaDevolucion != null) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.tertiary
                                                )
                                                Text(
                                                    "Devuelto: ${item.fechaDevolucion}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.tertiary
                                                )
                                            }
                                        } else {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    Icons.Default.Warning,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                                Text(
                                                    "Pendiente de devolución",
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                    ),
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }

                                    if (item.fechaDevolucion == null) {
                                        FilledTonalButton(
                                            onClick = {
                                                viewModel.devolverLibro(item.idtransaccion) { success, msg ->
                                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.AssignmentReturn,
                                                contentDescription = "Devolver",
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Devolver")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Filtro de autor
                if (uiState.filtro == BibliotecaFiltro.AUTOR) {
                    OutlinedTextField(
                        value = uiState.autorFiltro,
                        onValueChange = { viewModel.setAutorFiltro(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Buscar por autor") },
                        placeholder = { Text("Escribe el nombre del autor...") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Lista de libros
                if (uiState.libros.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Text(
                                text = if (uiState.filtro == BibliotecaFiltro.AUTOR && uiState.autorFiltro.isNotBlank()) {
                                    "No se encontraron libros de \"${uiState.autorFiltro}\""
                                } else {
                                    "No hay libros disponibles"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.libros) { libro ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                border = BorderStroke(1.dp, Color.Black)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.MenuBook,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            libro.nombre,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        )
                                    }

                                    if (libro.autor != null) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Person,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = MaterialTheme.colorScheme.secondary
                                            )
                                            Text(
                                                libro.autor,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    if (libro.isbn != null) {
                                        Text(
                                            "ISBN: ${libro.isbn}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    if (libro.editorial != null) {
                                        Text(
                                            "Editorial: ${libro.editorial}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Diálogo cerrar sesión
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                icon = {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                title = { Text("Cerrar sesión", fontWeight = FontWeight.Bold) },
                text = { Text("¿Estás seguro de que quieres cerrar sesión?") },
                confirmButton = {
                    Button(
                        onClick = {
                            UserSession.clear()
                            showLogoutDialog = false
                            onLogout()
                        }
                    ) {
                        Text("Sí, salir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancelar")
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }

        // Diálogo salir
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                icon = {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Salir de la aplicación", fontWeight = FontWeight.Bold) },
                text = { Text("¿Estás seguro de que quieres salir?") },
                confirmButton = {
                    Button(
                        onClick = { (context as? Activity)?.finish() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Sí, salir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Cancelar")
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}