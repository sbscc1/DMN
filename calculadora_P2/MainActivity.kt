package com.example.calculadorabasica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorScreen()
        }
    }
}

val DarkBackground = Color(0xFF000000)
val ButtonGray = Color(0xFF1E1F22)
val OperatorCyan = Color(0xFF75B1C6)
val EqualsBackground = Color(0xFF3B6774)
val TextWhite = Color(0xFFE2E2E2)
val TextRed = Color(0xFFD94A4A)

@Composable
fun CalculatorScreen() {
    var displayText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val operators = setOf("+", "-", "×", "÷")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Pantalla de resultados
        Text(
            text = errorMessage ?: displayText.ifEmpty { "0" },
            fontSize = if (errorMessage != null) 36.sp else 56.sp,
            color = if (errorMessage != null) TextRed else TextWhite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.End,
            maxLines = 2,
            lineHeight = 62.sp
        )

        HorizontalDivider(
            color = Color.DarkGray,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Rejilla de botones (usando códigos Unicode seguros para ÷ y ×)
        val buttons = listOf(
            listOf("C" to TextRed, "()" to OperatorCyan, "%" to OperatorCyan, "÷" to OperatorCyan),
            listOf("7" to TextWhite, "8" to TextWhite, "9" to TextWhite, "×" to OperatorCyan),
            listOf("4" to TextWhite, "5" to TextWhite, "6" to TextWhite, "-" to OperatorCyan),
            listOf("1" to TextWhite, "2" to TextWhite, "3" to TextWhite, "+" to OperatorCyan),
            listOf("+/-" to TextWhite, "0" to TextWhite, "." to TextWhite, "=" to TextWhite)
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { (label, color) ->
                    val isEquals = label == "="
                    CalculatorButton(
                        symbol = label,
                        textColor = color,
                        backgroundColor = if (isEquals) EqualsBackground else ButtonGray,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = {
                            if (errorMessage != null) {
                                displayText = ""
                                errorMessage = null
                            }

                            when (label) {
                                "C" -> {
                                    displayText = ""
                                    errorMessage = null
                                }
                                "=" -> {
                                    if (displayText.isBlank()) return@CalculatorButton
                                    try {
                                        val result = evaluateExpression(displayText)
                                        if (result.isInfinite() || result.isNaN()) {
                                            errorMessage = "No se puede dividir entre 0"
                                        } else {
                                            val symbols = DecimalFormatSymbols(Locale.US)
                                            val format = DecimalFormat("0.######", symbols)
                                            displayText = format.format(result)
                                        }
                                    } catch (e: ArithmeticException) {
                                        errorMessage = e.message ?: "Error aritmético"
                                    } catch (e: Exception) {
                                        errorMessage = "Formato inválido"
                                    }
                                }
                                "()" -> {
                                    val openCount = displayText.count { it == '(' }
                                    val closeCount = displayText.count { it == ')' }
                                    val lastChar = displayText.lastOrNull()
                                    displayText += if (
                                        openCount == closeCount ||
                                        lastChar == null ||
                                        lastChar == '(' ||
                                        lastChar.toString() in operators
                                    ) "(" else ")"
                                }
                                "+/-" -> {
                                    if (displayText.isNotEmpty() && displayText != "0") {
                                        displayText = if (displayText.startsWith("-")) {
                                            displayText.substring(1)
                                        } else {
                                            "-$displayText"
                                        }
                                    }
                                }
                                "." -> {
                                    // Evita múltiples puntos en el mismo número (ej. 5.2.3)
                                    val lastNumber = displayText.takeLastWhile { it.isDigit() || it == '.' }
                                    if (!lastNumber.contains('.')) {
                                        displayText += if (lastNumber.isEmpty()) "0." else "."
                                    }
                                }
                                in operators -> {
                                    // Evita empezar con *, / o + y reemplaza operador si ya había uno al final
                                    if (displayText.isEmpty()) {
                                        if (label == "-") displayText = "-"
                                    } else if (displayText.last().toString() in operators) {
                                        displayText = displayText.dropLast(1) + label
                                    } else {
                                        displayText += label
                                    }
                                }
                                else -> displayText += label
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    symbol: String,
    textColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = modifier,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = symbol,
            color = textColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

fun evaluateExpression(expression: String): Double {
    val sanitizedExpr = expression
        .replace("×", "*")
        .replace("÷", "/")
        .replace("%", "/100")
        .trim()

    if (sanitizedExpr.isEmpty()) return 0.0

    return object : Any() {
        var pos = -1
        var ch = 0

        fun nextChar() {
            ch = if (++pos < sanitizedExpr.length) sanitizedExpr[pos].code else -1
        }

        fun eat(charToEat: Int): Boolean {
            while (ch == ' '.code) nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < sanitizedExpr.length) {
                throw IllegalArgumentException("Carácter inesperado: ${ch.toChar()}")
            }
            return x
        }

        fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                when {
                    eat('+'.code) -> x += parseTerm()
                    eat('-'.code) -> x -= parseTerm()
                    else -> return x
                }
            }
        }

        fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                when {
                    eat('*'.code) -> x *= parseFactor()
                    eat('/'.code) -> {
                        val divisor = parseFactor()
                        if (divisor == 0.0) {
                            throw ArithmeticException("No se puede dividir entre 0")
                        }
                        x /= divisor
                    }
                    else -> return x
                }
            }
        }

        fun parseFactor(): Double {
            if (eat('+'.code)) return parseFactor()
            if (eat('-'.code)) return -parseFactor()

            val x: Double
            val startPos = this.pos
            if (eat('('.code)) {
                x = parseExpression()
                if (!eat(')'.code)) {
                    throw IllegalArgumentException("Falta cerrar paréntesis")
                }
            } else if ((ch in '0'.code..'9'.code) || ch == '.'.code) {
                while ((ch in '0'.code..'9'.code) || ch == '.'.code) nextChar()
                val numStr = sanitizedExpr.substring(startPos, this.pos)
                x = numStr.toDoubleOrNull()
                    ?: throw IllegalArgumentException("Número inválido: $numStr")
            } else {
                throw IllegalArgumentException("Expresión incompleta")
            }
            return x
        }
    }.parse()
}