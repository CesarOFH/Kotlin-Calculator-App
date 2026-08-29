package com.github.cesarofhdev.calculatorkotlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val _display = MutableLiveData("")
    val display: LiveData<String> = _display
    private val _result = MutableLiveData("")
    val result: LiveData<String> = _result
    private var total = 0.0
    private var currentNumber = ""
    // Indica que acabamos de presionar =
    private var justCalculated = false

    // NÚMEROS Y PUNTO
    fun number(value: String) {
        // PUNTO
        if (value == ".") {
            // Si acabamos de calcular:
            // 5 = → .
            // comenzamos un número nuevo: 0.
            if (justCalculated) {
                currentNumber = "0."
                total = 0.0
                _display.value = "0."
                _result.value = "0."
                justCalculated = false

                return
            }

            // Si ya hay un punto en el número actual,
            // no hacemos nada
            if (currentNumber.contains(".")) {
                return
            }

            // Si estamos empezando un número:
            // . → 0.
            if (currentNumber.isEmpty()) {
                currentNumber = "0."
                _display.value = (_display.value ?: "") + "0."
                updatePreview()

                return
            }

            // Si ya existe un número:
            // 5 → 5.
            currentNumber += "."
            _display.value = (_display.value ?: "") + "."

            return
        }

        // NÚMEROS
        // Si acabamos de calcular y escribimos
        // un número, empezamos una operación nueva
        if (justCalculated) {
            currentNumber = value
            total = 0.0
            _display.value = value
            _result.value = value
            justCalculated = false

            return
        }

        currentNumber += value
        _display.value = (_display.value ?: "") + value
        updatePreview()
    }

    // SUMA
    fun setOperator(operator: String) {

        if (operator != "+") return

        // Si acabamos de calcular:
        // 3 = → 3
        // +  → 3 +
        if (justCalculated) {

            total = (_result.value ?: "0").toDouble()
            currentNumber = ""

            _display.value = "${formatResult(total)} + "

            justCalculated = false

            return
        }

        // Si no hay número actual, no hacemos nada
        if (currentNumber.isEmpty()) return

        // Evitar convertir "0." directamente, a Double
        // porque todavía está incompleto
        if (currentNumber == "0.") {
            currentNumber = "0"
        }

        total += currentNumber.toDouble()
        currentNumber = ""

        _display.value = "${_display.value} + "

        _result.value = formatResult(total)
    }

    // Resultado
    fun calculate() {

        // Si el número termina en punto:
        // 5. → 5
        if (currentNumber == "0.") {
            currentNumber = "0"
        }

        if (currentNumber.isNotEmpty()) {
            total += currentNumber.toDouble()
        }

        val finalResult = formatResult(total)

        // El resultado se coloca en Camp_Ing
        _display.value = finalResult

        // Y también en Camp_result
        _result.value = finalResult

        // Marcamos que acabamos de calcular
        justCalculated = true

        currentNumber = ""
        total = 0.0
    }

    // Resultado EN TIEMPO REAL
    private fun updatePreview() {
        if (currentNumber.isEmpty()) return
        // "0." todavía no es un número completo,
        // así que no intentamos convertirlo
        if (currentNumber == "0.") {
            _result.value = "0"
            return
        }

        val current = currentNumber.toDouble()
        if (total != 0.0) {
            _result.value = formatResult(total + current)
        } else {
            _result.value = formatResult(current)
        }
    }

    // FORMATO
    private fun formatResult(value: Double): String {

        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }

    // LIMPIAR
    fun clear() {

        total = 0.0
        currentNumber = ""

        justCalculated = false

        _display.value = ""
        _result.value = ""
    }

    // =========================
// DELETE
// =========================

    fun delete() {

        // Sí acabamos de calcular,
        // borrar el resultado completo
        if (justCalculated) {

            currentNumber = ""
            total = 0.0
            justCalculated = false

            _display.value = ""
            _result.value = ""

            return
        }

        val texto = _display.value ?: ""

        // =========================
        // BORRAR NÚMERO ACTUAL
        // =========================

        if (currentNumber.isNotEmpty()) {

            currentNumber = currentNumber.dropLast(1)

            _display.value = texto.dropLast(1)

            if (currentNumber.isNotEmpty()) {

                updatePreview()

            } else {

                _result.value = if (total != 0.0) {
                    formatResult(total)
                } else {
                    ""
                }
            }

            return
        }

        // =========================
        // BORRAR OPERADOR +
        // =========================

        if (texto.endsWith(" + ")) {

            // Quitar " + "
            val nuevoTexto = texto.dropLast(3)

            _display.value = nuevoTexto

            // Recuperar el número que estaba antes del +
            currentNumber = nuevoTexto

            // IMPORTANTE:
            // ahora ese número vuelve a ser el número actual,
            // por lo que total debe volver a 0
            total = 0.0

            // Mostrar el número recuperado
            _result.value = if (currentNumber.isNotEmpty()) {
                formatResult(currentNumber.toDouble())
            } else {
                ""
            }

            return
        }
    }
}