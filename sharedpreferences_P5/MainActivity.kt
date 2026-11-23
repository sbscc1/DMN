package com.example.calculadorabasica

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenciasScreen() {
    val context = LocalContext.current
    val sharedPrefs = remember {
        context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
    }

    var nombre by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    var estadoMensaje by remember { mutableStateOf("") }
    var datosGuardados by remember { mutableStateOf("Sin datos guardados") }

    // Función auxiliar para leer de SharedPreferences
    fun cargarDatos(mostrarMensaje: Boolean = false) {
        val nombreGuardado = sharedPrefs.getString("KEY_NOMBRE", "") ?: ""
        val notaGuardada = sharedPrefs.getString("KEY_NOTA", "") ?: ""

        nombre = nombreGuardado
        nota = notaGuardada

        datosGuardados = if (nombreGuardado.isNotEmpty() || notaGuardada.isNotEmpty()) {
            "Nombre: $nombreGuardado\nNota: $notaGuardada"
        } else {
            "Sin datos guardados"
        }

        if (mostrarMensaje) {
            estadoMensaje = if (nombreGuardado.isNotEmpty() || notaGuardada.isNotEmpty()) {
                "Datos cargados correctamente."
            } else {
                "No hay datos guardados para cargar."
            }
        }
    }

    // Se ejecuta automáticamente al abrir o reabrir la app
    LaunchedEffect(Unit) {
        cargarDatos(mostrarMensaje = false)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Persistencia con SharedPreferences",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                label = { Text("Nota o preferencia") },
                modifier = Modifier.fillMaxWidth()
            )

            // Botones de acción: Guardar, Cargar, Borrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        sharedPrefs.edit()
                            .putString("KEY_NOMBRE", nombre.trim())
                            .putString("KEY_NOTA", nota.trim())
                            .apply()

                        cargarDatos(mostrarMensaje = false)
                        estadoMensaje = "Datos guardados en SharedPreferences."
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }

                OutlinedButton(
                    onClick = { cargarDatos(mostrarMensaje = true) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cargar")
                }

                Button(
                    onClick = {
                        sharedPrefs.edit().clear().apply()
                        nombre = ""
                        nota = ""
                        datosGuardados = "Sin datos guardados"
                        estadoMensaje = "Datos eliminados."
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Borrar")
                }
            }

            if (estadoMensaje.isNotEmpty()) {
                Text(
                    text = estadoMensaje,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }

            HorizontalDivider()

            // Tarjeta para visualizar lo que está persistido actualmente
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Información almacenada:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(text = datosGuardados, fontSize = 16.sp, lineHeight = 22.sp)
                }
            }
        }
    }
}