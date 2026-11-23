package com.example.calculadorabasica

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaludoScreen() {
    var nombre by remember { mutableStateOf("") }
    var mensajeSaludo by remember { mutableStateOf("Aquí aparecerá tu saludo") }

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
                text = "Saludo Personalizado",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo de entrada de texto (TextField)
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Escribe tu nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón (Button)
            Button(
                onClick = {
                    val nombreLimpio = nombre.trim()
                    mensajeSaludo = if (nombreLimpio.isNotEmpty()) {
                        "¡Hola, $nombreLimpio! Bienvenido a la aplicación."
                    } else {
                        "Por favor, ingresa tu nombre primero."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Saludar", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Etiqueta de texto dinámica (Text)
            Text(
                text = mensajeSaludo,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}