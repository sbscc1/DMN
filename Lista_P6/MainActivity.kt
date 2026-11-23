package com.example.calculadorabasica

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelo de datos
data class LenguajeItem(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String
)

@Composable
fun ListaScreen() {
    // Colección de datos dinámicos
    val listaLenguajes = remember {
        listOf(
            LenguajeItem(1, "Kotlin", "Móvil / Backend", "Lenguaje oficial y moderno para desarrollo nativo en Android."),
            LenguajeItem(2, "Python", "Backend / IA", "Muy usado en ciencia de datos, APIs con FastAPI y automatización."),
            LenguajeItem(3, "TypeScript", "Web Full-Stack", "JavaScript con tipado estático para proyectos escalables."),
            LenguajeItem(4, "C#", "Escritorio / Web", "Potente lenguaje del ecosistema .NET para aplicaciones empresariales."),
            LenguajeItem(5, "SQL", "Bases de Datos", "Lenguaje estándar para consultar y administrar bases relacionales."),
            LenguajeItem(6, "C++", "Sistemas / Embebidos", "Alto rendimiento para videojuegos, microcontroladores y sistemas.")
        )
    }

    // Alternar entre disposición lineal (Lista) y cuadrícula (Grid)
    var usarCuadricula by remember { mutableStateOf(false) }

    // Elemento seleccionado al hacer clic (para mostrar detalles)
    var itemSeleccionado by remember { mutableStateOf<LenguajeItem?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            // Encabezado y botón para cambiar de Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Catálogo de Lenguajes",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                FilledTonalButton(onClick = { usarCuadricula = !usarCuadricula }) {
                    Text(if (usarCuadricula) "Ver Lista" else "Ver Grid")
                }
            }

            // Equivalente a RecyclerView + LayoutManager (Linear o Grid)
            if (usarCuadricula) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaLenguajes, key = { it.id }) { item ->
                        LenguajeCard(
                            item = item,
                            onClick = { itemSeleccionado = item }
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaLenguajes, key = { it.id }) { item ->
                        LenguajeCard(
                            item = item,
                            onClick = { itemSeleccionado = item }
                        )
                    }
                }
            }
        }

        // Diálogo de detalles al capturar el evento de clic
        itemSeleccionado?.let { item ->
            AlertDialog(
                onDismissRequest = { itemSeleccionado = null },
                title = { Text(text = item.nombre, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Categoría: ${item.categoria}",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = item.descripcion)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { itemSeleccionado = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

// 1. Diseño del ítem individual con Material Design (Card)
@Composable
fun LenguajeCard(
    item: LenguajeItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.categoria,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Toca para ver detalles",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}