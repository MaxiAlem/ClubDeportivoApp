package com.grupo12.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.grupo12.clubdeportivoapp.databinding.ActivityDashboardBinding
import com.grupo12.clubdeportivoapp.db.SocioDao

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarSocios()

        // Listeners de los botones
        binding.btnRegistrarSocio.setOnClickListener {
            startActivity(Intent(this, AddSocioActivity::class.java))
        }

        binding.btnEditarSocio.setOnClickListener {
            val intent = Intent(this, FindSocio::class.java)
            intent.putExtra("modo", "editar") // "editar" es tu default pero por si lñas moscas
            startActivity(intent)
        }

        binding.btnRegistrarPago.setOnClickListener {
            val intent = Intent(this, FindSocio::class.java)
            intent.putExtra("modo", "perfil") // Este es el big one
            startActivity(intent)
        }

        binding.btnReporteVencimientos.setOnClickListener {
            startActivity(Intent(this, ReporteVencimientosActivity::class.java))
        }

        binding.btnHistorialCobros.setOnClickListener {
            startActivity(Intent(this, HistorialPagosSocioActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargarSocios() // ¡Esto actualiza la lista cuando volvés!
    }

    private fun cargarSocios() {
        val socioDao = SocioDao(this)
        val listaSocios = socioDao.obtenerTodos()
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val hoy = sdf.format(java.util.Date())

        val sociosVencidos = listaSocios.filter { socio ->
            socio.vencimiento.isNullOrEmpty() || socio.vencimiento <= hoy
        }

        val datosListView = sociosVencidos.map { "${it.nombre} ${it.apellido} - Vence: ${it.vencimiento ?: "Sin fecha"}" }
        binding.lvCuotasAtrasadas.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            datosListView
        )
    }
}