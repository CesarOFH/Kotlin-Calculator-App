package com.github.cesarofhdev.calculatorkotlin

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.cesarofhdev.calculatorkotlin.ui.CalculatorViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var screen: TextView
    private lateinit var resultScreen: TextView

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        screen = findViewById(R.id.Camp_Ing)
        resultScreen = findViewById(R.id.Camp_result)

        observeViewModel()
        setupButtons()
    }

    private fun observeViewModel() {

        viewModel.display.observe(this) { value ->
            screen.text = value
        }

        viewModel.result.observe(this) { value ->
            resultScreen.text = value
        }
    }

    private fun setupButtons() {

        // Números
        findViewById<Button>(R.id.btn0).setOnClickListener {
            viewModel.number("0")
        }

        findViewById<Button>(R.id.btn1).setOnClickListener {
            viewModel.number("1")
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            viewModel.number("2")
        }

        findViewById<Button>(R.id.btn3).setOnClickListener {
            viewModel.number("3")
        }

        findViewById<Button>(R.id.btn4).setOnClickListener {
            viewModel.number("4")
        }

        findViewById<Button>(R.id.btn5).setOnClickListener {
            viewModel.number("5")
        }

        findViewById<Button>(R.id.btn6).setOnClickListener {
            viewModel.number("6")
        }

        findViewById<Button>(R.id.btn7).setOnClickListener {
            viewModel.number("7")
        }

        findViewById<Button>(R.id.btn8).setOnClickListener {
            viewModel.number("8")
        }

        findViewById<Button>(R.id.btn9).setOnClickListener {
            viewModel.number("9")
        }

        // Punto
        findViewById<Button>(R.id.btnPoint).setOnClickListener {
            viewModel.number(".")
        }

        // Suma
        findViewById<Button>(R.id.btnPlus).setOnClickListener {
            viewModel.setOperator("+")
        }

        // Igual
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            viewModel.calculate()
        }

        // Limpiar
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            viewModel.clear()
        }

        // Delete
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            viewModel.delete()
        }
    }
}