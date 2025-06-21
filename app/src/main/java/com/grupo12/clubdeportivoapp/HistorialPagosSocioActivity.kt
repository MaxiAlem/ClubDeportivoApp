package com.grupo12.clubdeportivoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.grupo12.clubdeportivoapp.databinding.ActivityHistorialPagosSocioBinding
import com.grupo12.clubdeportivoapp.db.PagoDao

class HistorialPagosSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistorialPagosSocioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialPagosSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagoDao = PagoDao(this)
        val pagos = pagoDao.obtenerTodosPagos()

        binding.rvHistorialPagos.layoutManager = LinearLayoutManager(this)
        binding.rvHistorialPagos.adapter = PagoAdapter(pagos)

        binding.btnBack.setOnClickListener { finish() }
        // binding.btnDescargarReporte.setOnClickListener { ... } // tu lógica para PDF
    }
}