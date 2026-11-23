package com.example.calculadorabasica

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuToolbarScreen() {
    // Estados para controlar la visibilidad de los menús
    var menuSuperiorExpandido by remember { mutableStateOf(false) }
    var popupMenuExpandido by remember { mutableStateOf(false) }

    // Estado para mostrar la última acción ejecutada
    var ultimaAccion by remember { mutableStateOf("Ninguna acción seleccionada aún") }

    // Snackbar para dar retroalimentación visual al hacer pruebas
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun registrarAccion(mensaje: String) {
        ultimaAccion = mensaje
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            // --- TOOLBAR (TopAppBar) ---
            TopAppBar(
                title = { Text("Barra y Menús") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    // Ícono de acción 1: Buscar
                    IconButton(onClick = { registrarAccion("Acción: Buscar seleccionada") }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    }

                    // Ícono de acción 2: Compartir
                    IconButton(onClick = { registrarAccion("Acción: Compartir seleccionada") }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir"
                        )
                    }

                    // Botón para desplegar el OptionsMenu (3 puntos)
                    Box {
                        IconButton(onClick = { menuSuperiorExpandido = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Más opciones"
                            )
                        }

                        // --- OPTIONS MENU (Desplegable de la ToolBar) ---
                        DropdownMenu(
                            expanded = menuSuperiorExpandido,
                            onDismissRequest = { menuSuperiorExpandido = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Actualizar") },
                                leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                                onClick = {
                                    menuSuperiorExpandido = false
                                    registrarAccion("OptionsMenu: Actualizar")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Configuración") },
                                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                onClick = {
                                    menuSuperiorExpandido = false
                                    registrarAccion("OptionsMenu: Configuración")
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Acerca de") },
                                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                                onClick = {
                                    menuSuperiorExpandido = false
                                    registrarAccion("OptionsMenu: Acerca de")
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta que muestra el estado actual
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Último evento capturado:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = ultimaAccion,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- POPUP MENU (Anclado a un botón en el cuerpo) ---
            Box {
                Button(onClick = { popupMenuExpandido = true }) {
                    Text("Abrir PopupMenu")
                }

                DropdownMenu(
                    expanded = popupMenuExpandido,
                    onDismissRequest = { popupMenuExpandido = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Opción A - Editar") },
                        onClick = {
                            popupMenuExpandido = false
                            registrarAccion("PopupMenu: Editar")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Opción B - Duplicar") },
                        onClick = {
                            popupMenuExpandido = false
                            registrarAccion("PopupMenu: Duplicar")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Opción C - Eliminar") },
                        onClick = {
                            popupMenuExpandido = false
                            registrarAccion("PopupMenu: Eliminar")
                        }
                    )
                }
            }
        }
    }
}