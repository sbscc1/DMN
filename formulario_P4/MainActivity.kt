package com.example.calculadorabasica

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen() {
    // 1. Estado para Spinner (Dropdown)
    val opcionesCurso = listOf("Desarrollo Móvil", "Desarrollo Web", "Base de Datos", "Inteligencia Artificial")
    var menuExpandido by remember { mutableStateOf(false) }
    var cursoSeleccionado by remember { mutableStateOf(opcionesCurso[0]) }

    // 2. Estado para RadioGroup (opciones exclusivas)
    val modalidades = listOf("Presencial", "En línea", "Híbrida")
    var modalidadSeleccionada by remember { mutableStateOf(modalidades[0]) }

    // 3. Estado para CheckBoxes (selección múltiple)
    var checkKotlin by remember { mutableStateOf(false) }
    var checkPython by remember { mutableStateOf(false) }
    var checkSQL by remember { mutableStateOf(false) }

    // 4. Estado para Switch
    var recibirNotificaciones by remember { mutableStateOf(true) }

    // 5. Estado para SeekBar (Slider)
    var nivelExperiencia by remember { mutableFloatStateOf(5f) }

    // Estado para el resumen final
    var resumen by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registro de Curso",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            // --- SPINNER (ExposedDropdownMenuBox) ---
            Text(text = "Área de interés:", fontWeight = FontWeight.SemiBold)
            ExposedDropdownMenuBox(
                expanded = menuExpandido,
                onExpandedChange = { menuExpandido = !menuExpandido }
            ) {
                OutlinedTextField(
                    value = cursoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false }
                ) {
                    opcionesCurso.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                cursoSeleccionado = opcion
                                menuExpandido = false
                            }
                        )
                    }
                }
            }

            HorizontalDivider()

            // --- RADIO GROUP ---
            Text(text = "Modalidad preferida:", fontWeight = FontWeight.SemiBold)
            Column {
                modalidades.forEach { opcion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (opcion == modalidadSeleccionada),
                                onClick = { modalidadSeleccionada = opcion },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (opcion == modalidadSeleccionada),
                            onClick = null // El click lo maneja el Row completo
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = opcion)
                    }
                }
            }

            HorizontalDivider()

            // --- CHECKBOXES ---
            Text(text = "Tecnologías que conoces:", fontWeight = FontWeight.SemiBold)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checkKotlin, onCheckedChange = { checkKotlin = it })
                    Text("Kotlin")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checkPython, onCheckedChange = { checkPython = it })
                    Text("Python")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checkSQL, onCheckedChange = { checkSQL = it })
                    Text("SQL")
                }
            }

            HorizontalDivider()

            // --- SWITCH ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Recibir notificaciones por correo", fontWeight = FontWeight.SemiBold)
                Switch(
                    checked = recibirNotificaciones,
                    onCheckedChange = { recibirNotificaciones = it }
                )
            }

            HorizontalDivider()

            // --- SEEKBAR (Slider) ---
            Text(
                text = "Nivel de experiencia: ${nivelExperiencia.roundToInt()} / 10",
                fontWeight = FontWeight.SemiBold
            )
            Slider(
                value = nivelExperiencia,
                onValueChange = { nivelExperiencia = it },
                valueRange = 1f..10f,
                steps = 8 // Permite pasos exactos del 1 al 10
            )

            // --- BOTÓN DE PROCESAMIENTO ---
            Button(
                onClick = {
                    val tecnologias = buildList {
                        if (checkKotlin) add("Kotlin")
                        if (checkPython) add("Python")
                        if (checkSQL) add("SQL")
                    }.ifEmpty { listOf("Ninguna") }.joinToString(", ")

                    resumen = """
                        • Curso: $cursoSeleccionado
                        • Modalidad: $modalidadSeleccionada
                        • Tecnologías: $tecnologias
                        • Notificaciones: ${if (recibirNotificaciones) "Activadas" else "Desactivadas"}
                        • Experiencia: ${nivelExperiencia.roundToInt()}/10
                    """.trimIndent()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Procesar Formulario", fontSize = 18.sp)
            }

            // --- CONTENEDOR DE RESUMEN ---
            if (resumen.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Resumen de Selección",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(text = resumen, fontSize = 16.sp, lineHeight = 24.sp)
                    }
                }
            }
        }
    }
}