package com.example.imccalc

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etWeight = findViewById<EditText>(R.id.etWeight)
        val etHeight = findViewById<EditText>(R.id.etHeight)
        val btnCalculateIMC = findViewById<Button>(R.id.btnCalculateIMC)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnCalculateIMC.setOnClickListener {
            val weight = etWeight.text.toString().toDoubleOrNull()
            val height = etHeight.text.toString().toDoubleOrNull()

            if (weight == null || height == null || height <= 0) {
                Log.e("IMC_ERROR", "Campos inválidos ou vazios")
                tvResult.text = "Erro: Insira valores válidos"
                return@setOnClickListener
            }

            val imc = BMICalculator.calculateIMC(weight, height)
            val category = BMICalculator.getIMCCategory(imc)
            tvResult.text = "IMC: %.1f - %s".format(imc, category)
            Log.d("IMC_CALC", "IMC calculado: %.1f".format(imc))
            Log.i("IMC_INFO", "Cálculo concluído com sucesso")
        }
    }
}