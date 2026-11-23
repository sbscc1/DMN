package com.example.calculadorabasica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SegundaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recibimos los datos enviados desde el Intent
        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Sin nombre"
        val mensaje = intent.getStringExtra("EXTRA_MENSAJE") ?: "Sin mensaje"

        setContent {
            PantallaDestino(
                nombre = nombre,
                mensaje = mensaje,
                onVolver = { finish() } // Cierra esta Activity y regresa a la anterior
            )
        }
    }
}

@Composable
fun PantallaDestino(nombre: String, mensaje: String, onVolver: () -> Unit) {
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
                text = "Datos Recibidos",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Nombre:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = nombre, fontSize = 20.sp, modifier = Modifier.padding(bottom = 12.dp))

                    Text(text = "Mensaje:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = mensaje, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Regresar", fontSize = 18.sp)
            }
        }
    }
}