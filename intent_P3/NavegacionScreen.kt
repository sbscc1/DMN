package com.example.calculadorabasica

import android.content.Intent
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
fun NavegacionScreen() {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pantalla Principal",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    error = false
                },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = mensaje,
                onValueChange = {
                    mensaje = it
                    error = false
                },
                label = { Text("Mensaje o nota") },
                modifier = Modifier.fillMaxWidth()
            )

            if (error) {
                Text(
                    text = "Llena ambos campos antes de enviar.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || mensaje.isBlank()) {
                        error = true
                    } else {
                        // Creamos el Intent y adjuntamos los extras
                        val intent = Intent(context, SegundaActivity::class.java).apply {
                            putExtra("EXTRA_NOMBRE", nombre.trim())
                            putExtra("EXTRA_MENSAJE", mensaje.trim())
                        }
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Enviar y Navegar", fontSize = 18.sp)
            }
        }
    }
}