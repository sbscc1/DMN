package com.example.calculadorabasica

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// 5A. VIEWMODEL con estados reactivos (StateFlow)
class TareaViewModel(private val dao: TareaDao) : ViewModel() {

    val tareas: StateFlow<List<TareaEntity>> = dao.obtenerTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarTarea(titulo: String) {
        val textoLimpio = titulo.trim()
        if (textoLimpio.isNotEmpty()) {
            viewModelScope.launch {
                dao.insertar(TareaEntity(titulo = textoLimpio))
            }
        }
    }

    fun alternarCompletada(tarea: TareaEntity) {
        viewModelScope.launch {
            dao.actualizar(tarea.copy(completada = !tarea.completada))
        }
    }

    fun eliminarTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            dao.eliminar(tarea)
        }
    }
}

// Factory para inyectar el DAO al ViewModel
class TareaViewModelFactory(private val dao: TareaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TareaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TareaViewModel(dao) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}

// 5B. INTERFAZ EN JETPACK COMPOSE
@Composable
fun NotasRoomScreen() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val viewModel: TareaViewModel = viewModel(
        factory = TareaViewModelFactory(db.tareaDao())
    )

    val listaTareas by viewModel.tareas.collectAsState()
    var nuevaTarea by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp)
        ) {
            Text(
                text = "Gestor de Tareas (Room)",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo de entrada + Botón Insertar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nuevaTarea,
                    onValueChange = { nuevaTarea = it },
                    label = { Text("Nueva tarea") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        viewModel.agregarTarea(nuevaTarea)
                        nuevaTarea = ""
                    }
                ) {
                    Text("Agregar")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))

            // Lista reactiva en tiempo real
            if (listaTareas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay tareas guardadas en SQLite.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaTareas, key = { it.id }) { tarea ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Update (marcar como completada)
                                Checkbox(
                                    checked = tarea.completada,
                                    onCheckedChange = { viewModel.alternarCompletada(tarea) }
                                )
                                Text(
                                    text = tarea.titulo,
                                    fontSize = 16.sp,
                                    textDecoration = if (tarea.completada) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp)
                                )
                                // Delete
                                TextButton(
                                    onClick = { viewModel.eliminarTarea(tarea) },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}